package com.jiachian.nbatoday.game.data.model.remote

import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.game.data.model.local.GameStatus

data class ScheduleDto(
    @SerializedName("leagueSchedule") val leagueSchedule: RemoteLeagueSchedule?
) {
    data class RemoteLeagueSchedule(
        @SerializedName("gameDates") val gameDates: List<RemoteGameDate>?,
    ) {
        data class RemoteGameDate(
            @SerializedName("games") val games: List<RemoteGame>?
        ) {
            data class RemoteGame(
                @SerializedName("awayTeam") val awayTeam: RemoteTeam?,
                @SerializedName("gameId") val gameId: String?, // e.g. 0022200089
                @SerializedName("gameStatus") val gameStatus: GameStatus?,
                @SerializedName("gameStatusText") val gameStatusText: String?,
                @SerializedName("gameSequence") val gameSequence: Int?, // starting from 1
                @SerializedName("homeTeam") val homeTeam: RemoteTeam?,
                @SerializedName("gameDateEst") val gameDateEst: String?,
                @SerializedName("gameDateTimeEst") val gameDateTimeEst: String?, // e.g. 2022-10-30T12:00:00Z
                @SerializedName("pointsLeaders") val pointsLeaders: List<RemotePointsLeader?>?,
            ) {
                data class RemoteTeam(
                    @SerializedName("losses") val losses: Int?, // Team's losses (prior to this game)
                    @SerializedName("score") val score: Int?,
                    @SerializedName("teamId") val teamId: Int?, // e.g. 1610612746
                    @SerializedName("wins") val wins: Int? // Team's wins (prior to this game)
                )

                data class RemotePointsLeader(
                    @SerializedName("personId") val playerId: Int?, // e.g. 1628983
                    @SerializedName("points") val points: Double?, // e.g. 38.0
                    @SerializedName("teamId") val teamId: Int?, // e.g. 1610612760
                )
            }
        }
    }
}
