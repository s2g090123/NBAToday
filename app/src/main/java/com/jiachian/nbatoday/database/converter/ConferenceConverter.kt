package com.jiachian.nbatoday.database.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.models.local.team.NBATeam

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
