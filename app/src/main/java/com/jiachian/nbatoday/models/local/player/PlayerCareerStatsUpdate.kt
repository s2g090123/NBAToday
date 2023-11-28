package com.jiachian.nbatoday.models.local.player

import androidx.room.ColumnInfo

data class PlayerCareerStatsUpdate(
    @ColumnInfo(name = "player_id") val playerId: Int,
    @ColumnInfo(name = "player_stats") val stats: PlayerCareer.PlayerCareerStats
)
