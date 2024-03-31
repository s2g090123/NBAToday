package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerFirstName
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.PlayerBirthDateTime
import com.jiachian.nbatoday.TeamCity
import com.jiachian.nbatoday.player.data.model.remote.PlayerDto
import com.jiachian.nbatoday.player.data.model.remote.PlayerInfoDto
import com.jiachian.nbatoday.player.data.model.remote.PlayerStatsDto

object RemotePlayerGenerator {
    fun get(playerId: Int): PlayerDto {
        return PlayerDto(
            info = getPlayerInfo(playerId),
            stats = getPlayerStats(playerId)
        )
    }

    private fun getPlayerStats(playerId: Int): PlayerStatsDto {
        return PlayerStatsDto(
            parameters = PlayerStatsDto.RemoteParameter(playerId),
            resultSets = listOf(getPlayerStatsResult(playerId))
        )
    }

    private fun getPlayerStatsResult(playerId: Int): PlayerStatsDto.RemoteResult {
        val teamId = if (playerId == HomePlayerId) HomeTeamId else AwayTeamId
        val teamAbbr = if (playerId == HomePlayerId) HomeTeamAbbr else AwayTeamAbbr
        return PlayerStatsDto.RemoteResult(
            name = "ByYearPlayerDashboard",
            headers = listOf(
                "GROUP_VALUE",
                "TEAM_ID",
                "TEAM_ABBREVIATION",
                "GP",
                "W",
                "L",
                "W_PCT",
                "FGM",
                "FGA",
                "FG_PCT",
                "FG3M",
                "FG3A",
                "FG3_PCT",
                "FTM",
                "FTA",
                "FT_PCT",
                "OREB",
                "DREB",
                "REB",
                "AST",
                "TOV",
                "STL",
                "BLK",
                "PF",
                "PTS",
                "PLUS_MINUS",
                "GP_RANK",
                "W_RANK",
                "L_RANK",
                "W_PCT_RANK",
                "FGM_RANK",
                "FGA_RANK",
                "FG_PCT_RANK",
                "FG3M_RANK",
                "FG3A_RANK",
                "FG3_PCT_RANK",
                "FTM_RANK",
                "FTA_RANK",
                "FT_PCT_RANK",
                "OREB_RANK",
                "DREB_RANK",
                "REB_RANK",
                "AST_RANK",
                "TOV_RANK",
                "STL_RANK",
                "BLK_RANK",
                "PF_RANK",
                "PTS_RANK",
                "PLUS_MINUS_RANK"
            ),
            rowData = listOf(
                listOf(
                    BasicNumber.toString(),
                    teamId.toString(),
                    teamAbbr,
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                    BasicNumber.toString(),
                )
            )
        )
    }

    private fun getPlayerInfo(playerId: Int): PlayerInfoDto {
        return PlayerInfoDto(
            resultSets = getPlayerInfoResults(playerId)
        )
    }

    private fun getPlayerInfoResults(playerId: Int): List<PlayerInfoDto.RemoteResult> {
        val firstName = if (playerId == HomePlayerId) HomePlayerFirstName else AwayPlayerFirstName
        val lastName = if (playerId == HomePlayerId) HomePlayerLastName else AwayPlayerLastName
        val teamId = if (playerId == HomePlayerId) HomeTeamId else AwayTeamId
        val teamName = if (playerId == HomePlayerId) HomeTeamName else AwayTeamName
        val teamAbbr = if (playerId == HomePlayerId) HomeTeamAbbr else AwayTeamAbbr
        return listOf(
            PlayerInfoDto.RemoteResult(
                name = "CommonPlayerInfo",
                headers = listOf(
                    "PERSON_ID",
                    "DISPLAY_FIRST_LAST",
                    "DISPLAY_FI_LAST",
                    "COUNTRY",
                    "SCHOOL",
                    "HEIGHT",
                    "WEIGHT",
                    "SEASON_EXP",
                    "JERSEY",
                    "POSITION",
                    "TEAM_ID",
                    "TEAM_NAME",
                    "TEAM_ABBREVIATION",
                    "TEAM_CITY",
                    "FROM_YEAR",
                    "TO_YEAR",
                    "DRAFT_YEAR",
                    "DRAFT_ROUND",
                    "DRAFT_NUMBER",
                    "GREATEST_75_FLAG",
                    "BIRTHDATE",
                ),
                rowData = listOf(
                    listOf(
                        playerId.toString(),
                        firstName,
                        lastName,
                        TeamCity,
                        TeamCity,
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicPosition,
                        teamId.toString(),
                        teamName,
                        teamAbbr,
                        TeamCity,
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        "Y",
                        PlayerBirthDateTime
                    )
                )
            ),
            PlayerInfoDto.RemoteResult(
                name = "PlayerHeadlineStats",
                headers = listOf(
                    "TimeFrame",
                    "PTS",
                    "AST",
                    "REB",
                    "PIE"
                ),
                rowData = listOf(
                    listOf(
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString(),
                        BasicNumber.toString()
                    )
                )
            )
        )
    }
}
