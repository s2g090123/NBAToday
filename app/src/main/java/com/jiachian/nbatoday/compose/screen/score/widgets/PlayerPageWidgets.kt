package com.jiachian.nbatoday.compose.screen.score.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerLabel
import com.jiachian.nbatoday.compose.screen.score.models.BoxScorePlayerRowData
import com.jiachian.nbatoday.testing.testtag.BoxScoreTestTag
import com.jiachian.nbatoday.utils.dividerSecondaryColor
import com.jiachian.nbatoday.utils.rippleClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScorePlayerPage(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    players: List<BoxScorePlayerRowData>,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    LazyColumn(modifier = modifier) {
        stickyHeader {
            ScorePlayerLabelScrollableRow(
                viewModel = viewModel,
                scrollState = scrollState
            )
        }
        items(players) { rowData ->
            ScorePlayerScrollableRow(
                rowData = rowData,
                scrollState = scrollState,
                onClickPlayer = onClickPlayer
            )
        }
    }
}

@Composable
private fun ScorePlayerLabelScrollableRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    scrollState: ScrollState,
) {
    Column(modifier = Modifier.background(MaterialTheme.colors.primary)) {
        ScorePlayerLabelRow(
            modifier = modifier,
            viewModel = viewModel,
            scrollState = scrollState,
        )
        Divider(
            color = dividerSecondaryColor(),
            thickness = 3.dp,
        )
    }
}

@Composable
private fun ScorePlayerLabelRow(
    modifier: Modifier = Modifier,
    viewModel: BoxScoreViewModel,
    scrollState: ScrollState,
) {
    val selectedLabel by viewModel.selectedPlayerLabel.collectAsState()
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .size(120.dp, 40.dp)
                .background(MaterialTheme.colors.primary)
                .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
            text = stringResource(R.string.box_score_label_player),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            Spacer(modifier = Modifier.width(40.dp))
            viewModel.playerLabels.forEach { label ->
                ScorePlayerLabel(
                    label = label,
                    popup = label == selectedLabel,
                    onClick = { viewModel.selectPlayerLabel(label) },
                    onDismiss = { viewModel.selectPlayerLabel(null) }
                )
            }
        }
    }
}

@Composable
private fun ScorePlayerLabel(
    label: BoxScorePlayerLabel,
    popup: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag(BoxScoreTestTag.ScorePlayerLabel)
            .size(label.width, 40.dp)
            .rippleClickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = stringResource(label.textRes),
            textAlign = label.align,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.secondary
        )
        if (popup) {
            ScorePlayerLabelPopup(
                text = stringResource(label.infoRes),
                onDismiss = { onDismiss() }
            )
        }
    }
}

@Composable
private fun ScorePlayerLabelPopup(
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
                    .testTag(BoxScoreTestTag.ScorePlayerLabelPopup_Text_About)
                    .padding(8.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Composable
private fun ScorePlayerScrollableRow(
    modifier: Modifier = Modifier,
    rowData: BoxScorePlayerRowData,
    scrollState: ScrollState,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    ScorePlayerRow(
        modifier = modifier,
        scrollState = scrollState,
        rowData = rowData,
        onClickPlayer = onClickPlayer
    )
    Divider(color = dividerSecondaryColor())
}

@Composable
private fun ScorePlayerRow(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    rowData: BoxScorePlayerRowData,
    onClickPlayer: (playerId: Int) -> Unit,
) {
    Row(modifier = modifier) {
        ScorePlayerNameText(
            modifier = Modifier
                .testTag(BoxScoreTestTag.ScorePlayerRow_ScorePlayerNameText)
                .size(120.dp, 40.dp)
                .rippleClickable { onClickPlayer(rowData.player.playerId) }
                .padding(top = 8.dp, bottom = 8.dp, start = 4.dp),
            playerName = rowData.player.nameAbbr,
        )
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            Text(
                modifier = Modifier
                    .testTag(BoxScoreTestTag.ScorePlayerRow_Text_Position)
                    .size(40.dp)
                    .padding(8.dp),
                text = if (rowData.player.starter) rowData.player.position else "",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondary,
            )
            rowData.data.forEach { data ->
                ScorePlayerStatsText(data = data)
            }
        }
    }
}

@Composable
private fun ScorePlayerNameText(
    modifier: Modifier = Modifier,
    playerName: String,
) {
    Text(
        modifier = modifier,
        text = playerName,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun ScorePlayerStatsText(data: BoxScorePlayerRowData.Data) {
    Text(
        modifier = Modifier
            .testTag(BoxScoreTestTag.ScorePlayerStatsText)
            .size(data.width, 40.dp)
            .padding(8.dp),
        text = data.value,
        textAlign = data.align,
        fontSize = 16.sp,
        color = MaterialTheme.colors.secondary,
    )
}
