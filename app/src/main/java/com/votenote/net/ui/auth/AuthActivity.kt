package com.votenote.net.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.votenote.net.R
import com.votenote.net.databinding.ActivityAuthBinding
import com.votenote.net.log

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val hideHandler = Handler()

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.

    }

    private val hideRunnable = Runnable { hide() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(this, "onCreate()")

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val isFirst = intent.getBooleanExtra("isFirst", true)
        log(this, "isFirst = $isFirst")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    fun checkPasswordValid(inputPassword: TextInputLayout): Boolean {
        val errorHint: String
        val password: String = inputPassword.editText?.text.toString()

        val minLength = 8
        val maxLength = 128

        when {
            password.length < minLength -> {
                errorHint = "Password is too short"
            }
            password.length > maxLength -> {
                errorHint = "Password is too long"
            }
            !Regex("[0-9]").containsMatchIn(password) -> {
                errorHint = "Password must contain at least 1 digit"
            }
            !Regex("[a-zA-Z]").containsMatchIn(password) -> {
                errorHint = "Password must contain at least 1 letter"
            }
            !Regex("[A-Z]").containsMatchIn(password) -> {
                errorHint = "Password must contain at least 1 uppercase letter"
            }
            else -> {
                inputPassword.isErrorEnabled = false
                inputPassword.hint = "Password"
                return true
            }
        }
        inputPassword.hint = errorHint
        log(this, "Password error_hint = $errorHint")
        return false
    }

    fun getSubtitle(): String {
        val subtitles = resources.getStringArray(R.array.subtitles)
        val rand = subtitles.indices.random()
        val subtitle = subtitles[rand]
        log(this, "Subtitle today is '${subtitle}'")
        return subtitle
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}
