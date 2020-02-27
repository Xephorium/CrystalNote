package com.xephorium.crystalnote.data.room

import androidx.room.TypeConverter
import java.util.*

class RoomConverters {

    @TypeConverter
    fun fromDate(date: Long) : Date {
        return Date(date)
    }

    @TypeConverter
    fun toDate(date: Date) : Long {
        return date.time
    }
}