package com.jiachian.nbatoday.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.models.local.score.BoxScore

class BoxScoreTeamConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<BoxScore.BoxScoreTeam>() {}.type

    @TypeConverter
    fun from(value: BoxScore.BoxScoreTeam): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): BoxScore.BoxScoreTeam {
        return gson.fromJson(value, type)
    }
}
