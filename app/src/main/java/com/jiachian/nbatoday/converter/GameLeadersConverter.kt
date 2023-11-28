package com.jiachian.nbatoday.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.models.local.game.GameLeaders

class GameLeadersConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<GameLeaders>() {}.type

    @TypeConverter
    fun from(value: GameLeaders?): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameLeaders? {
        return gson.fromJson(value, type)
    }
}
