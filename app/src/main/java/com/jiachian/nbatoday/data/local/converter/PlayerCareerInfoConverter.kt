package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.player.PlayerCareer

class PlayerCareerInfoConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type

    @TypeConverter
    fun from(value: PlayerCareer.PlayerCareerInfo): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): PlayerCareer.PlayerCareerInfo {
        return gson.fromJson(value, type)
    }
}
