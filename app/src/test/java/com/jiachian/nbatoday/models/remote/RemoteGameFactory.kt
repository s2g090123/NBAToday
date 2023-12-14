package com.jiachian.nbatoday.models.remote

import com.jiachian.nbatoday.AwayPlayerFirstName
import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BasicMinutes
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameDateTime
import com.jiachian.nbatoday.GameDay
import com.jiachian.nbatoday.GameSeason
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.NBALeagueId
import com.jiachian.nbatoday.TeamCity
import com.jiachian.nbatoday.models.GameLeaderFactory
import com.jiachian.nbatoday.models.GameTeamFactory
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus
import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore

object RemoteGameFactory {
    fun getRemoteGameScoreboard(): RemoteGame {
        return RemoteGame(
            scoreboard = RemoteGame.RemoteScoreboard(
                gameDate = GameDate,
                games = listOf(
                    RemoteGame.RemoteScoreboard.RemoteGameDetail(
                        gameId = FinalGameId,
                        gameCode = GameCode,
                        gameStatus = GameStatus.FINAL.code,
                        gameStatusText = GameStatusFinal,
                        gameTime = GameDate,
                        gameLeaders = GameLeaderFactory.getGameLeaders(),
                        teamLeaders = GameLeaderFactory.getGameLeaders(),
                        homeTeam = GameTeamFactory.getDefaultRemoteHomeTeam(),
                        awayTeam = GameTeamFactory.getDefaultRemoteAwayTeam()
                    )
                )
            )
        )
    }

    fun getRemoteSchedule(): RemoteSchedule {
        return RemoteSchedule(
            RemoteSchedule.RemoteLeagueSchedule(
                gameDates = listOf(
                    RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate(
                        gameDate = GameDate,
                        games = listOf(
                            RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame(
                                awayTeam = RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemoteTeam(
                                    losses = BasicNumber,
                                    score = BasicNumber,
                                    teamCity = TeamCity,
                                    teamId = AwayTeamId,
                                    teamName = AwayTeamName,
                                    teamTricode = AwayTeamAbbr,
                                    wins = BasicNumber
                                ),
                                day = GameDay,
                                gameCode = GameCode,
                                gameId = FinalGameId,
                                gameStatus = GameStatus.FINAL,
                                gameStatusText = GameStatusFinal,
                                gameSequence = BasicNumber,
                                homeTeam = RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.HomeTeam(
                                    losses = BasicNumber,
                                    score = BasicNumber,
                                    teamCity = TeamCity,
                                    teamId = HomeTeamId,
                                    teamName = HomeTeamName,
                                    teamTricode = HomeTeamAbbr,
                                    wins = BasicNumber
                                ),
                                gameDateEst = GameDateTime,
                                gameDateTimeEst = GameDateTime,
                                monthNum = BasicNumber,
                                pointsLeaders = listOf(
                                    RemoteSchedule.RemoteLeagueSchedule.RemoteGameDate.RemoteGame.RemotePointsLeader(
                                        firstName = HomePlayerFirstName,
                                        lastName = HomePlayerLastName,
                                        playerId = HomePlayerId,
                                        points = BasicNumber.toDouble(),
                                        teamId = HomeTeamId,
                                        teamName = HomeTeamName,
                                        teamTricode = HomeTeamAbbr
                                    )
                                ),
                                weekNumber = BasicNumber
                            )
                        )
                    )
                ),
                leagueId = NBALeagueId,
                seasonYear = GameSeason
            )
        )
    }

