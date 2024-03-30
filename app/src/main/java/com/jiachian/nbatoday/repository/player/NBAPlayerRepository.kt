package com.jiachian.nbatoday.repository.player

import com.jiachian.nbatoday.CurrentSeason
import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.remote.player.extensions.toPlayer
import com.jiachian.nbatoday.service.PlayerService
import com.jiachian.nbatoday.utils.isError
import kotlinx.coroutines.flow.Flow

class NBAPlayerRepository(
    private val dao: PlayerDao,
    private val service: PlayerService,
) : PlayerRepository {
    override suspend fun addPlayer(playerId: Int): Response<Unit> {
        return service
            .getPlayer(season = CurrentSeason, playerId = playerId)
            .takeIf { !it.isError() }
            ?.body()
            ?.toPlayer()
            ?.also { player ->
                dao.addPlayer(player)
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    override fun getPlayer(playerId: Int): Flow<Player?> {
        return dao.getPlayer(playerId)
    }
}
