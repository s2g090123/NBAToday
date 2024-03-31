package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.data.remote.RemotePlayerGenerator
import com.jiachian.nbatoday.models.remote.player.extensions.toPlayer
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.utils.getOrAssert

object PlayerGenerator {
    fun getHome(): Player {
        return RemotePlayerGenerator
            .get(HomePlayerId)
            .toPlayer()
            .getOrAssert()
    }
}
