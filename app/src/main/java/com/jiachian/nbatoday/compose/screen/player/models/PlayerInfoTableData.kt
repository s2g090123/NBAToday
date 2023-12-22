package com.jiachian.nbatoday.compose.screen.player.models

import androidx.annotation.StringRes

data class PlayerInfoTableData(
    val rowData: List<RowData>,
) {
    data class RowData(
        val data: List<Data>,
    ) {
        data class Data(
            @StringRes val titleRes: Int,
            val value: String,
        )
    }
}
