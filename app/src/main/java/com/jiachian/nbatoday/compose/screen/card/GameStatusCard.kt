package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.AnimatedExpand
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun GameStatusCard(
    modifier: Modifier = Modifier,
    viewModel: GameStatusCardViewModel,
    color: Color,
    expandable: Boolean,
) {
    val canBet by viewModel.canBet.collectAsState()
    val betDialogVisible by viewModel.betDialogVisible.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreBoard(
            modifier = Modifier.fillMaxWidth(),
            gameAndBet = viewModel.gameAndBet,
            textColor = color,
            betVisible = canBet,
            onBetClick = viewModel::showBetDialog
        )
        if (expandable && viewModel.expandContentVisible) {
            ExpandContent(
                viewModel = viewModel,
                color = color,
            )
        }
        if (betDialogVisible) {
            GameStatusCardBetScreen(viewModel = viewModel)
        }
    }
}

@Composable
private fun ExpandContent(
    viewModel: GameStatusCardViewModel,
    color: Color
) {
    val isExpanded by viewModel.isCardExpanded.collectAsState()
    Box {
        AnimatedExpand(
            modifier = Modifier
                .testTag("ExpandContent_Btn_Expand")
                .fillMaxWidth()
                .height(24.dp)
                .rippleClickable { viewModel.setCardExpanded(true) }
                .padding(vertical = 2.dp),
            visible = !isExpanded,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_expand_more),
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(color),
                contentDescription = null
            )
        }
        AnimatedExpand(
            modifier = Modifier
                .testTag("ExpandContent_Btn_Collapse")
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            visible = isExpanded,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                GameStatusCardLeaderInfo(
                    modifier = Modifier
                        .testTag("ExpandContent_LeaderInfo")
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    viewModel = viewModel,
                    color = color
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .rippleClickable { viewModel.setCardExpanded(false) }
                        .padding(vertical = 2.dp),
                    painter = painterResource(R.drawable.ic_black_collpase_more),
                    alpha = 0.6f,
                    colorFilter = ColorFilter.tint(color),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ScoreBoard(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    textColor: Color,
    betVisible: Boolean,
    onBetClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreBoard_TeamInfo_Home")
                .padding(start = 16.dp),
            gameTeam = gameAndBet.game.homeTeam,
            textColor = textColor
        )
        GameStatusAndBetButton(
            gameAndBet = gameAndBet,
            textColor = textColor,
            betVisible = betVisible,
            onBetClick = onBetClick
        )
        TeamInfo(
            modifier = Modifier
                .testTag("ScoreBoard_TeamInfo_Away")
                .padding(end = 16.dp),
            gameTeam = gameAndBet.game.awayTeam,
            textColor = textColor
        )
    }
}

@Composable
private fun TeamInfo(
    modifier: Modifier = Modifier,
    gameTeam: GameTeam,
    textColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag("TeamInfo_Text_TriCode")
                .padding(top = 16.dp),
            text = gameTeam.team.abbreviation,
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        TeamLogoImage(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(100.dp),
            team = gameTeam.team
        )
        Text(
            modifier = Modifier
                .testTag("TeamInfo_Text_Score")
                .padding(top = 8.dp),
            text = gameTeam.score.toString(),
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
