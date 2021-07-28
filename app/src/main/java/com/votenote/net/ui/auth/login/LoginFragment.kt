package com.votenote.net.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.R
import com.votenote.net.databinding.FragmentLoginBinding
import com.votenote.net.log
import com.votenote.net.ui.auth.AuthActivity
import com.votenote.net.ui.auth.AuthViewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var inputPhone: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var textViewSignUp: TextView

    private lateinit var authActivity: AuthActivity

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        log(context, "onCreateView()")

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.handler = this

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        inputPhone = binding.textInputLayoutPhone
        inputPassword = binding.textInputLayoutPassword
        countryCodePicker = binding.countryCodePicker
        textViewSignUp = binding.textViewSignUpNow

        navHostFragment =
            activity?.
            supportFragmentManager?.
            findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController

        authActivity = activity as AuthActivity
        binding.authActivity = authActivity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log(context, "onViewCreated()")

        textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_nav_login_to_nav_register)
        }

        inputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authActivity.checkPasswordValid(inputPassword)
            }
        })

        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber)
                inputPhone.isErrorEnabled = false
        }
    }

    fun onLogin() {
//        val password: String = inputPassword.editText?.text.toString()
//        val phone: String = inputPhone.editText?.text.toString()
        val isPhoneValid = countryCodePicker.isValidFullNumber
        val isPasswordValid = authActivity.checkPasswordValid(inputPassword)

        if (isPasswordValid && isPhoneValid) {
            Toast.makeText(context, "YOU ARE LOGGED IN NOW", Toast.LENGTH_SHORT).show()
            // @TODO Retrofit login request
        } else if (!isPasswordValid) {
            inputPassword.isErrorEnabled = true
            inputPassword.error = "Wrong password"
        } else if (!isPhoneValid) {
            inputPhone.isErrorEnabled = true
            inputPhone.error = "Wrong phone number"
        }
    }
}

/*
@TODO Debug subtitle - Done
@TODO Error checker - Done
@TODO Retrofit
@TODO onClick TextViews - Done
@TODO Move reading subtitles to SplashScreen
 */