package com.shashank.cashlessatm.utils.iso

import java.util.Locale
import javax.inject.Inject
import com.shashank.cashlessatm.utils.Constants.KEY_STAN
import com.shashank.cashlessatm.utils.PreferencesHelper
import com.shashank.cashlessatm.utils.SecurityUtils
import com.shashank.cashlessatm.utils.logs.logThis
import com.shashank.cashlessatm.utils.typeUtils.StringUtils.padOrTruncate
import org.jpos.iso.ISOComponent
import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.GenericPackager
import java.lang.System.currentTimeMillis

class IsoUtils @Inject constructor(private val prefsHelper: PreferencesHelper) {

    fun getNextStan(): String {
        var stan = prefsHelper.getLong(KEY_STAN, 0)

        val nextStan = if (stan > 999999) 0 else ++stan
        prefsHelper.putLong(KEY_STAN, nextStan)

        return String.format(Locale.getDefault(), "%06d", nextStan)
    }

    fun generateRrn(separator: String): String {
        val randomString = SecurityUtils.getSecureRandom().nextInt(999999).toString()
        val sixDigitRandomString = padOrTruncate(randomString, 6)

        val timeInMillis = currentTimeMillis()
        val lastFiveDigits = timeInMillis % 100_000

        return sixDigitRandomString + separator + lastFiveDigits.toString()
    }


    fun parseAndLogIsoMessage(data: String, packager: GenericPackager): Map<String, String>? {
        var hashMap = HashMap<String, String>()

        try {
            val message = ISOMsg()
            message.packager = packager
            message.unpack(data.toByteArray())
            "-------ISO MESSAGE-------".logThis()

//            "MTI - ${message.getString("0.2")}".logThis()
//            "MTI - ${message.getString("0.3")}".logThis()
            "MTI - ${message.getString("1")}".logThis()

//            hashMap["0"] = message.mti
//            "MTI- ${message.mti}".logThis()

//            for (field in 1..message.maxField) {
//                if (message.hasField(field)) {
//                    hashMap[field.toString()] = message.getString(field)
//                    "Field $field - ${message.getString(field)}".logThis()
//
//                    val subField = message.getComponent(field)
//                    if (subField != null) {
//                        dumpSubFields(subField, field.toString())
//                    }
//                }
//            }
        } catch (e: ISOException) {
            "Error: ${e.message.toString()}".logThis()
        } finally {
            "------------------------".logThis()
        }

        return hashMap
    }

    private fun dumpSubFields(component: ISOComponent, fieldNumber: String) {

        for (subField in 1.. component.maxField) {
            try {
                (fieldNumber + " : " + subField.toString() + " - " + component.value).logThis()
            } catch (ex: ISOException) {
                "subfield N/A in component".logThis()
            }
        }
    }

    fun concatenateByteArrays(vararg arrays: ByteArray): ByteArray? {
        var finalLength = 0

        for (array in arrays) {
            finalLength += array.size
        }

        var dest: ByteArray? = null
        var destPos = 0

        for (array in arrays) {
            if (dest == null) {
                dest = array.copyOf(finalLength)
                destPos = array.size
            } else {
                System.arraycopy(array, 0, dest, destPos, array.size)
                destPos += array.size
            }
        }
        return dest
    }
}