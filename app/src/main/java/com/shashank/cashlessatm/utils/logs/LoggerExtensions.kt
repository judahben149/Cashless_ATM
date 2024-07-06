package com.shashank.cashlessatm.utils.logs

import android.util.Log

fun String?.logThis(tag: String = "TAG") {
    Log.d(tag, this.toString())
}

fun Int?.logThis(tag: String = "TAG") {
    Log.d(tag, this.toString())
}