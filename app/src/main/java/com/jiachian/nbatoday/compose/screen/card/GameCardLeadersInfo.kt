package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.models.local.game.GameLeaders
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.dividerPrimaryColor

@Composable
fun GameCardLeadersInfo(
    modifier: Modifier = Modifier,
    viewModel: GameCardViewModel,
    color: Color,
) {
    Column(modifier = modifier) {
        StatsLabelRow(
            modifier = Modifier.fillMaxWidth(),
            color = color
        )
        LeaderInfoRow(
            modifier = Modifier
                .testTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            player = viewModel.homeLeader,
            isGamePlayed = viewModel.isGamePlayed,
            color = color
        )
        LeaderInfoRow(
            modifier = Modifier
                .testTag(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            player = viewModel.awayLeader,
            isGamePlayed = viewModel.isGamePlayed,
            color = color
        )
    }
}

@Composable
private fun StatsLabelRow(
    modifier: Modifier = Modifier,
    color: Color
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            LeaderLabelText(
                modifier = Modifier.width(36.dp),
                text = stringResource(R.string.player_info_pts_abbr),
                color = color
            )
            LeaderLabelText(
                modifier = Modifier.width(36.dp),
                text = stringResource(R.string.player_info_reb_abbr),
                color = color
            )
            LeaderLabelText(
                modifier = Modifier.width(36.dp),
                text = stringResource(R.string.player_info_ast_abbr),
                color = color
            )
        }
        Divider(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            color = dividerPrimaryColor()
        )
    }
}

@Composable
private fun LeaderLabelText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun LeaderInfoRow(
    modifier: Modifier = Modifier,
    player: GameLeaders.GameLeader,
    isGamePlayed: Boolean,
    color: Color
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerImage(
            modifier = Modifier.size(width = 52.dp, height = 38.dp),
            playerId = player.playerId
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = player.name,
                color = color,
                fontSize = 12.sp
            )
            Text(
                modifier = Modifier.testTag(GameCardTestTag.LeaderInfoRow_Text_PlayerTitle),
                text = player.playerTitle,
                color = color,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LeaderStatsText(
            modifier = Modifier
                .testTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                .width(36.dp),
            text = (if (isGamePlayed) player.points.toInt() else player.points).toString(),
            color = color
        )
        LeaderStatsText(
            modifier = Modifier
                .testTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                .width(36.dp),
            text = (if (isGamePlayed) player.rebounds.toInt() else player.rebounds).toString(),
            color = color
        )
        LeaderStatsText(
            modifier = Modifier
                .testTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                .width(36.dp),
            text = (if (isGamePlayed) player.assists.toInt() else player.assists).toString(),
            color = color
        )
    }
}

@Composable
private fun LeaderStatsText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}