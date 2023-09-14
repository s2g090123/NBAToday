package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.data.local.team.NBATeam

class TeamConferenceConverter {
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
