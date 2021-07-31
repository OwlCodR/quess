package com.votenote.net.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answer (
    @field:Json(name = "error") val errorCode: String? = null,
    @field:Json(name = "meta") val meta: Meta? = null
)
