package com.jiachian.nbatoday.models.local.bet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bet")
data class Bet(
    @ColumnInfo(name = "bet_id") @PrimaryKey(autoGenerate = true) val betId: Long = 0,
    @ColumnInfo(name = "bet_account") val account: String,
    @ColumnInfo(name = "bet_game_id") val gameId: String,
    @ColumnInfo(name = "bet_home_points") val homePoints: Long,
    @ColumnInfo(name = "bet_away_points") val awayPoints: Long
)
