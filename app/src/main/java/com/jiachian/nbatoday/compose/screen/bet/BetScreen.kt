package com.jiachian.nbatoday.compose.screen.bet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.screen.bet.turntable.TurnTableRewardedDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.TurnTableScreen
import com.jiachian.nbatoday.compose.screen.bet.widgets.BetCard
import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.widget.IconButton
import com.jiachian.nbatoday.compose.widget.LoadingScreen
import com.jiachian.nbatoday.compose.widget.UIStateScreen
import com.jiachian.nbatoday.models.local.bet.BetAndGame
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.testing.testtag.BetTestTag.BetScreen_BetBody_Loading
import com.jiachian.nbatoday.utils.rippleClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun BetScreen(
    viewModel: BetViewModel = koinViewModel(),
    navigateToBoxScore: (gameId: String) -> Unit,
    navigateToTeam: (teamId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.betsAndGamesState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        Column {
            BetBackButton(onBack = onBack)
            BetBody(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onClickBet = {
                    when (it.game.gameStatus) {
                        GameStatus.COMING_SOON -> navigateToTeam(it.game.homeTeamId)
                        GameStatus.PLAYING -> navigateToBoxScore(it.game.gameId)
                        GameStatus.FINAL -> viewModel.settleBet(it)
                    }
                }
            )
        }
        TurnTableScreen(
            uiState = viewModel.turnTableUIState,
            idle = null,
            asking = {
                AskTurnTableDialog(
                    win = it.win,
                    lose = it.lose,
                    onContinue = { viewModel.showTurnTable(it.win, it.lose) },
                    onCancel = viewModel::closeTurnTable
                )
            },
            turntable = {
                BetTurnTable(
                    modifier = Modifier
                        .testTag(BetTestTag.BetTurnTable)
                        .fillMaxSize(),
                    uiState = it,
                    onStart = { viewModel.startTurnTable(it.win, it.lose) },
                    onClose = viewModel::closeTurnTable
                )
            },
            rewarded = {
                TurnTableRewardedDialog(
                    points = it.points,
                    onDismiss = viewModel::closeTurnTable
                )
            }
        )
    }
}

@Composable
private fun BetBackButton(onBack: () -> Unit) {
    IconButton(
        modifier = Modifier
            .testTag(BetTestTag.BetScreen_BetTop_Button_Back)
            .padding(top = 8.dp, start = 8.dp),
        drawableRes = R.drawable.ic_black_back,
        tint = MaterialTheme.colors.secondary,
        onClick = onBack
    )
}

@Composable
private fun BetBody(
    modifier: Modifier = Modifier,
    uiState: UIState<List<BetAndGame>>,
    onClickBet: (BetAndGame) -> Unit,
) {
    UIStateScreen(
        state = uiState,
        loading = {
            LoadingScreen(
                modifier = Modifier
                    .testTag(BetScreen_BetBody_Loading)
                    .then(modifier),
                color = MaterialTheme.colors.secondary
            )
        },
        ifNull = { BetEmptyScreen(modifier = modifier) },
    ) { list ->
        when {
            list.isEmpty() -> BetEmptyScreen(modifier = modifier)
            else -> {
                LazyColumn(modifier = modifier) {
                    itemsIndexed(
                        list,
                        key = { _, item -> item.bet.betId }
                    ) { index, betAndGame ->
                        BetCard(
                            modifier = Modifier
                                .testTag(BetTestTag.BetScreen_BetBody_BetCard)
                                .padding(
                                    top = if (index == 0) 8.dp else 16.dp,
                                    bottom = if (index >= list.size - 1) 16.dp else 0.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.secondary)
                                .padding(bottom = 8.dp)
                                .rippleClickable { onClickBet(betAndGame) },
                            betAndGame = betAndGame
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BetEmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .testTag(BetTestTag.BetScreen_BetEmptyScreen)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.bet_no_record),
            fontSize = 18.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}
