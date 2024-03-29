package com.jiachian.nbatoday.usecase.team

data class TeamUseCase(
    val getTeams: GetTeams,
    val addTeams: AddTeams,
    val updateTeamInfo: UpdateTeamInfo,
    val getTeamAndPlayers: GetTeamAndPlayers,
    val getTeamRank: GetTeamRank,
)
