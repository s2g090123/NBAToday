package com.jiachian.nbatoday.team.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class UpdateTeamInfoError : Error {
    UPDATE_TEAMS_FAILED,
    UPDATE_PLAYERS_FAILED,
}
