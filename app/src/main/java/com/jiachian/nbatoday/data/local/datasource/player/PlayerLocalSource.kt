package com.jiachian.nbatoday.data.local.datasource.player

import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import kotlinx.coroutines.flow.Flow

abstract class PlayerLocalSource {
    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    abstract suspend fun insertPlayerCareer(stats: PlayerCareer)
    abstract suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate)
    abstract suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate)

    abstract suspend fun existPlayer(playerId: Int): Boolean
}
