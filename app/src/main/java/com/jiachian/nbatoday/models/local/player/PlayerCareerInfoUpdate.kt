package com.jiachian.nbatoday.models.local.player

import androidx.room.ColumnInfo

data class PlayerCareerInfoUpdate(
    @ColumnInfo(name = "player_id") val playerId: Int,
    @ColumnInfo(name = "player_info") val info: PlayerCareer.PlayerCareerInfo,
)
