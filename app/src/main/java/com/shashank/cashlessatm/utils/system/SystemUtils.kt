package com.shashank.cashlessatm.utils.system

import org.jpos.util.Logger

object SystemUtils {

    fun fixXmlParserIssue() {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver")

        val versionShown = Logger::class.java.getDeclaredField("versionShown")

        versionShown.apply {
            isAccessible = true
            set(null, true)
        }
    }
}