package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam
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
            val homeTeam: GameTeam?,
            @SerializedName("awayTeam")
            val awayTeam: GameTeam?
        )
    }

    fun toGameUpdateData(): List<GameUpdateData> {
        val games = scoreboard?.games ?: return emptyList()
        return games.mapNotNull { game ->
            val gameId = game.gameId
            val gameStatus = game.gameStatus?.toGameStatusCode()
            val gameStatusText = game.gameStatusText
            val homeTeam = game.homeTeam
            val awayTeam = game.awayTeam
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
