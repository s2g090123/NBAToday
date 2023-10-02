package com.jiachian.nbatoday.compose.screen.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.RequestBetScreen
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.dividerPrimaryColor
import com.jiachian.nbatoday.utils.rippleClickable

@Composable
fun GameStatusCard(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    userData: User?,
    color: Color,
    expandable: Boolean,
    onLogin: (account: String, password: String) -> Unit,
    onRegister: (account: String, password: String) -> Unit,
    onConfirm: (gameId: String, homePoints: Long, awayPoints: Long) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val isGamePlayed by remember { derivedStateOf { gameAndBet.game.isGamePlayed } }
    val hasBet by remember(gameAndBet) {
        derivedStateOf { gameAndBet.bets.find { it.account == userData?.account } != null }
    }
    val canBet by remember(hasBet) { derivedStateOf { !isGamePlayed && !hasBet } }
    var showBetsDialog by rememberSaveable { mutableStateOf(false) }
    val leaders = if (!gameAndBet.game.isGamePlayed) gameAndBet.game.teamLeaders else gameAndBet.game.gameLeaders
    val homeLeader = leaders?.homeLeaders
    val awayLeader = leaders?.awayLeaders
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreBoard(
            modifier = Modifier.fillMaxWidth(),
            gameAndBet = gameAndBet,
            textColor = color,
            betVisible = canBet,
            onBetClick = { showBetsDialog = true }
        )
        if (expandable && homeLeader != null && awayLeader != null) {
            ExpandContent(
                homeLeader = homeLeader,
                awayLeader = awayLeader,
                isGamePlayed = isGamePlayed,
                isExpanded = isExpanded,
                color = color,
                onExpandClick = { isExpanded = it }
            )
        }
        if (showBetsDialog) {
            RequestBetScreen(
                userData = userData,
                gameAndBet = gameAndBet,
                onLogin = onLogin,
                onRegister = onRegister,
                onConfirm = onConfirm,
                onDismiss = { showBetsDialog = false }
            )
        }
    }
}

@Composable
private fun ExpandContent(
    homeLeader: GameLeaders.GameLeader,
    awayLeader: GameLeaders.GameLeader,
    isGamePlayed: Boolean,
    isExpanded: Boolean,
    color: Color,
    onExpandClick: (Boolean) -> Unit
) {
    Box {
        AnimatedVisibility(
            modifier = Modifier
                .testTag("ExpandContent_Btn_Expand")
                .fillMaxWidth()
                .height(24.dp)
                .rippleClickable { onExpandClick(true) }
                .padding(vertical = 2.dp),
            visible = !isExpanded,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_black_expand_more),
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(color),
                contentDescription = null
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .testTag("ExpandContent_Btn_Collapse")
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            visible = isExpanded,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LeaderInfo(
                    modifier = Modifier
                        .testTag("ExpandContent_LeaderInfo")
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    isGamePlayed = isGamePlayed,
                    homeLeader = homeLeader,
                    awayLeader = awayLeader,
                    color = color
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .rippleClickable { onExpandClick(false) }
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
    onBetClick: (NbaGameAndBet) -> Unit
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

@Composable
private fun GameStatusAndBetButton(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    textColor: Color,
    betVisible: Boolean,
    onBetClick: (NbaGameAndBet) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("GameStatusCard2_Text_GameStatus"),
            text = gameAndBet.game.gameStatusFormatText,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        if (betVisible) {
            IconButton(
                modifier = Modifier
                    .testTag("GameStatusCard2_Btn_Bet")
                    .padding(top = 8.dp),
                drawableRes = R.drawable.ic_black_coin,
                tint = textColor,
                onClick = { onBetClick(gameAndBet) }
            )
        }
    }
}

@Composable
private fun LeaderInfo(
    modifier: Modifier = Modifier,
    isGamePlayed: Boolean,
    homeLeader: GameLeaders.GameLeader,
    awayLeader: GameLeaders.GameLeader,
    color: Color,
) {
    Column(modifier = modifier) {
        LeaderLabel(
            modifier = Modifier.fillMaxWidth(),
            color = color
        )
        LeaderRow(
            modifier = Modifier
                .testTag("LeaderInfo_LeaderRow_Home")
                .padding(top = 8.dp)
                .fillMaxWidth(),
            player = homeLeader,
            isGamePlayed = isGamePlayed,
            color = color
        )
        LeaderRow(
            modifier = Modifier
                .testTag("LeaderInfo_LeaderRow_Away")
                .padding(top = 8.dp)
                .fillMaxWidth(),
            player = awayLeader,
            isGamePlayed = isGamePlayed,
            color = color
        )
    }
}

@Composable
private fun LeaderLabel(
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
private fun LeaderRow(
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
            playerId = player.personId
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = player.name,
                color = color,
                fontSize = 12.sp
            )
            Text(
                modifier = Modifier.testTag("LeaderRow_Text_PlayerInfo"),
                text = stringResource(
                    R.string.player_info,
                    player.teamTricode,
                    player.jerseyNum,
                    player.position
                ),
                color = color,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LeaderStatsText(
            modifier = Modifier
                .testTag("LeaderRow_Text_Pts")
                .width(36.dp),
            text = (if (isGamePlayed) player.points.toInt() else player.points).toString(),
            color = color
        )
        LeaderStatsText(
            modifier = Modifier
                .testTag("LeaderRow_Text_Reb")
                .width(36.dp),
            text = (if (isGamePlayed) player.rebounds.toInt() else player.rebounds).toString(),
            color = color
        )
        LeaderStatsText(
            modifier = Modifier
                .testTag("LeaderRow_Text_Ast")
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
