package com.jiachian.nbatoday.compose.screen.player.models

import androidx.annotation.StringRes
import com.jiachian.nbatoday.R

enum class PlayerTableLabel(
    @StringRes val titleRes: Int
) {
    PPG(R.string.player_career_info_ppg),
    RPG(R.string.player_career_info_rpg),
    APG(R.string.player_career_info_apg),
    PIE(R.string.player_career_info_pie),
    HEIGHT(R.string.player_career_info_height),
    WEIGHT(R.string.player_career_info_weight),
    COUNTRY(R.string.player_career_info_country),
    LAST_ATTENDED(R.string.player_career_info_attended),
    AGE(R.string.player_career_info_age),
    BIRTHDATE(R.string.player_career_info_birth),
    DRAFT(R.string.player_career_info_draft),
    EXPERIENCE(R.string.player_career_info_experience)
}
