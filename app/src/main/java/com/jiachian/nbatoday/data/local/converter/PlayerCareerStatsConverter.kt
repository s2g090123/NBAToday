package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.player.PlayerCareer

class PlayerCareerStatsConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<PlayerCareer.PlayerCareerStats>() {}.type

    @TypeConverter
    fun from(value: PlayerCareer.PlayerCareerStats): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): PlayerCareer.PlayerCareerStats {
        return gson.fromJson(value, type)
    }
}
