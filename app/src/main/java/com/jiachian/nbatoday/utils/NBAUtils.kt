package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.common.data.CdnBaseUrl

object NBAUtils {
    fun getTeamLogoUrlById(teamId: Int): String {
        return "${CdnBaseUrl}logos/nba/$teamId/global/L/logo.svg"
    }

    fun getTeamSmallLogoUrlById(teamId: Int): String {
        return "${CdnBaseUrl}logos/nba/$teamId/primary/L/logo.svg"
    }

    fun getPlayerImageUrlById(playerId: Int): String {
        return "${CdnBaseUrl}headshots/nba/latest/260x190/$playerId.png"
    }
}
