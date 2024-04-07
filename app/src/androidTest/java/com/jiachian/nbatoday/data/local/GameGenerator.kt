package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.remote.RemoteGameGenerator
import com.jiachian.nbatoday.data.remote.RemoteScheduleGenerator
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.toGameUpdateData
import com.jiachian.nbatoday.game.data.model.toGames
import com.jiachian.nbatoday.utils.getOrAssert

object GameGenerator {
    fun getFinal(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(0)
            ?.let { game ->
                val (gameLeaders, teamLeaders) = RemoteGameGenerator
                    .get()
                    .scoreboard
                    ?.toGameUpdateData()
                    ?.firstOrNull { it.gameId == game.gameId }
                    ?.let { updated -> updated.gameLeaders to updated.teamLeaders }
                    ?: (null to null)
                game.copy(
                    gameLeaders = gameLeaders,
                    teamLeaders = teamLeaders
                )
            }
            .getOrAssert()
    }

    fun getPlaying(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(1)
            ?.let { game ->
                val (gameLeaders, teamLeaders) = RemoteGameGenerator
                    .get()
                    .scoreboard
                    ?.toGameUpdateData()
                    ?.firstOrNull { it.gameId == game.gameId }
                    ?.let { updated -> updated.gameLeaders to updated.teamLeaders }
                    ?: (null to null)
                game.copy(
                    gameLeaders = gameLeaders,
                    teamLeaders = teamLeaders
                )
            }
            .getOrAssert()
    }

    fun getComingSoon(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(2)
            ?.let { game ->
                val (gameLeaders, teamLeaders) = RemoteGameGenerator
                    .get()
                    .scoreboard
                    ?.toGameUpdateData()
                    ?.firstOrNull { it.gameId == game.gameId }
                    ?.let { updated -> updated.gameLeaders to updated.teamLeaders }
                    ?: (null to null)
                game.copy(
                    gameLeaders = gameLeaders,
                    teamLeaders = teamLeaders
                )
            }
            .getOrAssert()
    }
}
