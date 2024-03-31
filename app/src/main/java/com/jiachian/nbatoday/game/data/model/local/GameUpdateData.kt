package com.jiachian.nbatoday.game.data.model.local

import androidx.room.ColumnInfo

/**
 * Data for each game needs to be aggregated through two different APIs,
 * 1. Fetch the season schedule from `static/json/staticData/scheduleLeagueV2_32.json` (which contains basic data about the schedule).
 * 2. Retrieve specific game data, including leaders' statistics, for each game on a given date through `stats/scoreboardv3`.
 *
 * Therefore, during the initialization phase (when the app has not obtained any data yet), the database is initialized through (1).
 * Subsequently, more detailed data is obtained by using (2) based on the current date and estimating a few days before and after.
 * This Data Class is designed to update the data from (2) into the previously fetched data from (1).
 */

data class GameUpdateData(
    @ColumnInfo(name = "game_id")
    val gameId: String, // e.g. 0022200089
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatus,
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // e.g. Final or 3:00 pm ET
    @ColumnInfo(name = "home_team")
    val homeTeam: GameTeam,
    @ColumnInfo(name = "away_team")
    val awayTeam: GameTeam,
    @ColumnInfo(name = "game_leaders")
    val gameLeaders: GameLeaders,
    @ColumnInfo(name = "team_leaders")
    val teamLeaders: GameLeaders
)
