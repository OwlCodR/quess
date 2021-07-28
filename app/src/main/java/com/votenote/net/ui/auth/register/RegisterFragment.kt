package com.votenote.net.ui.auth.register

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.R
import com.votenote.net.databinding.FragmentRegisterBinding
import com.votenote.net.log
import com.votenote.net.ui.auth.AuthActivity
import com.votenote.net.ui.auth.AuthViewModel

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var inputTag: TextInputLayout
    private lateinit var inputPhone: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputRepeatPassword: TextInputLayout
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var textViewSignIn: TextView
    private lateinit var textViewInvitationCode: TextView

    private lateinit var authActivity: AuthActivity

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        log(context, "onCreateView()")

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        inputTag = binding.textInputLayoutTag
        inputPhone = binding.textInputLayoutPhone
        inputPassword = binding.textInputLayoutPassword
        textViewSignIn = binding.textViewSignInNow
        countryCodePicker = binding.countryCodePicker
        inputRepeatPassword = binding.textInputLayoutRepeatPassword
        textViewInvitationCode = binding.textViewInvitationCode

        navHostFragment =
            activity?.
            supportFragmentManager?.
            findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController

        authActivity = activity as AuthActivity
        binding.authActivity = authActivity
        binding.handler = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log(context, "onViewCreated()")

        textViewSignIn.setOnClickListener {
            navController.navigate(R.id.action_nav_login_to_nav_register)
        }
        textViewInvitationCode.setOnClickListener {  }

        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber)
                inputPhone.isErrorEnabled = false
        }

        inputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authActivity.checkPasswordValid(inputPassword)
            }
        })

        inputTag.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkTagValid()
            }
        })
    }

    fun onRegister() {
        val password = inputPassword.editText?.text.toString()
        val repeatPassword = inputRepeatPassword.editText?.text.toString()

        val isTagValid = checkTagValid()
        val isTagUnique = isTagUnique(inputTag.editText?.text.toString())

        val isPhoneValid = countryCodePicker.isValidFullNumber
        val isPhoneUnique = isPhoneUnique(countryCodePicker.fullNumber)

        val isPasswordValid = authActivity.checkPasswordValid(inputPassword)

        if (isPasswordValid && isPhoneValid && isTagValid && isPhoneUnique && isTagUnique) {
            Toast.makeText(context, "YOU ARE LOGGED IN NOW", Toast.LENGTH_SHORT).show()
            // @TODO Retrofit login request
        } else {
            if (!isPasswordValid) {
                inputPassword.isErrorEnabled = true
                inputPassword.error = "Wrong password"
            }
            if (!isPhoneValid) {
                inputPhone.isErrorEnabled = true
                inputPhone.error = "Wrong phone number"
            }
            if (!isPhoneUnique) {
                inputPhone.isErrorEnabled = true
                inputPhone.error = "Phone number is not unique"
                inputPhone.hint = "This phone already exists"
            }
            if (!isTagValid) {
                inputTag.isErrorEnabled = true
                inputTag.error = "Wrong tag"
            }
            if (!isTagUnique) {
                inputTag.isErrorEnabled = true
                inputTag.error = "Phone number is not unique"
                inputTag.hint = "This tag already exists"
            }
        }
    }

    private fun checkTagValid(): Boolean {
        val tag: String = inputTag.editText?.text.toString()
        val errorHint: String
        when {
            tag.length < 6 -> {
                errorHint = "Tag is too short"
            }
            tag.length > 16 -> {
                errorHint = "Tag is too long"
            }
            Regex(".*/s.*").containsMatchIn(tag) -> {
                errorHint = "Tag must not contain spaces"
            }
            Regex(".*^/w.*").containsMatchIn(tag) -> {
                errorHint = "Tag must contain only letters, digits or underscores"
            }
            isTagUnique(tag) -> {
                log(context, "Password is valid and unique")
                return true
            }
            else -> errorHint = "Tag"
        }
        inputTag.hint = errorHint
        log(context, "Tag errorHint = $errorHint")
        return false
    }

    private fun isPhoneUnique(phone: String): Boolean {
        log(context, "isPhoneUnique()")
        // @TODO Make request
        return true
    }

    private fun isTagUnique(tag: String): Boolean {
        log(context, "isTagUnique()")
        // @TODO Make request
        return true
    }
}