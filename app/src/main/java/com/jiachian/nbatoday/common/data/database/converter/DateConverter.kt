package com.jiachian.nbatoday.common.data.database.converter

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
