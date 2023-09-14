package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jiachian.nbatoday.data.local.team.NBATeam

private val nbaTeamGson = GsonBuilder()
    .registerTypeAdapter(NBATeam::class.java, NBATeamTypeAdapter())
    .create()

class NBATeamConverter(private val gson: Gson = nbaTeamGson) {

    @TypeConverter
    fun from(value: NBATeam): String {
        return gson.toJson(value, NBATeam::class.java)
    }

    @TypeConverter
    fun to(value: String): NBATeam {
        return gson.fromJson(value, NBATeam::class.java)
    }
}
