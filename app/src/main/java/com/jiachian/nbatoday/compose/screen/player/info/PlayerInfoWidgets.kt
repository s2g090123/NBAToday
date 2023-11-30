package com.jiachian.nbatoday.compose.screen.player.info

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.player.models.PlayerInfoRowData
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.team.NBATeam

private const val PlayerImageAspectRatio = 1.36f

@Composable
fun PlayerCareerInfo(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
) {
    val playerCareer by viewModel.playerCareer.collectAsState()
    playerCareer?.let {
        Column(modifier = modifier) {
            TeamAndPlayerImage(
                modifier = Modifier.fillMaxWidth(),
                team = it.info.team,
                playerId = it.playerId
            )
            PlayerTitle(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                playerInfo = it.info
            )
            PlayerInfoTable(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun TeamAndPlayerImage(
    modifier: Modifier = Modifier,
    team: NBATeam,
    playerId: Int
) {
    Row(modifier = modifier) {
        TeamLogoImage(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .aspectRatio(1f),
            team = team
        )
        PlayerImage(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .weight(2f)
                .aspectRatio(PlayerImageAspectRatio),
            playerId = playerId
        )
    }
}

@Composable
private fun PlayerTitle(
    modifier: Modifier = Modifier,
    playerInfo: Player.PlayerInfo
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerInfo"),
                text = stringResource(
                    R.string.player_career_info,
                    playerInfo.team.location,
                    playerInfo.team.teamName,
                    playerInfo.jersey,
                    playerInfo.position
                ),
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                modifier = Modifier.testTag("PlayerCareerInfo_Text_PlayerName"),
                text = playerInfo.playerName,
                fontSize = 24.sp,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Bold
            )
        }
        if (playerInfo.isGreatest75) {
            Image(
                modifier = Modifier
                    .testTag("PlayerCareerInfo_Image_Greatest75")
                    .size(58.dp, 48.dp),
                painter = painterResource(R.drawable.ic_nba_75th_logo),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PlayerInfoTable(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
) {
    val tableData by viewModel.playerInfoTableData.collectAsState()
    tableData?.let {
        Column(modifier = modifier) {
            PlayerInfoRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                rowData = it.firstRowData,
                topDividerVisible = true,
                bottomDividerVisible = false
            )
            PlayerInfoRow(
                modifier = Modifier.fillMaxWidth(),
                rowData = it.secondRowData,
                topDividerVisible = true,
                bottomDividerVisible = true
            )
            PlayerInfoRow(
                modifier = Modifier.fillMaxWidth(),
                rowData = it.thirdRowData,
                topDividerVisible = false,
                bottomDividerVisible = true
            )
        }
    }
}

@Composable
private fun PlayerInfoRow(
    modifier: Modifier = Modifier,
    rowData: PlayerInfoRowData,
    topDividerVisible: Boolean,
    bottomDividerVisible: Boolean
) {
    Column(modifier = modifier) {
        if (topDividerVisible) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        PlayerInfoRowContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            rowData = rowData,
        )
        if (bottomDividerVisible) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
private fun PlayerInfoRowContent(
    modifier: Modifier = Modifier,
    rowData: PlayerInfoRowData,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = rowData.firstContent.first,
            value = rowData.firstContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = rowData.secondContent.first,
            value = rowData.secondContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = rowData.thirdContent.first,
            value = rowData.thirdContent.second
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        PlayerInfoBox(
            modifier = Modifier.weight(1f),
            title = rowData.forthContent.first,
            value = rowData.forthContent.second
        )
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
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 16.dp, horizontal = 8.dp),
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
                modifier = Modifier.testTag("PlayerInfoBox_Text_Value"),
                text = value,
                color = MaterialTheme.colors.secondaryVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
