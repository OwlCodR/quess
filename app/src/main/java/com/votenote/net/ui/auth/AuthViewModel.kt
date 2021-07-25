package com.votenote.net.ui.auth

import android.content.ClipData.Item
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AuthViewModel: ViewModel() {
    private val phone = MutableLiveData<String>()
    private val password = MutableLiveData<String>()

    fun getPhone(): LiveData<String> {
        return phone
    }

    fun getPassword(): LiveData<String> {
        return password
    }
}