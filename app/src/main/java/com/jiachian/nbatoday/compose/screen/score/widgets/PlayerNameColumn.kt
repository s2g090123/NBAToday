package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency50
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.models.local.score.BoxScoreRowData
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun PlayerNameColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<BoxScoreRowData>,
    onClickPlayer: (playerId: Int) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp),
            text = stringResource(R.string.box_score_label_player),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        PlayerColumn(
            playerState = state,
            scoreRowData = scoreRowData,
            onClickPlayer = onClickPlayer
        )
    }
}

@Composable
private fun PlayerColumn(
    playerState: LazyListState,
    scoreRowData: List<BoxScoreRowData>,
    onClickPlayer: (playerId: Int) -> Unit
) {
    DisableOverscroll {
        LazyColumn(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_Players")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = playerState
        ) {
            itemsIndexed(scoreRowData) { index, rowData ->
                Column(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Column_Player")
                        .fillMaxWidth()
                        .rippleClickable {
                            onClickPlayer(rowData.playerId)
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .testTag("PlayerStatistics_Text_PlayerName")
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp),
                        text = rowData.nameAbbr,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.secondary.copy(
                            if (rowData.notPlaying) Transparency50
                            else 1f
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index < scoreRowData.size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = dividerSecondaryColor(),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}
