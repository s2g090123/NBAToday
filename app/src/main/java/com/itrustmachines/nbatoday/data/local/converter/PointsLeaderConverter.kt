package com.itrustmachines.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itrustmachines.nbatoday.data.local.NbaGame

class PointsLeaderConverter {
    @TypeConverter
    fun fromPointsLeader(value: List<NbaGame.NbaPointsLeader>): String {
        val gson = Gson()
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toPointsLeader(value: String): List<NbaGame.NbaPointsLeader> {
        val gson = Gson()
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        return gson.fromJson(value, type)
    }
}