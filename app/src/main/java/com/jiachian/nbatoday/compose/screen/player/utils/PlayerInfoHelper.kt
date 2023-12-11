package com.jiachian.nbatoday.compose.screen.player.utils

import androidx.annotation.StringRes
import com.jiachian.nbatoday.MainApplication
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.models.local.player.Player

object PlayerInfoHelper {
    fun createPlayerInfoTableData(info: Player.PlayerInfo): PlayerInfoTableData {
        return PlayerInfoTableData(getPlayerInfoRowData(info))
    }

    private fun getPlayerInfoRowData(info: Player.PlayerInfo): List<PlayerInfoTableData.RowData> {
        return listOf(
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_ppg), info.points.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_rpg), info.rebounds.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_apg), info.assists.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_pie), info.impact.toString()
                    ),
                )
            ),
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_height),
                        getString(R.string.player_career_info_height_value, info.height)
                    ),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_weight),
                        getString(R.string.player_career_info_weight_value, info.weight)
                    ),
                    PlayerInfoTableData.RowData.Data(getString(R.string.player_career_info_country), info.country),
                    PlayerInfoTableData.RowData.Data(getString(R.string.player_career_info_attended), info.school),
                )
            ),
            PlayerInfoTableData.RowData(
                data = listOf(
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_age), info.playerAge.toString()
                    ),
                    PlayerInfoTableData.RowData.Data(getString(R.string.player_career_info_birth), info.birthDate),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_draft),
                        getString(
                            R.string.player_career_info_draft_format,
                            info.draftYear,
                            info.draftRound,
                            info.draftNumber
                        )
                    ),
                    PlayerInfoTableData.RowData.Data(
                        getString(R.string.player_career_info_experience), info.seasonExperience.toString()
                    ),
                )
            ),
        )
    }

    private fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return MainApplication.context.getString(id, *formatArgs)
    }
}
