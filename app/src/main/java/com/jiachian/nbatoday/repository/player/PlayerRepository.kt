package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class PlayerRepository : BaseRepository() {
    abstract suspend fun insertPlayer(playerId: Int)

    abstract fun getPlayer(playerId: Int): Flow<Player?>
}
