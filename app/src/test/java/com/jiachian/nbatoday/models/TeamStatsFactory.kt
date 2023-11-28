package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team

object TeamStatsFactory {

    fun getHomeTeamStats(): Team {
        return Team(
            teamId = HomeTeamId,
            team = HomeTeam,
            teamConference = NBATeam.Conference.EAST,
            gamePlayed = BasicNumber,
            win = BasicNumber,
            lose = BasicNumber,
            winPercentage = BasicPercentage,
            fieldGoalsMade = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            threePointersMade = BasicNumber,
            threePointersAttempted = BasicNumber,
            threePointersPercentage = BasicPercentage,
            freeThrowsMade = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            reboundsDefensive = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsTotal = BasicNumber,
            assists = BasicNumber,
            turnovers = BasicNumber,
            steals = BasicNumber,
            blocks = BasicNumber,
            foulsPersonal = BasicNumber,
            points = BasicNumber,
            plusMinus = BasicNumber
        )
    }

    fun getAwayTeamStats(): Team {
        return Team(
            teamId = AwayTeamId,
            team = AwayTeam,
            teamConference = NBATeam.Conference.WEST,
            gamePlayed = BasicNumber,
            win = BasicNumber,
            lose = BasicNumber,
            winPercentage = BasicPercentage,
            fieldGoalsMade = BasicNumber,
            fieldGoalsAttempted = BasicNumber,
            fieldGoalsPercentage = BasicPercentage,
            threePointersMade = BasicNumber,
            threePointersAttempted = BasicNumber,
            threePointersPercentage = BasicPercentage,
            freeThrowsMade = BasicNumber,
            freeThrowsAttempted = BasicNumber,
            freeThrowsPercentage = BasicPercentage,
            reboundsDefensive = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsTotal = BasicNumber,
            assists = BasicNumber,
            turnovers = BasicNumber,
            steals = BasicNumber,
            blocks = BasicNumber,
            foulsPersonal = BasicNumber,
            points = BasicNumber,
            plusMinus = BasicNumber
        )
    }
}
