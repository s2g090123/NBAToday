package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.remote.leader.GameLeaders

class GameLeadersConverter {
    @TypeConverter
    fun from(value: GameLeaders?): String {
        val gson = Gson()
        val type = object : TypeToken<GameLeaders>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameLeaders? {
        val gson = Gson()
        val type = object : TypeToken<GameLeaders>() {}.type
        return gson.fromJson(value, type)
    }
}