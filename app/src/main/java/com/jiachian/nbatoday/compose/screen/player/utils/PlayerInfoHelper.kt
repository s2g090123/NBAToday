package com.jiachian.nbatoday.compose.screen.player.utils

import androidx.annotation.StringRes
import com.jiachian.nbatoday.MainApplication
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoRowData
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.models.local.player.Player

object PlayerInfoHelper {
    fun createPlayerInfoTableData(info: Player.PlayerInfo): PlayerInfoTableData {
        return PlayerInfoTableData(
            firstRowData = PlayerInfoRowData(
                firstContent = getString(R.string.player_career_info_ppg) to info.points.toString(),
                secondContent = getString(R.string.player_career_info_rpg) to info.rebounds.toString(),
                thirdContent = getString(R.string.player_career_info_apg) to info.assists.toString(),
                forthContent = getString(R.string.player_career_info_pie) to info.impact.toString(),
            ),
            secondRowData = PlayerInfoRowData(
                firstContent = getString(R.string.player_career_info_height) to getString(
                    R.string.player_career_info_height_value,
                    info.height
                ),
                secondContent = getString(R.string.player_career_info_weight) to getString(
                    R.string.player_career_info_weight_value,
                    info.weight
                ),
                thirdContent = getString(R.string.player_career_info_country) to info.country,
                forthContent = getString(R.string.player_career_info_attended) to info.school,
            ),
            thirdRowData = PlayerInfoRowData(
                firstContent = getString(R.string.player_career_info_age) to info.playerAge.toString(),
                secondContent = getString(R.string.player_career_info_birth) to info.birthDate,
                thirdContent = getString(R.string.player_career_info_draft) to getString(
                    R.string.player_career_info_draft_format,
                    info.draftYear,
                    info.draftRound,
                    info.draftNumber
                ),
                forthContent = getString(R.string.player_career_info_experience) to info.seasonExperience.toString(),
            ),
        )
    }

    private fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return MainApplication.context.getString(id, *formatArgs)
    }
}
