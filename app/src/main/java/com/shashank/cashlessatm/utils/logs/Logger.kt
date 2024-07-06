package com.shashank.cashlessatm.utils.logs

import android.util.Log
import com.shashank.cashlessatm.utils.Constants.DEFAULT

object Logger {

    fun log(string: String, tag: String = DEFAULT) {
        Log.d(tag, "Kozen Sync --- $string")
    }

    fun log(string: Int, tag: String = DEFAULT) {
        Log.d(tag, "Kozen Sync --- $string")
    }

    fun log(string: Boolean, tag: String = DEFAULT) {
        Log.d(tag, "Kozen Sync --- $string")
    }

    fun log(string: Any, tag: String = DEFAULT) {
        Log.d(tag, "Kozen Sync --- $string")
    }
}