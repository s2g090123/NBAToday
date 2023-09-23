package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import com.jiachian.nbatoday.utils.getValueOrAssert
import com.jiachian.nbatoday.utils.isNull
import com.jiachian.nbatoday.utils.toGameStatusCode

data class GameScoreboard(
    @SerializedName("scoreboard")
    val scoreboard: Scoreboard?,
) {
    data class Scoreboard(
        @SerializedName("gameDate")
        val gameDate: String?, // e.g. 2022-12-14
        @SerializedName("games")
        val games: List<Game>?
    ) {
        data class Game(
            @SerializedName("gameId")
            val gameId: String?, // e.g. 0022200414
            @SerializedName("gameCode")
            val gameCode: String?, // e.g. 20221214/DETCHA
            @SerializedName("gameStatus")
            val gameStatus: Int?, // e.g. 1 or 3
            @SerializedName("gameStatusText")
            val gameStatusText: String?, // e.g. 7:00 pm ET or Final
            @SerializedName("gameTimeUTC")
            val gameTime: String, // e.g. 2022-12-15T00:00:00Z
            @SerializedName("gameLeaders")
            val gameLeaders: GameLeaders?,
            @SerializedName("teamLeaders")
            val teamLeaders: GameLeaders?,
            @SerializedName("homeTeam")
            val homeTeam: RemoteGameTeam?,
            @SerializedName("awayTeam")
            val awayTeam: RemoteGameTeam?
        ) {
            data class RemoteGameTeam(
                @SerializedName("teamId")
                val teamId: Int?,
                @SerializedName("wins")
                val wins: Int?,
                @SerializedName("losses")
                val losses: Int?,
                @SerializedName("score")
                val score: Int?,
                @SerializedName("periods")
                val periods: List<RemotePeriod>
            ) {
                data class RemotePeriod(
                    @SerializedName("period")
                    val period: Int?, // 第幾節, e.g. 1
                    @SerializedName("periodType")
                    val periodType: String?, // e.g. REGULAR or OVERTIME
                    @SerializedName("score")
                    val score: Int? // 得分, e.g 20
                ) {
                    fun toLocal(): GameTeam.Period {
                        return GameTeam.Period(
                            period = period.getOrZero(),
                            periodType = periodType.getOrNA(),
                            score = score.getOrZero()
                        )
                    }
                }

                fun toLocal(): GameTeam {
                    val team = teamId?.let { NBATeam.getTeamById(it) } ?: teamOfficial
                    return GameTeam(
                        team = team,
                        losses = losses.getOrZero(),
                        score = score.getOrZero(),
                        wins = wins.getOrZero(),
                        periods = periods.map { it.toLocal() }
                    )
                }
            }
        }
    }

    fun toGameUpdateData(): List<GameUpdateData> {
        val games = scoreboard?.games ?: return emptyList()
        return games.mapNotNull { game ->
            val gameId = game.gameId
            val gameStatus = game.gameStatus?.toGameStatusCode()
            val gameStatusText = game.gameStatusText
            val homeTeam = game.homeTeam?.toLocal()
            val awayTeam = game.awayTeam?.toLocal()
            val gameHomeLeaders = game.gameLeaders?.homeLeaders
            val gameAwayLeaders = game.gameLeaders?.awayLeaders
            val teamHomeLeaders = game.teamLeaders?.homeLeaders
            val teamAwayLeaders = game.teamLeaders?.awayLeaders
            val isGameNull = gameId.isNull() || gameStatus.isNull() || gameStatusText.isNull()
            val isTeamNull = homeTeam.isNull() || awayTeam.isNull()
            val isLeadersNull = gameHomeLeaders.isNull() || gameAwayLeaders.isNull() || teamHomeLeaders.isNull() || teamAwayLeaders.isNull()
            if (isGameNull || isTeamNull || isLeadersNull) {
                null
            } else {
                GameUpdateData(
                    gameId = gameId.getValueOrAssert(),
                    gameStatus = gameStatus.getValueOrAssert(),
                    gameStatusText = gameStatusText.getValueOrAssert(),
                    homeTeam = homeTeam.getValueOrAssert(),
                    awayTeam = awayTeam.getValueOrAssert(),
                    gameLeaders = GameLeaders(gameHomeLeaders, gameAwayLeaders),
                    teamLeaders = GameLeaders(teamHomeLeaders, teamAwayLeaders)
                )
            }
        }
    }
}
