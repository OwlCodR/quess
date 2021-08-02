package com.votenote.net.enums

enum class ErrorCodes(val code: String) {
    BAD_FORMAT("0110"),
    BAD_PHONE_FORMAT("0111"),
    BAD_TAG_FORMAT("0112"),
    BAD_PASSWORD_FORMAT("0113"),
    PHONE_EXISTS("0121"),
    TAG_EXISTS("0122"),
    BAD_INVITE_CODE("0123"),
    BAD_SMS_CODE("0131")
}