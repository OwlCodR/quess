package com.votenote.net.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.R
import com.votenote.net.databinding.FragmentLoginBinding
import com.votenote.net.log
import com.votenote.net.ui.auth.AuthViewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var textInputLayoutPhone: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        textInputLayoutPhone = binding.textInputLayoutLoginPhone
        textInputLayoutPassword = binding.textInputLayoutLoginPassword
        countryCodePicker = binding.countryCodePicker
        binding.handler = this


        log(context, "onCreateView!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        textInputLayoutPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (checkPasswordValid(textInputLayoutPassword))
                    log(context, "Password is valid")
            }
        })

        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber)
                textInputLayoutPhone.isErrorEnabled = false
        }

        setSubtitle()

        log(context, "onViewCreated!")
    }

    private fun setSubtitle() {
        val subtitles = resources.getStringArray(R.array.subtitles)
        val rand = subtitles.indices.random()
        binding.subtitle = subtitles[rand]
        log(context, "Subtitle today is '${binding.subtitle}'")
    }

    fun checkPasswordValid(inputPassword: TextInputLayout): Boolean {
        val errorHint: String
        val password: String = inputPassword.editText?.text.toString()
        when {
            password.length < 8 -> {
                errorHint = "Password is too short"
            }
            password.length > 128 -> {
                errorHint = "Password is too long"
            }
            Regex(".*[^0-9].*").containsMatchIn(password) -> {
                errorHint = "Password must contain at least 1 digit"
            }
            Regex(".*[^a-zA-Z].*").containsMatchIn(password) -> {
                errorHint = "Password must contain at least 1 letter"
            }
            else -> {
                inputPassword.isErrorEnabled = false
                inputPassword.hint = "Password"
                return true
            }
        }
        inputPassword.hint = errorHint
        log(context, "Password error_hint = $errorHint")
        return false
    }

    fun onLogin() {
//        val phone = viewModel.getPhone()
//        val password = viewModel.getPassword()

        val password: String = textInputLayoutPassword.editText?.text.toString()
        val phone: String = textInputLayoutPhone.editText?.text.toString()

        if (checkPasswordValid(textInputLayoutPassword) && checkPhoneValid()) {
            Toast.makeText(context, "YOU ARE LOGGED IN NOW", Toast.LENGTH_SHORT).show()
        } else if (!checkPasswordValid(textInputLayoutPassword)) {
            textInputLayoutPassword.isErrorEnabled = true
            textInputLayoutPassword.error = "Wrong password"
        } else if (!checkPhoneValid()) {
            textInputLayoutPhone.isErrorEnabled = true
            textInputLayoutPhone.error = "Wrong phone number"
        }
    }

    private fun checkPhoneValid(): Boolean {
        log(context, "Phone number valid = ${countryCodePicker.isValidFullNumber}")
        return countryCodePicker.isValidFullNumber
    }
}

/*
@TODO Debug subtitle - Done
@TODO Error checker
@TODO Retrofit
@TODO onClick TextViews
 */