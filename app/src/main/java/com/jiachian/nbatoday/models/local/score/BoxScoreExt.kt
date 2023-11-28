package com.jiachian.nbatoday.models.local.score

fun BoxScore.BoxScoreTeam.Player.createRowData(statsRowData: List<BoxScoreRowData.RowData>): BoxScoreRowData {
    return BoxScoreRowData(
        playerId = playerId,
        nameAbbr = nameAbbr,
        rowData = statsRowData,
        position = if (starter) position.last().toString() else "",
        notPlaying = status == PlayerActiveStatus.INACTIVE,
        notPlayingReason = notPlayingReason
    )
}
