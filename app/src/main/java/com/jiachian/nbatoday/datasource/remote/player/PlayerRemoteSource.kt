package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.models.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.models.remote.player.RemotePlayerStats

abstract class PlayerRemoteSource : RemoteSource() {
    abstract suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo?
    abstract suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats?
    abstract suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail?
}
