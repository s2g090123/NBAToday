package com.jiachian.nbatoday.boxscore.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jiachian.nbatoday.boxscore.ui.main.state.BoxScoreInfoState

@Composable
fun ScoreInfo(
    info: BoxScoreInfoState,
) {
    Column {
        info.boxScore?.let { boxScore ->
            ScoreTotal(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                boxScore = boxScore,
            )
            ScorePeriod(
                modifier = Modifier
                    .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth(),
                boxScore = boxScore,
                labels = info.periods,
            )
        }
    }
}
