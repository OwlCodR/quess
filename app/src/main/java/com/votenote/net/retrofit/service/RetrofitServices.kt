package com.votenote.net.retrofit.service

import com.votenote.net.retrofit.model.Answer
import com.votenote.net.retrofit.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitServices {
    @GET("version")
    fun getVersion(): Call<Answer>

    @POST("register")
    fun register(@Body user: User): Call<Answer>

    @GET("login")
    fun login(@Body user: User): Call<Answer>
}
