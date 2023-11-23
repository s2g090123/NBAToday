package com.jiachian.nbatoday.data.remote.datasource.player

import com.jiachian.nbatoday.data.remote.datasource.RemoteSource
import com.jiachian.nbatoday.data.remote.player.RemotePlayerDetail
import com.jiachian.nbatoday.data.remote.player.RemotePlayerInfo
import com.jiachian.nbatoday.data.remote.player.RemotePlayerStats

abstract class PlayerRemoteSource : RemoteSource() {
    abstract suspend fun getPlayerInfo(playerId: Int): RemotePlayerInfo?
    abstract suspend fun getPlayerCareerStats(playerId: Int): RemotePlayerStats?
    abstract suspend fun getPlayerDetail(playerId: Int): RemotePlayerDetail?
}
