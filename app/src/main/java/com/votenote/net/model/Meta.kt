package com.votenote.net.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta (
    @field:Json(name = "version") val version: String? = null
)
