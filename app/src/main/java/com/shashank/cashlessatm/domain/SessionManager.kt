package com.shashank.cashlessatm.domain

import com.shashank.cashlessatm.utils.Constants.DEFAULT_IP_ADDRESS
import com.shashank.cashlessatm.utils.Constants.DEFAULT_PORT
import com.shashank.cashlessatm.utils.Constants.DEFAULT_TIMEOUT
import com.shashank.cashlessatm.utils.Constants.KEY_IP_ADDRESS
import com.shashank.cashlessatm.utils.Constants.KEY_PORT
import com.shashank.cashlessatm.utils.Constants.KEY_TIMEOUT
import com.shashank.cashlessatm.utils.PreferencesHelper
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val prefs: PreferencesHelper
) {

    fun getIp() = prefs.getString(KEY_IP_ADDRESS, DEFAULT_IP_ADDRESS)
    fun getPort() = prefs.getInt(KEY_PORT, DEFAULT_PORT)
    fun getTimeout() = prefs.getInt(KEY_TIMEOUT, DEFAULT_TIMEOUT)
}