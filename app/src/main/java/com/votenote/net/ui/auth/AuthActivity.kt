package com.votenote.net.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.votenote.net.R
import com.votenote.net.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        //log(this, "onCreate()")

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFirst = intent.getBooleanExtra("isFirst", true)
        //log(this, "isFirst = $isFirst")
    }

    fun checkPasswordValidity(inputPassword: TextInputLayout): Boolean {
        val error: String
        val password: String = inputPassword.editText?.text.toString()

        val minLength = 8
        val maxLength = 128

        inputPassword.isErrorEnabled = true

        when {
            password.length < minLength -> {
                error = "Password is too short"
            }
            password.length > maxLength -> {
                error = "Password is too long"
            }
            !Regex("[0-9]").containsMatchIn(password) -> {
                error = "Password must contain at least 1 digit"
            }
            !Regex("[a-zA-Z]").containsMatchIn(password) -> {
                error = "Password must contain at least 1 letter"
            }
            !Regex("[A-Z]").containsMatchIn(password) -> {
                error = "Password must contain at least 1 uppercase letter"
            }
            else -> {
                inputPassword.isErrorEnabled = false
                return true
            }
        }

        inputPassword.error = error
        //log(this, "Password error_hint = $error")
        return false
    }

    fun getSubtitle(): String {
        val subtitles = resources.getStringArray(R.array.subtitles)
        val rand = subtitles.indices.random()
        val subtitle = subtitles[rand]
        //log(this, "Subtitle today is '${subtitle}'")
        return subtitle
    }
}
