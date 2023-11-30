package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeam
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameSeason
import com.jiachian.nbatoday.GameSeasonNext
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.TeamCity
import com.jiachian.nbatoday.models.local.player.Player

object PlayerCareerFactory {

    fun createHomePlayerCareer(): Player {
        return Player(
            playerId = HomePlayerId,
            info = createHomePlayerInfo(),
            stats = createHomePlayerStats()
        )
    }

    fun createAwayPlayerCareer(): Player {
        return Player(
            playerId = AwayPlayerId,
            info = createAwayPlayerInfo(),
            stats = createAwayPlayerStats()
        )
    }

    private fun createHomePlayerInfo(): Player.PlayerInfo {
        return Player.PlayerInfo(
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
            team = HomeTeam,
            fromYear = BasicNumber,
            toYear = BasicNumber,
            draftYear = BasicNumber,
            draftRound = BasicNumber,
            draftNumber = BasicNumber,
            isGreatest75 = true,
            headlineStats = createHeadlineStats()
        )
    }

    private fun createAwayPlayerInfo(): Player.PlayerInfo {
        return Player.PlayerInfo(
            playerName = AwayPlayerFullName,
            playerNameAbbr = AwayPlayerLastName,
            playerAge = BasicNumber,
            birthDate = GameDate,
            country = TeamCity,
            school = TeamCity,
            height = BasicNumber.toDouble(),
            weight = BasicNumber.toDouble(),
            seasonExperience = BasicNumber,
            jersey = BasicNumber,
            position = BasicPosition,
            team = AwayTeam,
            fromYear = BasicNumber,
            toYear = BasicNumber,
            draftYear = BasicNumber,
            draftRound = BasicNumber,
            draftNumber = BasicNumber,
            isGreatest75 = true,
            headlineStats = createHeadlineStats()
        )
    }

    private fun createHeadlineStats(): Player.PlayerInfo.HeadlineStats {
        return Player.PlayerInfo.HeadlineStats(
            timeFrame = GameSeason,
            points = BasicNumber.toDouble(),
            assists = BasicNumber.toDouble(),
            rebounds = BasicNumber.toDouble(),
            impact = BasicPercentage
        )
    }

    private fun createHomePlayerStats(): Player.PlayerStats {
        return Player.PlayerStats(
            stats = createHomeStats(),
            careerRank = createHomeRanks()
        )
    }

    private fun createAwayPlayerStats(): Player.PlayerStats {
        return Player.PlayerStats(
            stats = createAwayStats(),
            careerRank = createAwayRanks()
        )
    }

    private fun createHomeStats(): ArrayList<Player.PlayerStats.Stats> {
        return arrayListOf(
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

    private fun createAwayStats(): ArrayList<Player.PlayerStats.Stats> {
        return arrayListOf(
            Player.PlayerStats.Stats(
                timeFrame = GameSeason,
                teamId = AwayTeamId,
                teamNameAbbr = AwayTeamAbbr,
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

    private fun createHomeRanks(): ArrayList<Player.PlayerStats.Rank> {
        return arrayListOf(
            Player.PlayerStats.Rank(
                timeFrame = GameSeason,
                teamId = HomeTeamId,
                teamNameAbbr = HomeTeamAbbr,
                gamePlayedRank = BasicNumber,
                winRank = BasicNumber,
                loseRank = BasicNumber,
                winPercentageRank = BasicNumber,
                fieldGoalsMadeRank = BasicNumber,
                fieldGoalsAttemptedRank = BasicNumber,
                fieldGoalsPercentageRank = BasicNumber,
                threePointersMadeRank = BasicNumber,
                threePointersAttemptedRank = BasicNumber,
                threePointersPercentageRank = BasicNumber,
                freeThrowsMadeRank = BasicNumber,
                freeThrowsAttemptedRank = BasicNumber,
                freeThrowsPercentageRank = BasicNumber,
                reboundsOffensiveRank = BasicNumber,
                reboundsDefensiveRank = BasicNumber,
                reboundsTotalRank = BasicNumber,
                assistsRank = BasicNumber,
                turnoversRank = BasicNumber,
                stealsRank = BasicNumber,
                blocksRank = BasicNumber,
                foulsPersonalRank = BasicNumber,
                pointsRank = BasicNumber,
                plusMinusRank = BasicNumber
            )
        )
    }

    private fun createAwayRanks(): ArrayList<Player.PlayerStats.Rank> {
        return arrayListOf(
            Player.PlayerStats.Rank(
                timeFrame = GameSeason,
                teamId = AwayTeamId,
                teamNameAbbr = AwayTeamAbbr,
                gamePlayedRank = BasicNumber,
                winRank = BasicNumber,
                loseRank = BasicNumber,
                winPercentageRank = BasicNumber,
                fieldGoalsMadeRank = BasicNumber,
                fieldGoalsAttemptedRank = BasicNumber,
                fieldGoalsPercentageRank = BasicNumber,
                threePointersMadeRank = BasicNumber,
                threePointersAttemptedRank = BasicNumber,
                threePointersPercentageRank = BasicNumber,
                freeThrowsMadeRank = BasicNumber,
                freeThrowsAttemptedRank = BasicNumber,
                freeThrowsPercentageRank = BasicNumber,
                reboundsOffensiveRank = BasicNumber,
                reboundsDefensiveRank = BasicNumber,
                reboundsTotalRank = BasicNumber,
                assistsRank = BasicNumber,
                turnoversRank = BasicNumber,
                stealsRank = BasicNumber,
                blocksRank = BasicNumber,
                foulsPersonalRank = BasicNumber,
                pointsRank = BasicNumber,
                plusMinusRank = BasicNumber
            )
        )
    }
}
