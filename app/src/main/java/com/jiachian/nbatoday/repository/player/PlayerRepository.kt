package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class PlayerRepository : BaseRepository() {
    abstract suspend fun refreshPlayerStats(playerId: Int)

    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>
}
