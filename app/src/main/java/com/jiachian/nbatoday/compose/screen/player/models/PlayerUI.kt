package com.jiachian.nbatoday.compose.screen.player.models

import com.jiachian.nbatoday.models.local.player.Player

data class PlayerUI(
    val player: Player,
    val infoTableData: PlayerInfoTableData,
    val statsRowData: List<PlayerStatsRowData>,
)
