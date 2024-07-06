package com.shashank.cashlessatm.utils

import java.security.SecureRandom

object SecurityUtils {

    fun getSecureRandom(): SecureRandom {
        return SecureRandom.getInstance("SHA1PRNG").apply {
            setSeed(generateSeed(9))

            val bytes = ByteArray(10)
            nextBytes(bytes)
        }
    }

}