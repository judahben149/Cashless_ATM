package com.shashank.cashlessatm.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {


    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().apply {
            this.putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().apply {
            this.putLong(key, value)
            apply()
        }
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().apply {
            this.putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String? = null): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            this.putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        sharedPreferences.edit().apply {
            this.putFloat(key, value)
            apply()
        }
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }
}