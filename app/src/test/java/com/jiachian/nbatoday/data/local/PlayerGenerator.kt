package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameSeason
import com.jiachian.nbatoday.GameSeasonNext
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.TeamCity
import com.jiachian.nbatoday.models.local.player.Player

object PlayerGenerator {
    fun getHome(): Player {
        return Player(
            playerId = HomePlayerId,
            info = getHomePlayerInfo(),
            stats = getHomePlayerStats(),
        )
    }

    private fun getHomePlayerInfo(): Player.PlayerInfo {
        return Player.PlayerInfo(
            team = NBATeamGenerator.getHome(),
            playerName = HomePlayerFullName,
            playerNameAbbr = HomePlayerLastName,
            playerAge = BasicNumber,
            birthDate = GameDate,
            country = TeamCity,
            school = TeamCity,
            height = BasicNumber.toDouble(),
            weight = BasicNumber.toDouble(),
            seasonExperience = BasicNumber,
            jersey = BasicNumber,
            position = BasicPosition,
            draftYear = BasicNumber,
            draftRound = BasicNumber,
            draftNumber = BasicNumber,
            isGreatest75 = true,
            headlineStats = getHeadlineStats(),
        )
    }

    private fun getHeadlineStats(): Player.PlayerInfo.HeadlineStats {
        return Player.PlayerInfo.HeadlineStats(
            points = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            impact = BasicPercentage
        )
    }

    private fun getHomePlayerStats(): Player.PlayerStats {
        return Player.PlayerStats(
            stats = getHomePlayerSeasonStats()
        )
    }

    private fun getHomePlayerSeasonStats(): List<Player.PlayerStats.Stats> {
        return listOf(
            Player.PlayerStats.Stats(
                timeFrame = GameSeasonNext,
                teamId = HomeTeamId,
                teamNameAbbr = HomeTeamAbbr,
                gamePlayed = BasicNumber - 1,
                win = BasicNumber - 1,
                lose = BasicNumber - 1,
                winPercentage = BasicPercentage - 1,
                fieldGoalsMade = BasicNumber - 1,
                fieldGoalsAttempted = BasicNumber - 1,
                fieldGoalsPercentage = BasicPercentage - 1,
                threePointersMade = BasicNumber - 1,
                threePointersAttempted = BasicNumber - 1,
                threePointersPercentage = BasicPercentage - 1,
                freeThrowsMade = BasicNumber - 1,
                freeThrowsAttempted = BasicNumber - 1,
                freeThrowsPercentage = BasicPercentage - 1,
                reboundsOffensive = BasicNumber - 1,
                reboundsDefensive = BasicNumber - 1,
                reboundsTotal = BasicNumber - 1,
                assists = BasicNumber - 1,
                turnovers = BasicNumber - 1,
                steals = BasicNumber - 1,
                blocks = BasicNumber - 1,
                foulsPersonal = BasicNumber - 1,
                points = BasicNumber - 1,
                plusMinus = BasicNumber - 1
            ),
            Player.PlayerStats.Stats(
                timeFrame = GameSeason,
                teamId = HomeTeamId,
                teamNameAbbr = HomeTeamAbbr,
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
        )
    }
}
