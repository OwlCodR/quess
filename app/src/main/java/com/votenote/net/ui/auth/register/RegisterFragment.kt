package com.votenote.net.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.databinding.FragmentRegisterBinding
import com.votenote.net.log
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        inputTag = binding.textInputLayoutRegisterTag
        inputPhone = binding.textInputLayoutLoginPhone
        inputPassword = binding.textInputLayoutRegisterPassword
        inputRepeatPassword = binding.textInputLayoutRegisterRepeatPassword

        binding.handler = this

        log(context, "onCreateView!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        countryCodePicker = binding.countryCodePicker
        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber)
                inputPhone.isErrorEnabled = false
        }

        log(context, "onViewCreated!")
    }

    fun onRegister() {

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
            Regex(".*[^/w].*").containsMatchIn(tag) -> {
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

    private fun checkPhone(): Boolean {
        return countryCodePicker.isValidFullNumber
    }

    private fun isPhoneUnique(phone: String): Boolean {
        log(context, "isPhoneUnique()")
        return true
    }

    private fun isTagUnique(tag: String): Boolean {
        log(context, "isTagUnique()")
        return true
    }
}