package com.votenote.net.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User (
    val phone: String? = null,
    val password: String? = null,
    val tag: String? = null,
    val meta: Meta? = null
)
