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
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    viewModel: GameCardViewModel,
    color: Color,
    expandable: Boolean,
) {
    val betAvailable by viewModel.betAvailable.collectAsState()
    val betDialogVisible by viewModel.betDialogVisible.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameDetail(
            modifier = Modifier.fillMaxWidth(),
            gameAndBets = viewModel.gameAndBets,
            textColor = color,
            betAvailable = betAvailable,
            onBet = { viewModel.setBetDialogVisible(true) }
        )
        if (expandable && viewModel.expandedContentVisible) {
            GameExpandedContent(
                viewModel = viewModel,
                color = color,
            )
        }
        if (betDialogVisible) {
            GameCardBetScreen(viewModel = viewModel)
        }
    }
}

@Composable
private fun GameDetail(
    modifier: Modifier = Modifier,
    gameAndBets: GameAndBets,
    textColor: Color,
    betAvailable: Boolean,
    onBet: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameTeamInfo(
            modifier = Modifier
                .testTag(GameCardTestTag.GameDetail_GameTeamInfo_Home)
                .padding(start = 16.dp),
            gameTeam = gameAndBets.game.homeTeam,
            textColor = textColor
        )
        GameStatusAndBetButton(
            gameAndBets = gameAndBets,
            textColor = textColor,
            betAvailable = betAvailable,
            onBet = onBet
        )
        GameTeamInfo(
            modifier = Modifier
                .testTag(GameCardTestTag.GameDetail_GameTeamInfo_Away)
                .padding(end = 16.dp),
            gameTeam = gameAndBets.game.awayTeam,
            textColor = textColor
        )
    }
}

@Composable
private fun GameTeamInfo(
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
                .testTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
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
                .testTag(GameCardTestTag.GameTeamInfo_Text_Score)
                .padding(top = 8.dp),
            text = gameTeam.score.toString(),
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GameExpandedContent(
    viewModel: GameCardViewModel,
    color: Color
) {
    val expanded by viewModel.expanded.collectAsState()
    Box {
        AnimatedExpand(
            modifier = Modifier
                .testTag(GameCardTestTag.GameExpandedContent_Button_Expand)
                .fillMaxWidth()
                .height(24.dp)
                .rippleClickable { viewModel.setCardExpanded(true) }
                .padding(vertical = 2.dp),
            visible = !expanded,
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
                .testTag(GameCardTestTag.GameExpandedContent_Button_Collapse)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            visible = expanded,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                GameCardLeadersInfo(
                    modifier = Modifier
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
