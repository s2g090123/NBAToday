package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableState
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(
    state: TurnTableState,
    openTurnTable: (Win, Lose) -> Unit,
    startTurnTable: (Win, Lose) -> Unit,
    close: () -> Unit,
) {
    when (state) {
        TurnTableState.Idle -> {}
        is TurnTableState.Asking -> {
            AskTurnTableDialog(
                win = state.win,
                lose = state.lose,
                onContinue = { openTurnTable(state.win, state.lose) },
                onCancel = close
            )
        }
        is TurnTableState.TurnTable -> {
            BetTurnTable(
                modifier = Modifier
                    .testTag(BetTestTag.BetTurnTable)
                    .fillMaxSize(),
                state = state,
                onStart = { startTurnTable(state.win, state.lose) },
                onClose = close
            )
        }
        is TurnTableState.Rewarded -> {
            TurnTableRewardedDialog(
                points = state.points,
                onDismiss = close
            )
        }
    }
}
