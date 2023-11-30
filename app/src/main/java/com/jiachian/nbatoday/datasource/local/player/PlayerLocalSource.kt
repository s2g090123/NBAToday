package com.jiachian.nbatoday.datasource.local.player

import com.jiachian.nbatoday.models.local.player.PlayerCareer
import kotlinx.coroutines.flow.Flow

abstract class PlayerLocalSource {
    abstract fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    abstract suspend fun insertPlayerCareer(playerCareer: PlayerCareer)
}
