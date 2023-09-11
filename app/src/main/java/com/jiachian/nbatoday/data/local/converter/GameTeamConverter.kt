package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.remote.team.GameTeam

class GameTeamConverter {
    @TypeConverter
    fun from(value: GameTeam): String {
        val gson = Gson()
        val type = object : TypeToken<GameTeam>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameTeam {
        val gson = Gson()
        val type = object : TypeToken<GameTeam>() {}.type
        return gson.fromJson(value, type)
    }
}
