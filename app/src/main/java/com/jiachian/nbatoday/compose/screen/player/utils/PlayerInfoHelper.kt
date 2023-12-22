package com.jiachian.nbatoday.compose.screen.player.utils

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.models.local.player.Player

object PlayerInfoHelper {
    fun getTableData(info: Player.PlayerInfo): PlayerInfoTableData {
        return PlayerInfoTableData(getRowData(info))
    }

    private fun getRowData(info: Player.PlayerInfo): List<PlayerInfoTableData.RowData> {
        return listOf(
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_ppg, info.points.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_rpg, info.rebounds.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_apg, info.assists.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_pie, info.impact.toString()
                    ),
                )
            ),
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_height,
                        info.heightFormatted
                    ),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_weight,
                        info.weightFormatted
                    ),
                    PlayerInfoTableData.RowData.Data(R.string.player_career_info_country, info.country),
                    PlayerInfoTableData.RowData.Data(R.string.player_career_info_attended, info.school),
                )
            ),
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_age, info.playerAge.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(R.string.player_career_info_birth, info.birthDate),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_draft,
                        info.draftFormatted
                    ),
                    PlayerInfoTableData.RowData.Data(
                        R.string.player_career_info_experience, info.seasonExperience.toString()
                    ),
                )
            ),
        )
    }
}
