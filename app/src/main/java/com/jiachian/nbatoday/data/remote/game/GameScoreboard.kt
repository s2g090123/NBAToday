package com.jiachian.nbatoday.data.remote.game

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam

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
        val scoreboard = scoreboard ?: return emptyList()
        val games = scoreboard.games ?: return emptyList()
        return games.mapNotNull { game ->
            val gameId = game.gameId ?: return@mapNotNull null
            val gameStatus = when (game.gameStatus) {
                GameStatusCode.COMING_SOON.status -> GameStatusCode.COMING_SOON
                GameStatusCode.FINAL.status -> GameStatusCode.FINAL
                else -> null
            } ?: return@mapNotNull null
            val gameStatusText = game.gameStatusText ?: return@mapNotNull null
            val homeTeam = game.homeTeam ?: return@mapNotNull null
            val awayTeam = game.awayTeam ?: return@mapNotNull null
            val gameHomeLeaders = game.gameLeaders?.homeLeaders ?: return@mapNotNull null
            val gameAwayLeaders = game.gameLeaders.awayLeaders ?: return@mapNotNull null
            val teamHomeLeaders = game.teamLeaders?.homeLeaders ?: return@mapNotNull null
            val teamAwayLeaders = game.teamLeaders.awayLeaders ?: return@mapNotNull null
            GameUpdateData(
                gameId, gameStatus, gameStatusText, homeTeam, awayTeam,
                GameLeaders(gameHomeLeaders, gameAwayLeaders),
                GameLeaders(teamHomeLeaders, teamAwayLeaders)
            )
        }
    }
}
