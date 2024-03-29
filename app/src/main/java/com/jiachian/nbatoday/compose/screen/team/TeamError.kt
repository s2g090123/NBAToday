package com.jiachian.nbatoday.compose.screen.team

import com.jiachian.nbatoday.usecase.team.GetTeamAndPlayersError
import com.jiachian.nbatoday.usecase.team.UpdateTeamInfoError

enum class TeamError(val message: String) {
    UPDATE_TEAM_FAILED("Fetching info of the team is failed."),
    UPDATE_PLAYERS_FAILED("Fetching players of the team is failed."),
    TEAM_NOT_FOUND("The team is not found.")
}

fun GetTeamAndPlayersError.asTeamError(): TeamError {
    return when (this) {
        GetTeamAndPlayersError.TEAM_NOT_FOUND -> TeamError.TEAM_NOT_FOUND
    }
}

fun UpdateTeamInfoError.asTeamError(): TeamError {
    return when (this) {
        UpdateTeamInfoError.UPDATE_TEAMS_FAILED -> TeamError.UPDATE_TEAM_FAILED
        UpdateTeamInfoError.UPDATE_PLAYERS_FAILED -> TeamError.UPDATE_PLAYERS_FAILED
    }
}
