package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.player.PlayerCareer

class PlayerCareerInfoConverter {
    @TypeConverter
    fun from(value: PlayerCareer.PlayerCareerInfo): String {
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): PlayerCareer.PlayerCareerInfo {
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        return gson.fromJson(value, type)
    }
}