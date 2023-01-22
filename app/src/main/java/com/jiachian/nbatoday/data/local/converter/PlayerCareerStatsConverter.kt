package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.player.PlayerCareer

class PlayerCareerStatsConverter {
    @TypeConverter
    fun from(value: PlayerCareer.PlayerCareerStats): String {
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerStats>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): PlayerCareer.PlayerCareerStats {
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerStats>() {}.type
        return gson.fromJson(value, type)
    }
}