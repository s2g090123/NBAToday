package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.team.TeamPlayerStats

object PlayerStatsFactory {

    fun getHomePlayerStats(): TeamPlayerStats {
        return TeamPlayerStats(
            playerId = HomePlayerId,
            teamId = HomeTeamId,
            playerName = HomePlayerFullName,
            playerNickName = HomePlayerLastName,
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

    fun getAwayPlayerStats(): TeamPlayerStats {
        return TeamPlayerStats(
            playerId = AwayPlayerId,
            teamId = AwayTeamId,
            playerName = AwayPlayerFullName,
            playerNickName = AwayPlayerLastName,
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
