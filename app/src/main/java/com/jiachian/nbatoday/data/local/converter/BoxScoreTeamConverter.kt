package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.score.GameBoxScore

class BoxScoreTeamConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<GameBoxScore.BoxScoreTeam>() {}.type

    @TypeConverter
    fun from(value: GameBoxScore.BoxScoreTeam): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameBoxScore.BoxScoreTeam {
        return gson.fromJson(value, type)
    }
}
