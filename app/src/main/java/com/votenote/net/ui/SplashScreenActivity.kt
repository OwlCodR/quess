package com.votenote.net.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.votenote.net.MainActivity
import com.votenote.net.MainActivityOld
import com.votenote.net.R
import com.votenote.net.enums.SharedPreferencesTags
import com.votenote.net.retrofit.model.Answer
import com.votenote.net.retrofit.common.Common
import com.votenote.net.retrofit.service.RetrofitServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_TIME: Long = 500 // ms

    private lateinit var sharedPreference: SharedPreferences
    private lateinit var retrofitService: RetrofitServices
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash_screen)

        view = findViewById(R.id.splash_screen_container)

        sharedPreference = getSharedPreferences(MainActivityOld().APP_PREFERENCE, Context.MODE_PRIVATE)
        retrofitService = Common.retrofitService

        Handler(Looper.getMainLooper()).postDelayed({
            checkFirstStart()
        }, SPLASH_SCREEN_TIME)

        setApiVersion()
    }

    private fun setApiVersion() {
        retrofitService
            .getVersion()
            .enqueue(
                object: Callback<Answer> {
                    override fun onFailure(call: Call<Answer>, t: Throwable) {
                        onFailure(t)
                    }

                    override fun onResponse(call: Call<Answer>, response: Response<Answer>) {
                        onResponse(response)
                    }
                }
            )
    }

    private fun onFailure(t: Throwable) {
        //log(this, t.message.toString())
    }

    private fun onResponse(response: Response<Answer>) {
        //log(this, "response.isSuccessful = " + response.isSuccessful)
        //log(this, "errorBody() = ${response.errorBody()?.string()}")

        if (response.isSuccessful) {
            val body = response.body()
            val errorCode = body?.errorCode
            val apiVersion = body?.meta?.version

            //log(this, "apiVersion = $apiVersion")

            if (errorCode == "0000") {
                sharedPreference
                    .edit()
                    .putString(SharedPreferencesTags.API_VERSION.tag, apiVersion)
                    .apply()
            } else {
                //log(this, "An error[$errorCode] has occurred!\n")
            }
        }
    }

    private fun checkFirstStart() {
        val isFirst = sharedPreference.getBoolean("isFirst", true)
        val loggedIn = sharedPreference.getBoolean("loggedIn", false)

        //log(this, "isFirst = $isFirst")
        //log(this, "loggedIn = $loggedIn")

        if (isFirst || !loggedIn) {
            sharedPreference
                .edit()
                .putBoolean("isFirst", false)
                .apply()

            // @TODO Change to AuthActivity
            startActivity(MainActivity::class.java)
        } else {
            startActivity(MainActivity::class.java)
        }
    }

    private fun startActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}
