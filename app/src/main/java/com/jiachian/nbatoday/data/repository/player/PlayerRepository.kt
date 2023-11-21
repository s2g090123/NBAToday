package com.jiachian.nbatoday.data.repository.player

import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class PlayerRepository : BaseRepository() {
    abstract suspend fun refreshPlayerStats(playerId: Int)

    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>
}
