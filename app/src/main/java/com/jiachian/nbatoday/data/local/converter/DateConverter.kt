package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun timeToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTime(value: Date): Long {
        return value.time
    }
}