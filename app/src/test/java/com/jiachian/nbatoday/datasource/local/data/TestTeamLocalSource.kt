package com.jiachian.nbatoday.datasource.local.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TestTeamLocalSource(
    dataHolder: DataHolder
) : TeamLocalSource() {
    private val teams = dataHolder.teams
    private val teamPlayers = dataHolder.teamPlayers

    override fun getTeams(conference: NBATeam.Conference): Flow<List<Team>> {
        return teams.map { teams ->
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
            TeamAndPlayers(
                team = team,
                teamPlayers = players
            )
        }
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank> {
        return combine(
            getTeamStanding(teamId, conference),
            getTeamPointsRank(teamId),
            getTeamAssistsRank(teamId),
            getTeamReboundsRank(teamId),
            getTeamPlusMinusRank(teamId)
        ) { standing, points, rebounds, assists, plusMinus ->
            TeamRank(
                standing = standing,
                pointsRank = points,
                reboundsRank = rebounds,
                assistsRank = assists,
                plusMinusRank = plusMinus
            )
        }
    }

    override suspend fun insertTeams(teams: List<Team>) {
        this.teams.value = this.teams.value.toMutableList().apply {
            val id = teams.map { it.teamId }
            removeIf { team -> team.teamId in id }
            addAll(teams)
        }
    }

    override suspend fun insertTeamPlayers(teamPlayers: List<TeamPlayer>) {
        this.teamPlayers.value = this.teamPlayers.value.toMutableList().apply {
            val id = teamPlayers.map { it.playerId }
            removeIf { player -> player.playerId in id }
            addAll(teamPlayers)
        }
    }

    override suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>) {
        this.teamPlayers.value = this.teamPlayers.value.toMutableList().apply {
            removeIf { player ->
                player.teamId == teamId && player.playerId in playerIds
            }
        }
    }

    private fun getTeam(teamId: Int): Flow<Team> {
        return teams.map { teams ->
            teams.first { team ->
                team.teamId == teamId
            }
        }
    }

    private fun getTeamPlayers(teamId: Int): Flow<List<TeamPlayer>> {
        return teamPlayers.map { players ->
            players.filter { player ->
                player.teamId == teamId
            }
        }
    }

    private fun getTeamStanding(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return teams.map { teams ->
            teams.filter { team ->
                team.teamConference == conference
            }.sortedByDescending { team ->
                team.winPercentage
            }.indexOfFirst { team ->
                team.teamId == teamId
            } + 1
        }
    }

    private fun getTeamPointsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.pointsAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    private fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.reboundsTotalAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    private fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.assistsAverage
        }.thenByDescending { team ->
            team.winPercentage
        }
        return teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }

    private fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        val comparator = compareByDescending<Team> { team ->
            team.plusMinus
        }.thenByDescending { team ->
            team.winPercentage
        }
        return teams.map { teams ->
            teams
                .sortedWith(comparator)
                .indexOfFirst { team ->
                    team.teamId == teamId
                } + 1
        }
    }
}
