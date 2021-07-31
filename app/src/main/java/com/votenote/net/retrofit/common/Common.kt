package com.votenote.net.retrofit.common

import com.votenote.net.BuildConfig
import com.votenote.net.retrofit.RetrofitClient
import com.votenote.net.retrofit.service.RetrofitServices


object Common {
    private val BASE_URL = BuildConfig.API_URL

    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}
