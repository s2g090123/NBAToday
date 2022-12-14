package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.remote.leader.GameLeaders

class GameLeaderConverter {
    @TypeConverter
    fun from(value: GameLeaders.GameLeader): String {
        val gson = Gson()
        val type = object : TypeToken<GameLeaders.GameLeader>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameLeaders.GameLeader {
        val gson = Gson()
        val type = object : TypeToken<GameLeaders.GameLeader>() {}.type
        return gson.fromJson(value, type)
    }
}