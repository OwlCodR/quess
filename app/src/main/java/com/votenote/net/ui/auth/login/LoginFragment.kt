package com.votenote.net.ui.auth.login

import android.os.Bundle
import android.util.Log
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        log(context, "onCreateView!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        textInputLayoutPhone = binding.textInputLayoutLoginPhone

        countryCodePicker = binding.countryCodePicker
        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber)
                textInputLayoutPhone.isErrorEnabled = false
        }

        val subtitles = resources.getStringArray(R.array.subtitles)
        val rand = subtitles.indices.random()
        binding.subtitle = subtitles[rand]
        log(context, "Subtitle today is ${binding.subtitle}")

        binding.handler = this
        log(context, "onViewCreated!")
    }

    fun onLogin() {
        val phone = viewModel.getPhone()
        val password = viewModel.getPassword()

        if (countryCodePicker.isValidFullNumber) {
            Toast.makeText(activity, countryCodePicker.formattedFullNumber, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "INVALID", Toast.LENGTH_SHORT).show()
            textInputLayoutPhone.error = "Wrong phone number"
        }
        log(context, "Phone number valid = ${countryCodePicker.isValidFullNumber}")
    }
}

/*
@TODO Error checker
@TODO Retrofit
@TODO onClick TextViews
 */