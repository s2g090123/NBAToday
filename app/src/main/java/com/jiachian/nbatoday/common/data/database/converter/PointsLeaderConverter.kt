package com.jiachian.nbatoday.common.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.game.data.model.local.Game

class PointsLeaderConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<List<Game.PointsLeader>>() {}.type

    @TypeConverter
    fun from(value: List<Game.PointsLeader>): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): List<Game.PointsLeader> {
        return gson.fromJson(value, type)
    }
}
