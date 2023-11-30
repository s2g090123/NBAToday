package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.models.local.player.Player
import kotlinx.coroutines.flow.Flow

abstract class PlayerLocalSource {
    abstract fun getPlayer(playerId: Int): Flow<Player?>

    abstract suspend fun insertPlayer(player: Player)
}
