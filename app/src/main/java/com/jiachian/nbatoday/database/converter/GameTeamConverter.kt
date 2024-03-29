package com.jiachian.nbatoday.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.models.local.game.GameTeam

class GameTeamConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<GameTeam>() {}.type

    @TypeConverter
    fun from(value: GameTeam): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameTeam {
        return gson.fromJson(value, type)
    }
}
