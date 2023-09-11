package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

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
