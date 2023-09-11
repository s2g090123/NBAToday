package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.score.GameBoxScore

class BoxScoreTeamConverter {
    @TypeConverter
    fun from(value: GameBoxScore.BoxScoreTeam): String {
        val gson = Gson()
        val type = object : TypeToken<GameBoxScore.BoxScoreTeam>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): GameBoxScore.BoxScoreTeam {
        val gson = Gson()
        val type = object : TypeToken<GameBoxScore.BoxScoreTeam>() {}.type
        return gson.fromJson(value, type)
    }
}
