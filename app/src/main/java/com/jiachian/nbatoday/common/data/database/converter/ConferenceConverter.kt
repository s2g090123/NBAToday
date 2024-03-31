package com.jiachian.nbatoday.common.data.database.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam

class ConferenceConverter {
    @TypeConverter
    fun from(value: NBATeam.Conference): String {
        return value.name
    }

    @TypeConverter
    fun to(value: String): NBATeam.Conference {
        return when (value) {
            NBATeam.Conference.WEST.name -> NBATeam.Conference.WEST
            NBATeam.Conference.EAST.name -> NBATeam.Conference.EAST
            else -> NBATeam.Conference.WEST
        }
    }
}
