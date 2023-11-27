package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.NbaGame

class PointsLeaderConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type

    @TypeConverter
    fun from(value: List<NbaGame.NbaPointsLeader>): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): List<NbaGame.NbaPointsLeader> {
        return gson.fromJson(value, type)
    }
}
