package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "game")
data class Game(
    @ColumnInfo(name = "home_team_id")
    val homeTeamId: Int,
    @ColumnInfo(name = "away_team_id")
    val awayTeamId: Int,
    @ColumnInfo(name = "away_team")
    val awayTeam: GameTeam,
    @PrimaryKey @ColumnInfo(name = "game_id")
    val gameId: String, // e.g. 0022200089
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatus,
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // e.g. Final or 3:00 pm ET
    @ColumnInfo(name = "home_team")
    val homeTeam: GameTeam,
    @ColumnInfo(name = "game_date")
    val gameDate: Date, // Start date of the game (the time is always 13:00), e.g. 2022-11-20T13:00:00Z (GMT+8)
    @ColumnInfo(name = "game_date_time")
    val gameDateTime: Date, // Start time of the game, e.g. 2022-11-20T15:30:00Z (GMT+8)
    @ColumnInfo(name = "points_leader")
    val pointsLeaders: List<PointsLeader>, // Leaders in scoring
    @ColumnInfo(name = "game_leaders")
    val gameLeaders: GameLeaders?, // Leaders in this game
    @ColumnInfo(name = "team_leaders")
    val teamLeaders: GameLeaders? // Leaders in the team
) {
    data class PointsLeader(
        @ColumnInfo(name = "player_id")
        val playerId: Int, // e.g. 1628983
        @ColumnInfo(name = "points")
        val points: Double, // e.g. 38.0
        @ColumnInfo(name = "team_id")
        val teamId: Int, // e.g. 1610612760
    )

    val gameFinal: Boolean
        get() = gameStatus == GameStatus.FINAL

    val gamePlayed: Boolean
        get() = gameStatus != GameStatus.COMING_SOON

    val homeWin: Boolean
        get() = homeTeam.score > awayTeam.score && gameFinal

    val awayWin: Boolean
        get() = homeTeam.score <= awayTeam.score && gameFinal

    val statusFormattedText: String
        get() = gameStatusText.replaceFirst(" ", "\n").trim()

    val homeLeaderId: Int
        get() {
            return when {
                gamePlayed -> gameLeaders?.homeLeader?.playerId
                else -> teamLeaders?.homeLeader?.playerId
            } ?: pointsLeaders.firstOrNull {
                it.teamId == homeTeam.team.teamId
            }?.playerId ?: 0
        }

    val awayLeaderId: Int
        get() {
            return when {
                gamePlayed -> gameLeaders?.awayLeader?.playerId
                else -> teamLeaders?.awayLeader?.playerId
            } ?: pointsLeaders.firstOrNull {
                it.teamId == awayTeam.team.teamId
            }?.playerId ?: 0
        }
}
