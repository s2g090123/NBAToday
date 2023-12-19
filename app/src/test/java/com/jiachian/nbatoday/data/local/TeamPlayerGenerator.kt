package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.team.TeamPlayer

object TeamPlayerGenerator {
    fun getHome(): TeamPlayer {
        return TeamPlayer(
            playerId = HomePlayerId,
            teamId = HomeTeamId,
            playerName = HomePlayerFullName,
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
            reboundsOffensive = BasicNumber,
            reboundsDefensive = BasicNumber,
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

    fun getAway(): TeamPlayer {
        return TeamPlayer(
            playerId = AwayPlayerId,
            teamId = AwayTeamId,
            playerName = AwayPlayerFullName,
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
            reboundsOffensive = BasicNumber,
            reboundsDefensive = BasicNumber,
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
