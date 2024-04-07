package com.jiachian.nbatoday.team.data.model.local.teams

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.theme.NBAColors

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

        fun getTeamById(teamId: Int?): NBATeam {
            teamId ?: return teamOfficial
            return nbaTeams.firstOrNull { it.teamId == teamId } ?: teamOfficial
        }
    }

    enum class Conference(
        @StringRes val textRes: Int,
    ) {
        EAST(R.string.standing_conference_east),
        WEST(R.string.standing_conference_west);
    }

    val teamId: Int // e.g. 1610612747
    val abbreviation: String // e.g. LAL
    val teamName: String // e.g. Lakers
    val location: String // e.g. Los Angeles
    val logoRes: Int // Team logo drawable resource
    val conference: Conference
    val colors: NBAColors
    val teamFullName: String // e.g. Los Angeles Lakers
        get() = "$location $teamName"
}
