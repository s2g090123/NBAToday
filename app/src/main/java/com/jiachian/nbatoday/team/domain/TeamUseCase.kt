package com.jiachian.nbatoday.team.domain

data class TeamUseCase(
    val getTeams: GetTeams,
    val addTeams: AddTeams,
    val updateTeamInfo: UpdateTeamInfo,
    val getTeamAndPlayers: GetTeamAndPlayers,
    val getTeamRank: GetTeamRank,
)
