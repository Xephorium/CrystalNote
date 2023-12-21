package com.xephorium.crystalnote.data.utility

import java.text.SimpleDateFormat
import java.util.*

object DateUtility {

    fun getCurrentFormattedDateTime(): String {
        return getFormattedDateTime(Calendar.getInstance().time)
    }

    fun getFormattedDateTime(date: Date): String {
        val dayFormat = SimpleDateFormat("M/d/yy", Locale.US)
        val hourFormat = SimpleDateFormat("h:mma", Locale.US)
        return dayFormat.format(date) + " " + hourFormat.format(date).lowercase(Locale.ROOT)
    }
}