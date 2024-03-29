package com.jiachian.nbatoday.usecase.team

import com.jiachian.nbatoday.common.Error
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.team.models.TeamPlayerSorting
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.repository.team.TeamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

enum class GetTeamAndPlayersError : Error {
    TEAM_NOT_FOUND,
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetTeamAndPlayers(
    private val repository: TeamRepository,
) {
    operator fun invoke(teamId: Int, sorting: TeamPlayerSorting): Flow<Resource<TeamAndPlayers, GetTeamAndPlayersError>> {
        return repository.getTeamAndPlayers(teamId)
            .mapLatest {
                if (it == null) {
                    Resource.Error(GetTeamAndPlayersError.TEAM_NOT_FOUND)
                } else {
                    Resource.Success(it.copy(teamPlayers = it.teamPlayers.sortedWith(sorting)))
                }
            }
    }

    private fun List<TeamPlayer>.sortedWith(sorting: TeamPlayerSorting): List<TeamPlayer> {
        val comparator = when (sorting) {
            TeamPlayerSorting.GP -> compareByDescending { it.gamePlayed }
            TeamPlayerSorting.W -> compareByDescending { it.win }
            TeamPlayerSorting.L -> compareBy { it.lose }
            TeamPlayerSorting.WINP -> compareByDescending { it.winPercentage }
            TeamPlayerSorting.PTS -> compareByDescending { it.pointsAverage }
            TeamPlayerSorting.FGM -> compareByDescending { it.fieldGoalsMadeAverage }
            TeamPlayerSorting.FGA -> compareByDescending { it.fieldGoalsAttemptedAverage }
            TeamPlayerSorting.FGP -> compareByDescending { it.fieldGoalsPercentage }
            TeamPlayerSorting.PM3 -> compareByDescending { it.threePointersMadeAverage }
            TeamPlayerSorting.PA3 -> compareByDescending { it.threePointersAttemptedAverage }
            TeamPlayerSorting.PP3 -> compareByDescending { it.threePointersPercentage }
            TeamPlayerSorting.FTM -> compareByDescending { it.freeThrowsMadeAverage }
            TeamPlayerSorting.FTA -> compareByDescending { it.freeThrowsAttemptedAverage }
            TeamPlayerSorting.FTP -> compareByDescending { it.freeThrowsPercentage }
            TeamPlayerSorting.OREB -> compareByDescending { it.reboundsOffensiveAverage }
            TeamPlayerSorting.DREB -> compareByDescending { it.reboundsDefensiveAverage }
            TeamPlayerSorting.REB -> compareByDescending { it.reboundsTotalAverage }
            TeamPlayerSorting.AST -> compareByDescending { it.assistsAverage }
            TeamPlayerSorting.TOV -> compareBy { it.turnoversAverage }
            TeamPlayerSorting.STL -> compareByDescending { it.stealsAverage }
            TeamPlayerSorting.BLK -> compareByDescending { it.blocksAverage }
            TeamPlayerSorting.PF -> compareBy { it.foulsPersonalAverage }
            TeamPlayerSorting.PLUSMINUS -> compareByDescending<TeamPlayer> { it.plusMinus }
        }.thenByDescending { it.winPercentage }
        return sortedWith(comparator)
    }
}
