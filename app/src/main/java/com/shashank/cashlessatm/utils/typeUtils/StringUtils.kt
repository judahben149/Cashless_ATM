package com.shashank.cashlessatm.utils.typeUtils

object StringUtils {

    fun padOrTruncate(str: String, length: Int): String {
        return if (str.length >= length) {
            str.substring(0, length)
        } else {
            str.padStart(length, '0')
        }
    }
}