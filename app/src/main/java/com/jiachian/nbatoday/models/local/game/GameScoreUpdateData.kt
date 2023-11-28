package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo

data class GameScoreUpdateData(
    @ColumnInfo(name = "game_id")
    val gameId: String, // 比賽id, e.g. 0022200089
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatus,
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // 比賽狀態(Final或開始時間), e.g. Final or 3:00 pm ET
    @ColumnInfo(name = "home_team")
    val homeTeam: GameTeam,
    @ColumnInfo(name = "away_team")
    val awayTeam: GameTeam,
    @ColumnInfo(name = "points_leader")
    val pointsLeaders: List<Game.PointsLeader>
)
