package com.jiachian.nbatoday.models.local.team

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.team.data.team76ers
import com.jiachian.nbatoday.models.local.team.data.teamBlazers
import com.jiachian.nbatoday.models.local.team.data.teamBucks
import com.jiachian.nbatoday.models.local.team.data.teamBulls
import com.jiachian.nbatoday.models.local.team.data.teamCavaliers
import com.jiachian.nbatoday.models.local.team.data.teamCeltics
import com.jiachian.nbatoday.models.local.team.data.teamClippers
import com.jiachian.nbatoday.models.local.team.data.teamGrizzlies
import com.jiachian.nbatoday.models.local.team.data.teamHawks
import com.jiachian.nbatoday.models.local.team.data.teamHeat
import com.jiachian.nbatoday.models.local.team.data.teamHornets
import com.jiachian.nbatoday.models.local.team.data.teamJazz
import com.jiachian.nbatoday.models.local.team.data.teamKings
import com.jiachian.nbatoday.models.local.team.data.teamKnicks
import com.jiachian.nbatoday.models.local.team.data.teamLakers
import com.jiachian.nbatoday.models.local.team.data.teamMagic
import com.jiachian.nbatoday.models.local.team.data.teamMavericks
import com.jiachian.nbatoday.models.local.team.data.teamNets
import com.jiachian.nbatoday.models.local.team.data.teamNuggets
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.models.local.team.data.teamPacers
import com.jiachian.nbatoday.models.local.team.data.teamPelicans
import com.jiachian.nbatoday.models.local.team.data.teamPistons
import com.jiachian.nbatoday.models.local.team.data.teamRaptors
import com.jiachian.nbatoday.models.local.team.data.teamRockets
import com.jiachian.nbatoday.models.local.team.data.teamSpurs
import com.jiachian.nbatoday.models.local.team.data.teamSuns
import com.jiachian.nbatoday.models.local.team.data.teamThunder
import com.jiachian.nbatoday.models.local.team.data.teamTimberwolves
import com.jiachian.nbatoday.models.local.team.data.teamWarriors
import com.jiachian.nbatoday.models.local.team.data.teamWizards

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
