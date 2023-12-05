package com.jiachian.nbatoday.models.remote.game

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.models.local.game.GameStatus

data class RemoteSchedule(
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
                @SerializedName("gameId") val gameId: String?, // 比賽id, e.g. 0022200089
                @SerializedName("gameStatus") val gameStatus: GameStatus?,
                @SerializedName("gameStatusText") val gameStatusText: String?,
                @SerializedName("gameSequence") val gameSequence: Int?, // 今天的第幾場比賽(起始為1), e.g. 1
                @SerializedName("homeTeam") val homeTeam: RemoteTeam?,
                @ColumnInfo(name = "gameDateEst") val gameDateEst: String?,
                @SerializedName("gameDateTimeEst") val gameDateTimeEst: String?, // 比賽開始時間, e.g. 2022-10-30T12:00:00Z
                @SerializedName("pointsLeaders") val pointsLeaders: List<RemotePointsLeader?>?,
            ) {
                data class RemoteTeam(
                    @SerializedName("losses") val losses: Int?, // 敗場場次(從這場之前), e.g. 2
                    @SerializedName("score") val score: Int?, // 比分, e.g. 100
                    @SerializedName("teamId") val teamId: Int?, // 隊伍id, e.g. 1610612746
                    @SerializedName("wins") val wins: Int? // 勝場場次(從這場之前), e.g. 2
                )

                data class RemotePointsLeader(
                    @SerializedName("personId") val playerId: Int?, // 球員id, e.g. 1628983
                    @SerializedName("points") val points: Double?, // 球員當場得分, e.g. 38.0
                    @SerializedName("teamId") val teamId: Int?, // 球員所屬球隊id, e.g. 1610612760
                )
            }
        }
    }
}
