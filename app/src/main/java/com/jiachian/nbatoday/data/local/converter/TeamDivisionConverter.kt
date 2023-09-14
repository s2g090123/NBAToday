package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.data.local.team.NBATeam

class TeamDivisionConverter {
    @TypeConverter
    fun from(value: NBATeam.Division): String {
        return value.name
    }

    @TypeConverter
    fun to(value: String): NBATeam.Division {
        return when (value) {
            NBATeam.Division.PACIFIC.name -> NBATeam.Division.PACIFIC
            NBATeam.Division.SOUTHEAST.name -> NBATeam.Division.SOUTHEAST
            NBATeam.Division.NORTHWEST.name -> NBATeam.Division.NORTHWEST
            NBATeam.Division.CENTRAL.name -> NBATeam.Division.CENTRAL
            NBATeam.Division.SOUTHWEST.name -> NBATeam.Division.SOUTHWEST
            NBATeam.Division.ATLANTIC.name -> NBATeam.Division.ATLANTIC
            else -> NBATeam.Division.PACIFIC
        }
    }
}
