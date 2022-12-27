package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.data.local.team.DefaultTeam

class TeamDivisionConverter {
    @TypeConverter
    fun from(value: DefaultTeam.Division): String {
        return value.name
    }

    @TypeConverter
    fun to(value: String): DefaultTeam.Division {
        return when (value) {
            DefaultTeam.Division.PACIFIC.name -> DefaultTeam.Division.PACIFIC
            DefaultTeam.Division.SOUTHEAST.name -> DefaultTeam.Division.SOUTHEAST
            DefaultTeam.Division.NORTHWEST.name -> DefaultTeam.Division.NORTHWEST
            DefaultTeam.Division.CENTRAL.name -> DefaultTeam.Division.CENTRAL
            DefaultTeam.Division.SOUTHWEST.name -> DefaultTeam.Division.SOUTHWEST
            DefaultTeam.Division.ATLANTIC.name -> DefaultTeam.Division.ATLANTIC
            else -> DefaultTeam.Division.PACIFIC
        }
    }
}