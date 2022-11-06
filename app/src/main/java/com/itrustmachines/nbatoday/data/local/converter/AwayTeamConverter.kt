package com.itrustmachines.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itrustmachines.nbatoday.data.local.NbaGame

class AwayTeamConverter {
    @TypeConverter
    fun fromAwayTeam(value: NbaGame.NbaAwayTeam): String {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaAwayTeam>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAwayTeam(value: String): NbaGame.NbaAwayTeam {
        val gson = Gson()
        val type = object : TypeToken<NbaGame.NbaAwayTeam>() {}.type
        return gson.fromJson(value, type)
    }
}