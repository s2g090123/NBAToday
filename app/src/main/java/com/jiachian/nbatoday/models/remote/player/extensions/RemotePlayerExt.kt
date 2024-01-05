package com.jiachian.nbatoday.models.remote.player.extensions

import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.remote.player.RemotePlayer

fun RemotePlayer.toPlayer(): Player? {
    val playerId = getPlayerId()
    val playerInfo = info?.toPlayerInfo()
    val playerStats = stats?.toPlayerStats()
    if (playerId == null || playerInfo == null || playerStats == null) return null
    return Player(
        playerId = playerId,
        info = playerInfo,
        stats = playerStats,
    )
}

private fun RemotePlayer.getPlayerId(): Int? {
    return info?.getPlayerInfo("PERSON_ID")?.toIntOrNull()
}
