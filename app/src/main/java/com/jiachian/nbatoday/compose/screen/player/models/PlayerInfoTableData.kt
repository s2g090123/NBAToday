package com.jiachian.nbatoday.compose.screen.player.models

data class PlayerInfoTableData(
    val rowData: List<RowData>,
) {
    data class RowData(
        val data: List<Data>,
    ) {
        data class Data(
            val title: String,
            val value: String,
        )
    }
}
