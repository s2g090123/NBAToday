package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.data.local.team.DefaultTeam

class TeamConferenceConverter {
    @TypeConverter
    fun from(value: DefaultTeam.Conference): String {
        return value.name
    }

    @TypeConverter
    fun to(value: String): DefaultTeam.Conference {
        return when (value) {
            DefaultTeam.Conference.WEST.name -> DefaultTeam.Conference.WEST
            DefaultTeam.Conference.EAST.name -> DefaultTeam.Conference.EAST
            else -> DefaultTeam.Conference.WEST
        }
    }
}