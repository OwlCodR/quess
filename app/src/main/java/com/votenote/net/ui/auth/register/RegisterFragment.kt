package com.votenote.net.ui.auth.register

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.R
import com.votenote.net.databinding.FragmentRegisterBinding
import com.votenote.net.log
import com.votenote.net.model.Answer
import com.votenote.net.model.Meta
import com.votenote.net.model.User
import com.votenote.net.retrofit.common.Common
import com.votenote.net.retrofit.service.RetrofitServices
import com.votenote.net.ui.auth.AuthActivity
import com.votenote.net.ui.auth.AuthViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private lateinit var progressBar: ProgressBar

    private lateinit var authActivity: AuthActivity

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private lateinit var retrofitService: RetrofitServices

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        log(context, "onCreateView()")

        retrofitService = Common.retrofitService

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        inputTag = binding.textInputLayoutTag
        inputPhone = binding.textInputLayoutPhone
        progressBar = binding.progressBar
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
            navController.popBackStack()
        }

        textViewInvitationCode.setOnClickListener {

        }

        countryCodePicker.registerCarrierNumberEditText(binding.editTextPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener {
            if (countryCodePicker.isValidFullNumber) {
                inputPhone.isErrorEnabled = false
            }
        }

        inputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authActivity.checkPasswordValid(inputPassword)
            }
        })

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

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun onRegister() {
        val password = inputPassword.editText?.text.toString()
        val phone = countryCodePicker.fullNumberWithPlus
        val tag = inputTag.editText?.toString()

        val isTagValid = checkTagValid()
        val isTagUnique = isTagUnique(inputTag.editText?.text.toString())

        val isPhoneValid = countryCodePicker.isValidFullNumber
        val isPhoneUnique = isPhoneUnique(countryCodePicker.fullNumber)

        val isPasswordValid = authActivity.checkPasswordValid(inputPassword)

        if (arePasswordsEqual() && isPasswordValid &&
            isPhoneValid && isTagValid &&
            isPhoneUnique && isTagUnique) {

            showProgressBar()

            retrofitService.register(User(password=password, phone=phone, tag=tag, meta=Meta("0.0")))
                .enqueue(object : Callback<Answer> {
                override fun onFailure(call: Call<Answer>, t: Throwable) {
                    Snackbar.make(
                        requireView(),
                        "An error has occurred!\nCheck internet connection or try later",
                        Snackbar.LENGTH_LONG
                    ).show()
                    log(context, t.message.toString())

                    hideProgressBar()
                }

                override fun onResponse(call: Call<Answer>, response: Response<Answer>) {
                    log(context, "response.isSuccessful = " + response.isSuccessful)

                    if (response.isSuccessful) {
                        val body = response.body()
                        val errorCode = body?.errorCode

                    } else {
                        log(context, response.errorBody()?.string())
                    }
                    Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show()

                    hideProgressBar()
                }
            })

        } else {
            Snackbar.make(
                requireView(),
                "Registration error.\nCheck the correctness of the entered data.",
                Snackbar.LENGTH_SHORT
            ).show()

            if (!arePasswordsEqual()) {
                inputRepeatPassword.isErrorEnabled = true
                inputRepeatPassword.error = "Wrong password"
                inputRepeatPassword.hint = "Password mismatch"
            }
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

    private fun arePasswordsEqual(): Boolean {
        val password = inputPassword.editText?.text.toString()
        val repeatPassword = inputRepeatPassword.editText?.text.toString()

        return password == repeatPassword
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
            Regex("\\s").containsMatchIn(tag) -> {
                errorHint = "Tag must not contain spaces"
            }
            Regex("[^a-zA-Z0-9_]").containsMatchIn(tag) -> {
                log(context, Regex("[^a-zA-Z0-9_]").containsMatchIn(tag).toString())
                errorHint = "Only english letters, digits or '_' are allowed"
            }
            else -> {
                log(context, "Tag is valid")
                inputTag.isErrorEnabled = false
                inputTag.hint = "Tag"
                return true
            }
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
