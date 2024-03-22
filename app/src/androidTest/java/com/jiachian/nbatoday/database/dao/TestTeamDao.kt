package com.jiachian.nbatoday.database.dao

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestTeamDao(
    private val dataHolder: DataHolder
) : TeamDao {
    override fun getTeams(conference: NBATeam.Conference): Flow<List<Team>> {
        return dataHolder.teams.map { teams ->
            teams.filter { team ->
                team.teamConference == conference
            }
        }
    }

    override fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?> {
        return combine(
            getTeam(teamId),
            getTeamPlayers(teamId)
        ) { team, players ->
            team ?: return@combine null
            TeamAndPlayers(
                team = team,
                teamPlayers = players
            )
        }
    }

    override fun getTeamStanding(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return dataHolder.teams.map { teams ->
            teams.filter { team ->
                team.teamConference == conference
            }.sortedByDescending { team ->
                team.winPercentage
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    override suspend fun addTeams(stats: List<Team>) {
        dataHolder.teams.value = dataHolder.teams.value.toMutableList().apply {
            val id = stats.map { it.teamId }
            removeIf { team -> team.teamId in id }
            addAll(stats)
        }
    }

    override suspend fun addTeamPlayers(stats: List<TeamPlayer>) {
        dataHolder.teamPlayers.value = dataHolder.teamPlayers.value.toMutableList().apply {
            val id = stats.map { it.playerId }
            removeIf { player -> player.playerId in id }
            addAll(stats)
        }
    }

    override suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>) {
        dataHolder.teamPlayers.value = dataHolder.teamPlayers.value.toMutableList().apply {
            removeIf { player ->
                player.teamId == teamId && player.playerId in playerIds
            }
        }
    }

    private fun getTeam(teamId: Int): Flow<Team?> {
        return dataHolder.teams.map { teams ->
            teams.firstOrNull { team ->
                team.teamId == teamId
            }
        }
    }

    private fun getTeamPlayers(teamId: Int): Flow<List<TeamPlayer>> {
        return dataHolder.teamPlayers.map { players ->
            players.filter { player ->
                player.teamId == teamId
            }
        }
    }

    override fun getPointsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.pointsAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return dataHolder.teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    override fun getReboundsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.reboundsTotalAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return dataHolder.teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    override fun getAssistsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.assistsAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return dataHolder.teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    override fun getPlusMinusRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.plusMinus
        }.thenByDescending { team ->
            team.winPercentage
        }
        return dataHolder.teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }
}
