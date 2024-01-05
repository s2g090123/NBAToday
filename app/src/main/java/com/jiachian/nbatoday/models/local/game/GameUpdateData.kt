package com.jiachian.nbatoday.models.local.game

import androidx.room.ColumnInfo

/**
 * 每場比賽的數據，需要透過兩個不同的api才能彙整一份資料，
 * 1. 透過`static/json/staticData/scheduleLeagueV2_32.json`取得賽季的賽程表(當中會包含簡單的數據)
 * 2. 透過`stats/scoreboardv3`取得特定日期裡各場比賽的數據(包含leaders數據)
 *
 * 因此會在初始時(App尚未取得任何數據)先透過(1)初始化資料庫，再透過根據當日且推算前幾天後幾天，透過(2)取得更詳細的資料，
 * 而此Data Class就是用來將(2)的資料更新於先前的(1)的資料中
 */
data class GameUpdateData(
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
    @ColumnInfo(name = "game_leaders")
    val gameLeaders: GameLeaders,
    @ColumnInfo(name = "team_leaders")
    val teamLeaders: GameLeaders
)
