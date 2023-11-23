package com.jiachian.nbatoday.data.local.team

import com.jiachian.nbatoday.compose.theme.NbaColors

interface NBATeam {

    companion object {

        val nbaTeams = listOf(
            teamHawks, teamCeltics, teamNets, teamHornets, teamBulls,
            teamCavaliers, teamMavericks, teamNuggets, teamPistons, teamWarriors,
            teamRockets, teamPacers, teamClippers, teamLakers, teamGrizzlies,
            teamHeat, teamBucks, teamTimberwolves, teamPelicans, teamKnicks,
            teamThunder, teamMagic, team76ers, teamSuns, teamBlazers,
            teamKings, teamSpurs, teamRaptors, teamJazz, teamWizards
        )

        fun getTeamById(teamId: Int): NBATeam? {
            return nbaTeams.firstOrNull { it.teamId == teamId }
        }
    }

    enum class Conference {
        EAST, WEST;

        override fun toString(): String {
            return when (this) {
                EAST -> "Eastern"
                WEST -> "Western"
            }
        }
    }

    enum class Division {
        ATLANTIC, CENTRAL, SOUTHEAST,
        NORTHWEST, PACIFIC, SOUTHWEST
    }

    val teamId: Int // e.g. 1610612747
    val abbreviation: String // e.g. LAL
    val teamName: String // e.g. Lakers
    val location: String // e.g. Los Angeles
    val logoRes: Int // Team logo drawable resource
    val conference: Conference
    val division: Division
    val colors: NbaColors
    val teamFullName: String // e.g. Los Angeles Lakers
        get() = "$location $teamName"
}
