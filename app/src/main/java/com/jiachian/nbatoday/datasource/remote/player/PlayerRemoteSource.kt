package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats
import retrofit2.Response

abstract class PlayerRemoteSource : RemoteSource() {
    abstract suspend fun getPlayerInfo(playerId: Int): Response<RemotePlayerInfo>
    abstract suspend fun getPlayerStats(playerId: Int): Response<RemotePlayerStats>
    abstract suspend fun getPlayerDetail(playerId: Int): Response<RemotePlayerDetail>
}
