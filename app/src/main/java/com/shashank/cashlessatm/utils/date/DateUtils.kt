package com.shashank.cashlessatm.utils.date

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {

    val timeAndDateFormatter = SimpleDateFormat("MMddHHmmss", Locale.ROOT) // Field 7

    val timeFormatter = SimpleDateFormat("HHmmss", Locale.ROOT) // Field 12

    val dateFormatter = SimpleDateFormat("MMdd", Locale.ROOT) // Field 13

}