    fun getRemoteGameBoxScore(): RemoteBoxScore {
        return RemoteBoxScore(
            game = RemoteBoxScore.RemoteGame(
                gameId = FinalGameId,
                gameEt = GameDate,
                gameCode = GameCode,
                gameStatusText = GameStatusFinal,
                gameStatus = GameStatus.FINAL,
                homeTeam = RemoteBoxScore.RemoteGame.RemoteTeam(
                    teamId = HomeTeamId,
                    teamName = HomeTeamName,
                    teamCity = TeamCity,
                    teamTricode = HomeTeamAbbr,
                    score = BasicNumber,
                    inBonus = "1",
                    timeoutsRemaining = BasicNumber,
                    periods = listOf(
                        RemoteBoxScore.RemoteGame.RemoteTeam.RemotePeriod(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BasicNumber
                        )
                    ),
                    players = listOf(
                        RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BasicNumber,
                            playerId = HomePlayerId,
                            jerseyNum = BasicNumber.toString(),
                            position = BasicPosition,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics(
                                assists = BasicNumber,
                                blocks = BasicNumber,
                                blocksReceived = BasicNumber,
                                fieldGoalsAttempted = BasicNumber,
                                fieldGoalsMade = BasicNumber,
                                fieldGoalsPercentage = BasicPercentage,
                                foulsOffensive = BasicNumber,
                                foulsDrawn = BasicNumber,
                                foulsPersonal = BasicNumber,
                                foulsTechnical = BasicNumber,
                                freeThrowsAttempted = BasicNumber,
                                freeThrowsMade = BasicNumber,
                                freeThrowsPercentage = BasicPercentage,
                                minus = BasicNumber.toDouble(),
                                minutes = BasicMinutes,
                                plus = BasicNumber.toDouble(),
                                plusMinusPoints = BasicNumber.toDouble(),
                                points = BasicNumber,
                                reboundsOffensive = BasicNumber,
                                reboundsDefensive = BasicNumber,
                                reboundsTotal = BasicNumber,
                                steals = BasicNumber,
                                threePointersAttempted = BasicNumber,
                                threePointersMade = BasicNumber,
                                threePointersPercentage = BasicPercentage,
                                turnovers = BasicNumber,
                                twoPointersAttempted = BasicNumber,
                                twoPointersMade = BasicNumber,
                                twoPointersPercentage = BasicPercentage
                            ),
                            name = HomePlayerFullName,
                            nameAbbr = HomePlayerFullName,
                            firstName = HomePlayerFirstName,
                            familyName = HomePlayerLastName
                        )
                    ),
                    statistics = RemoteBoxScore.RemoteGame.RemoteTeam.RemoteStatistics(
                        assists = BasicNumber,
                        blocks = BasicNumber,
                        blocksReceived = BasicNumber,
                        fieldGoalsAttempted = BasicNumber,
                        fieldGoalsMade = BasicNumber,
                        fieldGoalsPercentage = BasicPercentage,
                        foulsOffensive = BasicNumber,
                        foulsDrawn = BasicNumber,
                        foulsPersonal = BasicNumber,
                        foulsTeam = BasicNumber,
                        foulsTechnical = BasicNumber,
                        freeThrowsAttempted = BasicNumber,
                        freeThrowsMade = BasicNumber,
                        freeThrowsPercentage = BasicPercentage,
                        points = BasicNumber,
                        reboundsDefensive = BasicNumber,
                        reboundsOffensive = BasicNumber,
                        reboundsPersonal = BasicNumber,
                        reboundsTotal = BasicNumber,
                        steals = BasicNumber,
                        threePointersAttempted = BasicNumber,
                        threePointersMade = BasicNumber,
                        threePointersPercentage = BasicPercentage,
                        turnovers = BasicNumber,
                        turnoversTeam = BasicNumber,
                        turnoversTotal = BasicNumber,
                        twoPointersAttempted = BasicNumber,
                        twoPointersMade = BasicNumber,
                        twoPointersPercentage = BasicPercentage,
                        pointsFastBreak = BasicNumber,
                        pointsFromTurnovers = BasicNumber,
                        pointsInThePaint = BasicNumber,
                        pointsSecondChance = BasicNumber,
                        benchPoints = BasicNumber
                    )
                ),
                awayTeam = RemoteBoxScore.RemoteGame.RemoteTeam(
                    teamId = AwayTeamId,
                    teamName = AwayTeamName,
                    teamCity = TeamCity,
                    teamTricode = AwayTeamAbbr,
                    score = BasicNumber,
                    inBonus = "1",
                    timeoutsRemaining = BasicNumber,
                    periods = listOf(
                        RemoteBoxScore.RemoteGame.RemoteTeam.RemotePeriod(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BasicNumber
                        )
                    ),
                    players = listOf(
                        RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BasicNumber,
                            playerId = AwayPlayerId,
                            jerseyNum = BasicNumber.toString(),
                            position = BasicPosition,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteBoxScore.RemoteGame.RemoteTeam.RemotePlayer.RemoteStatistics(
                                assists = BasicNumber,
                                blocks = BasicNumber,
                                blocksReceived = BasicNumber,
                                fieldGoalsAttempted = BasicNumber,
                                fieldGoalsMade = BasicNumber,
                                fieldGoalsPercentage = BasicPercentage,
                                foulsOffensive = BasicNumber,
                                foulsDrawn = BasicNumber,
                                foulsPersonal = BasicNumber,
                                foulsTechnical = BasicNumber,
                                freeThrowsAttempted = BasicNumber,
                                freeThrowsMade = BasicNumber,
                                freeThrowsPercentage = BasicPercentage,
                                minus = BasicNumber.toDouble(),
                                minutes = BasicMinutes,
                                plus = BasicNumber.toDouble(),
                                plusMinusPoints = BasicNumber.toDouble(),
                                points = BasicNumber,
                                reboundsOffensive = BasicNumber,
                                reboundsDefensive = BasicNumber,
                                reboundsTotal = BasicNumber,
                                steals = BasicNumber,
                                threePointersAttempted = BasicNumber,
                                threePointersMade = BasicNumber,
                                threePointersPercentage = BasicPercentage,
                                turnovers = BasicNumber,
                                twoPointersAttempted = BasicNumber,
                                twoPointersMade = BasicNumber,
                                twoPointersPercentage = BasicPercentage
                            ),
                            name = AwayPlayerFullName,
                            nameAbbr = AwayPlayerFullName,
                            firstName = AwayPlayerFirstName,
                            familyName = AwayPlayerLastName
                        )
                    ),
                    statistics = RemoteBoxScore.RemoteGame.RemoteTeam.RemoteStatistics(
                        assists = BasicNumber,
                        blocks = BasicNumber,
                        blocksReceived = BasicNumber,
                        fieldGoalsAttempted = BasicNumber,
                        fieldGoalsMade = BasicNumber,
                        fieldGoalsPercentage = BasicPercentage,
                        foulsOffensive = BasicNumber,
                        foulsDrawn = BasicNumber,
                        foulsPersonal = BasicNumber,
                        foulsTeam = BasicNumber,
                        foulsTechnical = BasicNumber,
                        freeThrowsAttempted = BasicNumber,
                        freeThrowsMade = BasicNumber,
                        freeThrowsPercentage = BasicPercentage,
                        points = BasicNumber,
                        reboundsDefensive = BasicNumber,
                        reboundsOffensive = BasicNumber,
                        reboundsPersonal = BasicNumber,
                        reboundsTotal = BasicNumber,
                        steals = BasicNumber,
                        threePointersAttempted = BasicNumber,
                        threePointersMade = BasicNumber,
                        threePointersPercentage = BasicPercentage,
                        turnovers = BasicNumber,
                        turnoversTeam = BasicNumber,
                        turnoversTotal = BasicNumber,
                        twoPointersAttempted = BasicNumber,
                        twoPointersMade = BasicNumber,
                        twoPointersPercentage = BasicPercentage,
                        pointsFastBreak = BasicNumber,
                        pointsFromTurnovers = BasicNumber,
                        pointsInThePaint = BasicNumber,
                        pointsSecondChance = BasicNumber,
                        benchPoints = BasicNumber
                    )
                )
            )
        )
    }
}
