package com.jiachian.nbatoday.player.data

import com.jiachian.nbatoday.common.data.CurrentSeason
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.player.data.model.toPlayer
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
