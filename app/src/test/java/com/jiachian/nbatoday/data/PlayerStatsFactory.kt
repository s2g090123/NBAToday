package com.jiachian.nbatoday.data

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
import com.jiachian.nbatoday.data.local.player.PlayerStats

object PlayerStatsFactory {

    fun getHomePlayerStats(): PlayerStats {
        return PlayerStats(
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

    fun getAwayPlayerStats(): PlayerStats {
        return PlayerStats(
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
