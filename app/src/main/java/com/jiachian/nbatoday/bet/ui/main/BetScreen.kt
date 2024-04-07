package com.jiachian.nbatoday.bet.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.bet.data.model.local.BetAndGame
import com.jiachian.nbatoday.bet.ui.main.error.BetError
import com.jiachian.nbatoday.bet.ui.main.event.BetDataEvent
import com.jiachian.nbatoday.bet.ui.main.event.BetUIEvent
import com.jiachian.nbatoday.bet.ui.main.state.BetState
import com.jiachian.nbatoday.bet.ui.turntable.TurnTableScreen
import com.jiachian.nbatoday.common.ui.IconButton
import com.jiachian.nbatoday.game.data.model.local.GameStatus
import com.jiachian.nbatoday.main.ui.navigation.NavigationController
import com.jiachian.nbatoday.testing.testtag.BetTestTag
import com.jiachian.nbatoday.utils.rippleClickable
import com.jiachian.nbatoday.utils.showToast

@Composable
fun BetScreen(
    state: BetState,
    onEvent: (BetUIEvent) -> Unit,
    navigationController: NavigationController,
) {
    val context = LocalContext.current
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            IconButton(
                modifier = Modifier
                    .testTag(BetTestTag.BetScreen_BetTop_Button_Back)
                    .padding(top = 8.dp, start = 8.dp),
                drawableRes = R.drawable.ic_black_back,
                tint = MaterialTheme.colors.secondary,
                onClick = navigationController::back
            )
        }
    ) { padding ->
        BetContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = state,
            onClickItem = {
                when (it.game.gameStatus) {
                    GameStatus.COMING_SOON -> {
                        navigationController.navigateToTeam(it.game.homeTeamId)
                    }
                    GameStatus.PLAYING -> {
                        navigationController.navigateToBoxScore(it.game.gameId)
                    }
                    GameStatus.FINAL -> onEvent(BetUIEvent.Settle(it))
                }
            }
        )
        TurnTableScreen(
            state = state.turnTable,
            onOpenTurnTable = { win, lose ->
                onEvent(BetUIEvent.OpenTurnTable(win, lose))
            },
            onStartTurnTable = { win, lose ->
                onEvent(BetUIEvent.StartTurnTable(win, lose))
            },
            onClose = { onEvent(BetUIEvent.CloseTurnTable) },
        )
    }
    LaunchedEffect(state.event) {
        state.event?.let { event ->
            when (event) {
                is BetDataEvent.Error -> {
                    when (event.error) {
                        BetError.ADD_POINTS_FAILED,
                        BetError.USER_NOT_LOGIN -> showToast(context, event.error.message)
                    }
                }
            }
            onEvent(BetUIEvent.EventReceived)
        }
    }
}

@Composable
private fun BetContent(
    state: BetState,
    onClickItem: (BetAndGame) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.secondary,
            )
        } else {
            BetList(
                modifier = Modifier.fillMaxSize(),
                list = state.games,
                onClickItem = onClickItem
            )
        }
    }
}

@Composable
private fun BetList(
    list: List<BetAndGame>,
    onClickItem: (BetAndGame) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (list.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.testTag(BetTestTag.BetScreen_BetEmptyText),
                text = stringResource(R.string.bet_no_record),
                fontSize = 18.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    } else {
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
                        .rippleClickable { onClickItem(betAndGame) }
                        .padding(bottom = 8.dp),
                    betAndGame = betAndGame
                )
            }
        }
    }
}
