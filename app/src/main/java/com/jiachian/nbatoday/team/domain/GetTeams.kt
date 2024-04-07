package com.jiachian.nbatoday.team.domain

import com.jiachian.nbatoday.home.standing.ui.model.StandingSorting
import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetTeams(
    private val repository: TeamRepository
) {
    operator fun invoke(
        conference: NBATeam.Conference,
        sorting: StandingSorting = StandingSorting.WINP,
    ): Flow<List<Team>> {
        return repository
            .getTeams(conference)
            .mapLatest {
                it.sortedWith(sorting)
            }
    }

    private fun List<Team>.sortedWith(sorting: StandingSorting): List<Team> {
        val comparator = when (sorting) {
            StandingSorting.GP -> compareByDescending { it.gamePlayed }
            StandingSorting.W -> compareByDescending { it.win }
            StandingSorting.L -> compareBy { it.lose }
            StandingSorting.WINP -> compareByDescending { it.winPercentage }
            StandingSorting.PTS -> compareByDescending { it.pointsAverage }
            StandingSorting.FGP -> compareByDescending { it.fieldGoalsPercentage }
            StandingSorting.PP3 -> compareByDescending { it.threePointersPercentage }
            StandingSorting.FTP -> compareByDescending { it.freeThrowsPercentage }
            StandingSorting.OREB -> compareByDescending { it.reboundsOffensiveAverage }
            StandingSorting.DREB -> compareByDescending { it.reboundsDefensiveAverage }
            StandingSorting.AST -> compareByDescending { it.assistsAverage }
            StandingSorting.TOV -> compareBy { it.turnoversAverage }
            StandingSorting.STL -> compareByDescending { it.stealsAverage }
            StandingSorting.BLK -> compareByDescending<Team> { it.blocksAverage }
        }
            .thenByDescending { it.winPercentage }
            .thenByDescending { it.gamePlayed }
        return sortedWith(comparator)
    }
}
