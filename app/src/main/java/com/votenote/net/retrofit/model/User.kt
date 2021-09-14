package com.votenote.net.retrofit.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User (
    @field:Json(name = "phone") val phone: String? = null,
    @field:Json(name = "password") val password: String? = null,
    @field:Json(name = "tag") val tag: String? = null,
    @field:Json(name = "meta") val meta: Meta? = null,
    @field:Json(name = "invitecode") val inviteCode: String? = null
)
