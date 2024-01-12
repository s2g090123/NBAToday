package com.jiachian.nbatoday.models.remote.game

import com.google.gson.annotations.SerializedName
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
            @SerializedName("gameLeaders") val gameLeaders: RemoteGameLeaders?,
            @SerializedName("teamLeaders") val teamLeaders: RemoteGameLeaders?,
            @SerializedName("homeTeam") val homeTeam: RemoteGameTeam?,
            @SerializedName("awayTeam") val awayTeam: RemoteGameTeam?
        ) {
            data class RemoteGameTeam(
                @SerializedName("teamId") val teamId: Int?,
                @SerializedName("wins") val wins: Int?,
                @SerializedName("losses") val losses: Int?,
                @SerializedName("score") val score: Int?,
            )

            data class RemoteGameLeaders(
                @SerializedName("homeLeaders") val homeLeader: RemoteGameLeader,
                @SerializedName("awayLeaders") val awayLeader: RemoteGameLeader
            ) {
                data class RemoteGameLeader(
                    @SerializedName("personId") val playerId: Int, // e.g. 1626179
                    val name: String, // e.g. Terry Rozier
                    val jerseyNum: String, // e.g. 3
                    val position: String, // e.g. G
                    val teamTricode: String, // e.g. CHA
                    val points: Double, // e.g. If the game has finished： 22, otherwise：22.2
                    val rebounds: Double, // e.g. If the game has finished： 22, otherwise：22.2
                    val assists: Double // e.g. If the game has finished： 22, otherwise：22.2
                )
            }

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
