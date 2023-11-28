package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.models.local.score.BoxScoreRowData

@Composable
fun PlayerStatistics(
    modifier: Modifier = Modifier,
    scoreRowData: List<BoxScoreRowData>,
    labels: Array<ScoreLabel>,
    showPlayerCareer: (Int) -> Unit
) {
    val statisticsState = rememberLazyListState()
    val playerState = rememberLazyListState()
    val stateStatisticsOffset by remember { derivedStateOf { statisticsState.firstVisibleItemScrollOffset } }
    val stateStatisticsIndex by remember { derivedStateOf { statisticsState.firstVisibleItemIndex } }
    val statePlayerOffset by remember { derivedStateOf { playerState.firstVisibleItemScrollOffset } }
    val statePlayerIndex by remember { derivedStateOf { playerState.firstVisibleItemIndex } }
    Row(modifier = modifier) {
        PlayerNameColumn(
            modifier = Modifier.width(124.dp),
            state = playerState,
            scoreRowData = scoreRowData,
            onClickPlayer = showPlayerCareer
        )
        PlayerStatsColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_Column_Stats")
                .horizontalScroll(rememberScrollState()),
            state = statisticsState,
            scoreRowData = scoreRowData,
            labels = labels
        )
    }
    LaunchedEffect(stateStatisticsOffset, stateStatisticsIndex) {
        playerState.scrollToItem(stateStatisticsIndex, stateStatisticsOffset)
    }
    LaunchedEffect(statePlayerOffset, statePlayerIndex) {
        statisticsState.scrollToItem(statePlayerIndex, statePlayerOffset)
    }
}
