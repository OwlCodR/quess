package com.votenote.net.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.votenote.net.MainActivity
import com.votenote.net.R
import com.votenote.net.log
import com.votenote.net.ui.auth.AuthActivity

class SplashScreenActivity : AppCompatActivity() {
    val START_PREFERENCE = "START_PREF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            checkFirstStart()
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
        },1000)
    }

    private fun checkFirstStart() {
        val sharedPreference = getSharedPreferences(START_PREFERENCE, Context.MODE_PRIVATE)

        // sharedPreference.edit().clear().apply()

        val isFirst = sharedPreference.getBoolean("isFirst", true)
        val loggedIn = false

        if (isFirst || !loggedIn) {
            log(this, "First start or not logged in")

            sharedPreference
                .edit()
                .putBoolean("isFirst", false)
                .apply()

            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("isFirst", isFirst)
            startActivity(intent)
            finish()
        } else
            log(this, "Not first login or already logged in")
    }
}