package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.NbaGame

class PointsLeaderConverter(private val gson: Gson = generalGson) {
    @TypeConverter
    fun from(value: List<NbaGame.NbaPointsLeader>): String {
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): List<NbaGame.NbaPointsLeader> {
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        return gson.fromJson(value, type)
    }
}
