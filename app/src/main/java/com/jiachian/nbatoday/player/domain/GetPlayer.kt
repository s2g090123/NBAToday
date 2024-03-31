package com.jiachian.nbatoday.player.domain

import com.jiachian.nbatoday.common.domain.Resource
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.player.domain.error.GetPlayerError
import com.jiachian.nbatoday.player.ui.model.PlayerStatsSorting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlayer(
    private val repository: PlayerRepository,
) {
    operator fun invoke(playerId: Int, sorting: PlayerStatsSorting): Flow<Resource<Player, GetPlayerError>> {
        return repository
            .getPlayer(playerId)
            .mapLatest { player ->
                player?.let {
                    Resource.Success(
                        player.copy(
                            stats = player.stats.copy(
                                stats = player.stats.stats.sortedWith(sorting)
                            )
                        )
                    )
                } ?: Resource.Error(GetPlayerError.NOT_FOUND)
            }
    }

    private fun List<Player.PlayerStats.Stats>.sortedWith(sorting: PlayerStatsSorting): List<Player.PlayerStats.Stats> {
        val comparator = when (sorting) {
            PlayerStatsSorting.TIME_FRAME -> compareByDescending { it.timeFrame }
            PlayerStatsSorting.GP -> compareByDescending { it.gamePlayed }
            PlayerStatsSorting.W -> compareByDescending { it.win }
            PlayerStatsSorting.L -> compareBy { it.lose }
            PlayerStatsSorting.WINP -> compareByDescending { it.winPercentage }
            PlayerStatsSorting.PTS -> compareByDescending { it.pointsAverage }
            PlayerStatsSorting.FGM -> compareByDescending { it.fieldGoalsMadeAverage }
            PlayerStatsSorting.FGA -> compareByDescending { it.fieldGoalsAttemptedAverage }
            PlayerStatsSorting.FGP -> compareByDescending { it.fieldGoalsPercentage }
            PlayerStatsSorting.PM3 -> compareByDescending { it.threePointersMadeAverage }
            PlayerStatsSorting.PA3 -> compareByDescending { it.threePointersAttemptedAverage }
            PlayerStatsSorting.PP3 -> compareByDescending { it.threePointersPercentage }
            PlayerStatsSorting.FTM -> compareByDescending { it.freeThrowsMadeAverage }
            PlayerStatsSorting.FTA -> compareByDescending { it.freeThrowsAttemptedAverage }
            PlayerStatsSorting.FTP -> compareByDescending { it.freeThrowsPercentage }
            PlayerStatsSorting.OREB -> compareByDescending { it.reboundsOffensiveAverage }
            PlayerStatsSorting.DREB -> compareByDescending { it.reboundsDefensiveAverage }
            PlayerStatsSorting.REB -> compareByDescending { it.reboundsTotalAverage }
            PlayerStatsSorting.AST -> compareByDescending { it.assistsAverage }
            PlayerStatsSorting.TOV -> compareBy { it.turnoversAverage }
            PlayerStatsSorting.STL -> compareByDescending { it.stealsAverage }
            PlayerStatsSorting.BLK -> compareByDescending { it.blocksAverage }
            PlayerStatsSorting.PF -> compareBy { it.foulsPersonalAverage }
            PlayerStatsSorting.PLUSMINUS -> compareByDescending<Player.PlayerStats.Stats> { it.plusMinus }
        }.thenByDescending { it.winPercentage }
        return sortedWith(comparator)
    }
}
