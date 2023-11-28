package com.jiachian.nbatoday.models.remote.game

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.models.local.game.GameStatus

data class RemoteGame(
    @SerializedName("scoreboard") val scoreboard: RemoteScoreboard?,
) {
    data class RemoteScoreboard(
        @SerializedName("games") val games: List<RemoteGameDetail>?
    ) {
        data class RemoteGameDetail(
            @SerializedName("gameId") val gameId: String?, // e.g. 0022200414
            @SerializedName("gameStatus") val gameStatus: Int?, // e.g. 1 or 3
            @SerializedName("gameStatusText") val gameStatusText: String?, // e.g. 7:00 pm ET or Final
            @SerializedName("gameLeaders") val gameLeaders: GameLeaders?,
            @SerializedName("teamLeaders") val teamLeaders: GameLeaders?,
            @SerializedName("homeTeam") val homeTeam: RemoteGameTeam?,
            @SerializedName("awayTeam") val awayTeam: RemoteGameTeam?
        ) {
            data class RemoteGameTeam(
                @SerializedName("teamId") val teamId: Int?,
                @SerializedName("wins") val wins: Int?,
                @SerializedName("losses") val losses: Int?,
                @SerializedName("score") val score: Int?,
            )

            fun getFormattedGameStatus(): GameStatus? {
                return when (gameStatus) {
                    GameStatus.COMING_SOON.code -> GameStatus.COMING_SOON
                    GameStatus.PLAYING.code -> GameStatus.PLAYING
                    GameStatus.FINAL.code -> GameStatus.FINAL
                    else -> null
                }
            }
        }
    }
}
