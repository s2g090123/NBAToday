package com.jiachian.nbatoday.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jiachian.nbatoday.models.local.team.NBATeam

class NBATeamConverter(private val gson: Gson = typeAdapterGson) {

    @TypeConverter
    fun from(value: NBATeam): String {
        return gson.toJson(value, NBATeam::class.java)
    }

    @TypeConverter
    fun to(value: String): NBATeam {
        return gson.fromJson(value, NBATeam::class.java)
    }
}
