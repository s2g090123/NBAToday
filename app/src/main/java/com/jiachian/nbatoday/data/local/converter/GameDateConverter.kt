package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.NbaGame

class GameDateConverter {
    @TypeConverter
    fun fromGameDate(value: NbaGame.NbaGameDate): String {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaGameDate>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAwayTeam(value: String): NbaGame.NbaGameDate {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaGameDate>() {}.type
        return gson.fromJson(value, type)
    }
}