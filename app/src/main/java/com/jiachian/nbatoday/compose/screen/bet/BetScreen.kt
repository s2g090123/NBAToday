package com.jiachian.nbatoday.compose.screen.bet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.widget.BackHandle
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.utils.NBAUtils
import com.jiachian.nbatoday.utils.color
import com.jiachian.nbatoday.utils.noRippleClickable
import com.jiachian.nbatoday.utils.rippleClickable
import kotlin.math.abs

private const val CrownIconRotationZ = -45f

@Composable
fun BetScreen(viewModel: BetViewModel) {
    BackHandle(onBack = viewModel::close) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .noRippleClickable { }
        ) {
            BetTop(onBackClick = viewModel::close)
            BetBody(
                modifier = Modifier
                    .testTag("bet_lc_cards")
                    .fillMaxSize(),
                viewModel = viewModel
            )
        }
        TurnTableScreen(viewModel = viewModel)
        RewardPointsScreen(viewModel = viewModel)
    }
}

@Composable
private fun TurnTableScreen(
    viewModel: BetViewModel
) {
    val askTurnTable by viewModel.askTurnTableVisible.collectAsState()
    val showTurnTable by viewModel.tryTurnTableVisible.collectAsState()
    askTurnTable?.let { betData ->
        AskTurnTableDialog(
            turnTablePoints = betData,
            onContinue = {
                viewModel.showTurnTable(it)
                viewModel.closeAskTurnTable()
            },
            onCancel = { viewModel.closeAskTurnTable() }
        )
    }
    showTurnTable?.let { betData ->
        BetTurnTable(
            modifier = Modifier
                .testTag("BetScreen_BetTurnTable")
                .fillMaxSize()
                .background("#66000000".color)
                .noRippleClickable { },
            viewModel = viewModel,
            onStart = {
                viewModel.startTurnTable(betData)
            },
            onClose = { viewModel.closeTurnTable() }
        )
    }
}

@Composable
private fun RewardPointsScreen(
    viewModel: BetViewModel
) {
    val showRewardPoints by viewModel.rewardedPointsVisible.collectAsState()
    showRewardPoints?.let { reward ->
        RewardPointDialog(
            rewardPoints = reward,
            onDismiss = { viewModel.closeRewardedPoints() }
        )
    }
}

@Composable
private fun BetTop(
    onBackClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .testTag("bet_btn_back")
            .padding(top = 8.dp, start = 8.dp),
        onClick = onBackClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_black_back),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun BetBody(
    modifier: Modifier = Modifier,
    viewModel: BetViewModel
) {
    val betsAndGames by viewModel.betAndGame.collectAsState()

    if (betsAndGames.isEmpty()) {
        BetEmptyScreen(modifier = modifier)
    } else {
        LazyColumn(modifier = modifier) {
            itemsIndexed(betsAndGames) { index, betAndGame ->
                BetCard(
                    modifier = Modifier
                        .padding(
                            top = if (index == 0) 8.dp else 16.dp,
                            bottom = if (index >= betsAndGames.size - 1) 16.dp else 0.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                        .padding(bottom = 8.dp)
                        .rippleClickable {
                            viewModel.clickBetAndGame(betAndGame)
                        },
                    betAndGame = betAndGame
                )
            }
        }
    }
}

@Composable
private fun BetEmptyScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.bet_no_record),
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun BetCard(
    modifier: Modifier = Modifier,
    betAndGame: BetAndGame
) {
    val homeTeam = betAndGame.game.homeTeam
    val awayTeam = betAndGame.game.awayTeam
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BetCardTeamInfo(
            modifier = Modifier
                .testTag("BetCard_BetCardTeamInfo_Home")
                .padding(start = 16.dp),
            team = homeTeam,
            pointsText = betAndGame.getHomePointsText(),
            pointsTextColor = betAndGame.getHomePointsTextColor(),
            isGamePlayed = betAndGame.isGamePlayed,
            isWin = betAndGame.isHomeTeamWin
        )
        Text(
            modifier = Modifier
                .testTag("betCard_text_gameStatus"),
            text = betAndGame.getBetStatusText(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
        BetCardTeamInfo(
            modifier = Modifier
                .testTag("BetCard_BetCardTeamInfo_Away")
                .padding(end = 16.dp),
            team = awayTeam,
            pointsText = betAndGame.getAwayPointsText(),
            pointsTextColor = betAndGame.getAwayPointsTextColor(),
            isGamePlayed = betAndGame.isGamePlayed,
            isWin = betAndGame.isAwayTeamWin
        )
    }
}

@Composable
private fun BetCardTeamInfo(
    modifier: Modifier = Modifier,
    team: GameTeam,
    pointsText: String,
    pointsTextColor: Color,
    isGamePlayed: Boolean,
    isWin: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag("BetCardTeamInfo_Text_Points")
                .padding(top = 8.dp),
            text = pointsText,
            color = pointsTextColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Box {
            AsyncImage(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(100.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(NBAUtils.getTeamLogoUrlById(team.team.teamId))
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                error = painterResource(team.team.logoRes),
                placeholder = painterResource(team.team.logoRes),
                contentDescription = null
            )
            if (isWin) {
                Icon(
                    modifier = Modifier
                        .testTag("BetCardTeamInfo_Icon_Win")
                        .size(32.dp)
                        .graphicsLayer {
                            translationX = -6.dp.toPx()
                            translationY = -14.dp.toPx()
                            rotationZ = CrownIconRotationZ
                        },
                    painter = painterResource(R.drawable.ic_black_crown),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        if (isGamePlayed) {
            Text(
                modifier = Modifier
                    .testTag("BetCardTeamInfo_Text_Scores")
                    .padding(top = 8.dp),
                text = team.score.toString(),
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RewardPointDialog(
    rewardPoints: Long,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag("RewardPointDialog_dialog"),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.testTag("RewardPointDialog_text_title"),
                text = stringResource(
                    if (rewardPoints >= 0) R.string.bet_reward_win_title
                    else R.string.bet_reward_lose_title
                ),
                color = MaterialTheme.colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Text(
                modifier = Modifier.testTag("RewardPointDialog_text_body"),
                text = stringResource(
                    if (rewardPoints >= 0) R.string.bet_reward_win_text else R.string.bet_reward_lose_text,
                    abs(rewardPoints)
                ),
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier
                        .testTag("RewardPointDialog_text_ok")
                        .padding(bottom = 8.dp, end = 8.dp)
                        .rippleClickable { onDismiss() }
                        .padding(10.dp),
                    text = stringResource(R.string.bet_reward_ok),
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
