package com.jiachian.nbatoday.game.ui

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.common.ui.AnimatedExpand
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.common.ui.TeamLogoImage
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.local.GameLeaders
import com.jiachian.nbatoday.game.data.model.local.GameTeam
import com.jiachian.nbatoday.game.ui.event.GameCardEvent
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    data: GameCardData,
    color: Color = MaterialTheme.colors.secondary,
    expandable: Boolean = false,
    onRequestLogin: () -> Unit,
    onRequestBet: (String) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val betAvailable by data.betAvailable.collectAsState(true)
    val showLogin by rememberUpdatedState(onRequestLogin)
    val showBet by rememberUpdatedState(onRequestBet)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameDetail(
            modifier = Modifier.fillMaxWidth(),
            gameAndBets = data.data,
            textColor = color,
            betAvailable = betAvailable,
            onBet = {
                coroutineScope.launch {
                    data.attemptBet()
                }
            }
        )
        if (expandable) {
            GameExpandedContent(
                expanded = data.expanded,
                gamePlayed = data.data.game.gamePlayed,
                homePlayer = data.homeLeader,
                awayPlayer = data.awayLeader,
                color = color,
                onExpandChange = data::updateExpanded,
            )
        }
    }
    LaunchedEffect(data.event) {
        data.event.collect {
            when (it) {
                is GameCardEvent.Bet -> showBet(it.gameId)
                GameCardEvent.Login -> showLogin()
                GameCardEvent.Unavailable -> showToast(context, R.string.bet_toast_already_bet_before)
            }
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
private fun GameStatusAndBetButton(
    modifier: Modifier = Modifier,
    gameAndBets: GameAndBets,
    textColor: Color,
    betAvailable: Boolean,
    onBet: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag(GameCardTestTag.GameStatusAndBetButton_Text_Status),
            text = gameAndBets.game.statusFormattedText,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (betAvailable) {
            IconButton(
                modifier = Modifier
                    .testTag(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                    .padding(top = 8.dp),
                drawableRes = R.drawable.ic_black_coin,
                tint = textColor,
                onClick = onBet
            )
        }
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
    expanded: Boolean,
    gamePlayed: Boolean,
    homePlayer: GameLeaders.GameLeader,
    awayPlayer: GameLeaders.GameLeader,
    color: Color,
    onExpandChange: (Boolean) -> Unit,
) {
    Box {
        AnimatedExpand(
            modifier = Modifier
                .testTag(GameCardTestTag.GameExpandedContent_Button_Expand)
                .fillMaxWidth()
                .height(24.dp)
                .rippleClickable { onExpandChange(true) }
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
                    gamePlayed = gamePlayed,
                    homePlayer = homePlayer,
                    awayPlayer = awayPlayer,
                    color = color
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .rippleClickable { onExpandChange(false) }
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
