package com.jiachian.nbatoday.models.remote.player

import com.jiachian.nbatoday.models.local.player.PlayerCareer

fun RemotePlayerDetail.toPlayer(): PlayerCareer? {
    val playerId = getPlayerId() ?: return null
    val playerInfo = info?.toPlayerInfo()
    val playerStats = stats?.toPlayerStats()
    if (playerInfo == null || playerStats == null) return null
    return PlayerCareer(
        playerId = playerId,
        info = playerInfo,
        stats = playerStats,
    )
}

private fun RemotePlayerDetail.getPlayerId(): Int? {
    return info?.getPlayerInfo("PERSON_ID")?.toIntOrNull()
}
