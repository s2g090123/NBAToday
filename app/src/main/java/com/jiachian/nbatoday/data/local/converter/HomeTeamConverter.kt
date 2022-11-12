package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.NbaGame

class HomeTeamConverter {
    @TypeConverter
    fun fromHomeTeam(value: NbaGame.NbaHomeTeam): String {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaHomeTeam>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toHomeTeam(value: String): NbaGame.NbaHomeTeam {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaHomeTeam>() {}.type
        return gson.fromJson(value, type)
    }
}