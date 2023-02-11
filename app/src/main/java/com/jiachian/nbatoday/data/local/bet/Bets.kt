package com.jiachian.nbatoday.data.local.bet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nba_game_bets")
data class Bets(
    @ColumnInfo(name = "bets_id") @PrimaryKey(autoGenerate = true) val betsId: Long = 0,
    @ColumnInfo(name = "bets_game_id") val gameId: String,
    @ColumnInfo(name = "bets_home_points") val homePoints: Long,
    @ColumnInfo(name = "bets_away_points") val awayPoints: Long
)