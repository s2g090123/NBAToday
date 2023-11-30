package com.jiachian.nbatoday.models.remote.player

import com.jiachian.nbatoday.models.local.player.Player

fun RemotePlayer.toPlayer(): Player? {
    val playerId = getPlayerId() ?: return null
    val playerInfo = info?.toPlayerInfo()
    val playerStats = stats?.toPlayerStats()
    if (playerInfo == null || playerStats == null) return null
    return Player(
        playerId = playerId,
        info = playerInfo,
        stats = playerStats,
    )
}

private fun RemotePlayer.getPlayerId(): Int? {
    return info?.getPlayerInfo("PERSON_ID")?.toIntOrNull()
}
