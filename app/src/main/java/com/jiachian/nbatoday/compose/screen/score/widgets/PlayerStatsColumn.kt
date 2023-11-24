package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.Transparency50
import com.jiachian.nbatoday.compose.screen.score.data.ScoreRowData
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabelType
import com.jiachian.nbatoday.compose.widget.DisableOverscroll
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.px2Dp
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.max

@Composable
fun PlayerStatsColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<ScoreRowData>,
    labels: Array<ScoreLabel>
) {
    var dividerWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        LabelRow(
            modifier = Modifier
                .testTag("PlayerStatistics_Row_Labels")
                .onSizeChanged {
                    dividerWidth = max(dividerWidth, it.width)
                }
                .fillMaxWidth()
                .padding(start = 16.dp),
            labels = labels
        )
        Divider(
            modifier = Modifier.width(dividerWidth.px2Dp()),
            color = dividerSecondaryColor(),
            thickness = 3.dp
        )
        PlayerStatsTable(
            modifier = Modifier
                .testTag("PlayerStatistics_LC_PlayerStats")
                .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
                .fillMaxWidth(),
            state = state,
            scoreRowData = scoreRowData,
            dividerWidth = dividerWidth.px2Dp()
        )
    }
}

@Composable
private fun LabelRow(
    modifier: Modifier = Modifier,
    labels: Array<ScoreLabel>
) {
    var selectedType by remember { mutableStateOf<ScoreLabelType?>(null) }
    Row(modifier = modifier) {
        labels.forEach { label ->
            LabelText(
                label = label,
                isPopupVisible = label.type == selectedType,
                onClick = { selectedType = it.type },
                onClickOutside = { selectedType = null }
            )
        }
    }
}

@Composable
private fun LabelText(
    label: ScoreLabel,
    isPopupVisible: Boolean,
    onClick: (label: ScoreLabel) -> Unit,
    onClickOutside: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("PlayerStatistics_Box_Label")
            .width(label.width)
            .height(40.dp)
            .rippleClickable { onClick(label) }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = label.textAlign,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        if (isPopupVisible) {
            LabelAboutPopup(
                text = stringResource(label.infoRes),
                onDismiss = { onClickOutside() }
            )
        }
    }
}

@Composable
private fun LabelAboutPopup(
    text: String,
    onDismiss: () -> Unit
) {
    Popup(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .shadow(8.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.secondaryVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .testTag("LabelAboutPopup_Text_About")
                    .padding(8.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun PlayerStatsTable(
    modifier: Modifier = Modifier,
    state: LazyListState,
    scoreRowData: List<ScoreRowData>,
    dividerWidth: Dp
) {
    DisableOverscroll {
        LazyColumn(
            modifier = modifier,
            state = state
        ) {
            itemsIndexed(scoreRowData) { index, rowData ->
                PlayerStatsRow(
                    modifier = Modifier
                        .testTag("PlayerStatistics_Row_PlayerStats")
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    rowData = rowData
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (index < scoreRowData.size - 1) {
                    Divider(
                        modifier = Modifier.width(dividerWidth),
                        color = dividerSecondaryColor(),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsRow(
    modifier: Modifier = Modifier,
    rowData: ScoreRowData
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .testTag("PlayerStatistics_Text_PlayerPosition")
                .width(16.dp),
            text = rowData.position,
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondary
        )
        if (rowData.notPlaying) {
            PlayerNotPlayReasonText(rowData.notPlayingReason)
        } else {
            rowData.rowData.forEach { statsRowData ->
                PlayerStatsText(
                    width = statsRowData.textWidth,
                    text = statsRowData.value,
                    textAlign = statsRowData.textAlign
                )
            }

        }
    }
}

@Composable
private fun PlayerNotPlayReasonText(reason: String) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlay")
            .width(72.dp),
        text = stringResource(R.string.box_score_player_dnp),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(Transparency50)
    )
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_NotPlayReason")
            .padding(start = 16.dp),
        text = reason,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary.copy(Transparency50)
    )
}

@Composable
private fun PlayerStatsText(
    width: Dp,
    text: String,
    textAlign: TextAlign
) {
    Text(
        modifier = Modifier
            .testTag("PlayerStatistics_Text_PlayerStats")
            .width(width)
            .padding(horizontal = 8.dp),
        text = text,
        textAlign = textAlign,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary
    )
}
