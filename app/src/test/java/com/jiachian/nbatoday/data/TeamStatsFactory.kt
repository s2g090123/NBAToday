package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_TEAM
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.BASIC_PERCENTAGE
import com.jiachian.nbatoday.HOME_TEAM
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats

object TeamStatsFactory {

    fun getHomeTeamStats(): TeamStats {
        return TeamStats(
            teamId = HOME_TEAM_ID,
            team = HOME_TEAM,
            teamConference = NBATeam.Conference.EAST,
            gamePlayed = BASIC_NUMBER,
            win = BASIC_NUMBER,
            lose = BASIC_NUMBER,
            winPercentage = BASIC_PERCENTAGE,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            threePointersMade = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            reboundsDefensive = BASIC_NUMBER,
            reboundsOffensive = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            assists = BASIC_NUMBER,
            turnovers = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            points = BASIC_NUMBER,
            plusMinus = BASIC_NUMBER
        )
    }

    fun getAwayTeamStats(): TeamStats {
        return TeamStats(
            teamId = AWAY_TEAM_ID,
            team = AWAY_TEAM,
            teamConference = NBATeam.Conference.WEST,
            gamePlayed = BASIC_NUMBER,
            win = BASIC_NUMBER,
            lose = BASIC_NUMBER,
            winPercentage = BASIC_PERCENTAGE,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            threePointersMade = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            reboundsDefensive = BASIC_NUMBER,
            reboundsOffensive = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            assists = BASIC_NUMBER,
            turnovers = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            points = BASIC_NUMBER,
            plusMinus = BASIC_NUMBER
        )
    }
}
