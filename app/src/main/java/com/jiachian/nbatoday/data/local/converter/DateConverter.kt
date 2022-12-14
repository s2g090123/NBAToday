package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun to(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun from(value: Date): Long {
        return value.time
    }
}