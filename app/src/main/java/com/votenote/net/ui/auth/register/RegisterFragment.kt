package com.votenote.net.ui.auth.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.votenote.net.*
import com.votenote.net.databinding.FragmentRegisterBinding
import com.votenote.net.enums.ErrorCodes
import com.votenote.net.enums.SharedPreferencesTags
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
import com.squareup.moshi.JsonAdapter

import com.squareup.moshi.Moshi


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

    private lateinit var sharedPreference: SharedPreferences
    private var inviteCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        log(context, "onCreateView()")

        sharedPreference = requireActivity().getSharedPreferences(
                MainActivity().APP_PREFERENCE,
                Context.MODE_PRIVATE
            )

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
            showInviteCodeDialog()
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
                authActivity.checkPasswordValidity(inputPassword)
            }
        })

        inputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authActivity.checkPasswordValidity(inputPassword)
            }
        })

        inputRepeatPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (arePasswordsEqual()) {
                    inputRepeatPassword.isErrorEnabled = false
                    inputRepeatPassword.hint = "Repeat password"
                }
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

    private fun showInviteCodeDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater

        val dialogLayout = inflater.inflate(R.layout.dialog_invite_code, null)
        val inviteCodeLayout = dialogLayout.findViewById<TextInputLayout>(R.id.textInputLayoutInviteCode)

        builder.setTitle("Invite code")
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            inviteCode = inviteCodeLayout.editText?.text.toString()
        }
        builder.show()

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun onRegister() {
        val password = inputPassword.editText?.text.toString()
        val phone = countryCodePicker.fullNumberWithPlus.trim()
        val tag = inputTag.editText?.text.toString()

        val isTagValid = checkTagValid()
        val isPhoneValid = countryCodePicker.isValidFullNumber
        val isPasswordValid = authActivity.checkPasswordValidity(inputPassword)

        if (arePasswordsEqual() && inviteCode != null &&
            isPasswordValid && isPhoneValid && isTagValid) {

            showProgressBar()

            val apiVersion = sharedPreference.getString(
                SharedPreferencesTags.API_VERSION.name,
                    BuildConfig.DEFAULT_API_VERSION)

            val newUser = User(password=password, phone=phone, tag=tag, meta=Meta(apiVersion))

            retrofitService
                .register(newUser)
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
        } else {
            showSnackbar("Registration error.\nCheck the correctness of the entered data.")

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
            if (!isTagValid) {
                inputTag.isErrorEnabled = true
                inputTag.error = "Wrong tag"
            }
            if (inviteCode == null) {
                showSnackbar("Error!\nAn invite code is required.")
            }
        }
    }

    private fun onFailure(t: Throwable) {
        log(context, t.message.toString())

        showSnackbar("An error has occurred!\nCheck internet connection or try later")

        hideProgressBar()
    }

    private fun onResponse(response: Response<Answer>) {
        log(context, "response.isSuccessful = " + response.isSuccessful)

        if (response.isSuccessful) {
            val body = response.body()
            val code: String? = body?.errorCode

            if (code == "0000") {
                log(context, "User has been registered")

                sharedPreference
                    .edit()
                    .putBoolean("loggedIn", true)
                    .apply()
                navController.navigate(R.id.action_nav_register_to_nav_main)
            }
        } else {
            /*
                There are 2 ways:
                1. We have some unpredictable server exception, i.e. html code of error-page
                2. We have some predictable json with "error" and "meta" fields
             */

            val errorBodyString = response.errorBody()!!.string()
            val isHtmlPage = errorBodyString.contains("HTML")

            if (isHtmlPage) {
                log(context, "An error[SERVER_ERROR] has occurred!\n")
            } else {
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<Answer> = moshi.adapter<Answer>(Answer::class.java)

                val answer: Answer? = jsonAdapter.fromJson(errorBodyString)
                val errorCode = answer?.errorCode

                when (errorCode) {
                    ErrorCodes.PHONE_EXISTS.name -> {
                        inputPhone.isErrorEnabled = true
                        inputPhone.error = "Phone number is not unique"
                        inputPhone.hint = "This phone already exists"
                    }
                    ErrorCodes.TAG_EXISTS.name -> {
                        inputTag.isErrorEnabled = true
                        inputTag.error = "Phone number is not unique"
                        inputTag.hint = "This tag already exists"
                    }
                }

                log(context, "An error[$errorCode] has occurred!")
                showSnackbar("An error[$errorCode] has occurred!")
            }
        }

        hideProgressBar()
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

    private fun showSnackbar(s: String) {
        Snackbar.make(requireView(), s, Snackbar.LENGTH_LONG).show()
    }
}
