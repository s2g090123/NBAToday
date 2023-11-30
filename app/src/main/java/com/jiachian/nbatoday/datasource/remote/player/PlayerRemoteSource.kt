package com.jiachian.nbatoday.datasource.remote.player

import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.models.remote.player.RemotePlayer
import retrofit2.Response

abstract class PlayerRemoteSource : RemoteSource() {
    abstract suspend fun getPlayer(playerId: Int): Response<RemotePlayer>
}
