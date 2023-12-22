package com.jiachian.nbatoday.compose.screen.player.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoTableData
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.testing.testtag.PlayerTestTag
import com.jiachian.nbatoday.utils.modifyIf

private const val PlayerImageAspectRatio = 1.36f

fun LazyListScope.playerInfo(
    player: Player,
    tableData: PlayerInfoTableData?,
) {
    item {
        TeamAndPlayerImage(player = player)
    }
    item {
        PlayerTitle(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            player = player,
        )
    }
    tableData?.rowData?.let {
        itemsIndexed(tableData.rowData) { index, rowData ->
            PlayerInfoRow(
                modifier = Modifier
                    .modifyIf(index == 0) { padding(top = 8.dp) }
                    .fillMaxWidth(),
                rowData = rowData,
                topDivider = index < tableData.rowData.size - 1,
                bottomDivider = index > 0,
            )
        }
    }
}

@Composable
private fun TeamAndPlayerImage(
    modifier: Modifier = Modifier,
    player: Player,
) {
    Row(modifier = modifier) {
        TeamLogoImage(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .aspectRatio(1f),
            team = player.info.team
        )
        PlayerImage(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .weight(2f)
                .aspectRatio(PlayerImageAspectRatio),
            playerId = player.playerId
        )
    }
}

@Composable
private fun PlayerTitle(
    modifier: Modifier = Modifier,
    player: Player,
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.testTag(PlayerTestTag.PlayerTitle_Text_Detail),
                text = player.info.detail,
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                modifier = Modifier.testTag(PlayerTestTag.PlayerTitle_Text_Name),
                text = player.info.playerName,
                fontSize = 24.sp,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Bold
            )
        }
        if (player.info.isGreatest75) {
            Image(
                modifier = Modifier
                    .testTag(PlayerTestTag.PlayerTitle_Image_Greatest)
                    .size(58.dp, 48.dp),
                painter = painterResource(R.drawable.ic_nba_75th_logo),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PlayerInfoRow(
    modifier: Modifier = Modifier,
    rowData: PlayerInfoTableData.RowData,
    topDivider: Boolean,
    bottomDivider: Boolean
) {
    Column(modifier = modifier) {
        if (topDivider) {
            Divider(color = MaterialTheme.colors.secondaryVariant)
        }
        PlayerInfoRowContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            rowData = rowData,
        )
        if (bottomDivider) {
            Divider(color = MaterialTheme.colors.secondaryVariant)
        }
    }
}

@Composable
private fun PlayerInfoRowContent(
    modifier: Modifier = Modifier,
    rowData: PlayerInfoTableData.RowData,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        rowData.data.forEachIndexed { index, data ->
            PlayerInfoBox(
                modifier = Modifier.weight(1f),
                title = stringResource(data.titleRes),
                value = data.value
            )
            if (index < rowData.data.size - 1) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}

@Composable
private fun PlayerInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.secondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.testTag(PlayerTestTag.PlayerInfoBox_Text_Value),
                text = value,
                color = MaterialTheme.colors.secondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
