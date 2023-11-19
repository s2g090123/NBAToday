package com.jiachian.nbatoday.compose.screen.card

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.account.LoginDialog
import com.jiachian.nbatoday.compose.screen.bet.BetDialog
import com.jiachian.nbatoday.compose.widget.AnimatedExpand
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.PlayerImage
import com.jiachian.nbatoday.compose.widget.TeamLogoImage
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.utils.dividerPrimaryColor
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast

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
            RequestBetScreen(viewModel = viewModel)
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
                LeaderInfo(
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

@Composable
private fun GameStatusAndBetButton(
    modifier: Modifier = Modifier,
    gameAndBet: NbaGameAndBet,
    textColor: Color,
    betVisible: Boolean,
    onBetClick: () -> Unit
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
                onClick = onBetClick
            )
        }
    }
}

@Composable
private fun LeaderInfo(
    modifier: Modifier = Modifier,
    viewModel: GameStatusCardViewModel,
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
            player = viewModel.homeLeader,
            isGamePlayed = viewModel.isGamePlayed,
            color = color
        )
        LeaderRow(
            modifier = Modifier
                .testTag("LeaderInfo_LeaderRow_Away")
                .padding(top = 8.dp)
                .fillMaxWidth(),
            player = viewModel.awayLeader,
            isGamePlayed = viewModel.isGamePlayed,
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

@Composable
private fun RequestBetScreen(
    viewModel: GameStatusCardViewModel
) {
    val context = LocalContext.current
    val isLogin by viewModel.isLogin.collectAsState()
    val hasBet by viewModel.hasBet.collectAsState()
    if (!isLogin) {
        LoginDialog(
            onLoginClicked = viewModel::login,
            onRegisterClicked = viewModel::register,
            onDismiss = {}
        )
    } else if (hasBet) {
        showToast(context, R.string.bet_toast_already_bet_before)
    } else {
        BetDialog(
            viewModel = viewModel.createBetDialogViewModel(),
            onConfirm = viewModel::bet,
            onDismiss = viewModel::hideBetDialog
        )
    }
}
