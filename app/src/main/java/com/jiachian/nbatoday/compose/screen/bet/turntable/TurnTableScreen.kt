package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jiachian.nbatoday.compose.screen.bet.models.Lose
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableState
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableStatus
import com.jiachian.nbatoday.compose.screen.bet.models.Win
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(
    state: TurnTableState,
    openTurnTable: (Win, Lose) -> Unit,
    startTurnTable: (Win, Lose) -> Unit,
    close: () -> Unit,
) {
    when (val status = state.status) {
        TurnTableStatus.Idle -> Unit
        is TurnTableStatus.Asking -> {
            AskTurnTableDialog(
                win = status.win,
                lose = status.lose,
                onContinue = { openTurnTable(status.win, status.lose) },
                onCancel = close
            )
        }
        is TurnTableStatus.TurnTable -> {
            BetTurnTable(
                modifier = Modifier
                    .testTag(BetTestTag.BetTurnTable)
                    .fillMaxSize(),
                status = status,
                onStart = { startTurnTable(status.win, status.lose) },
                onClose = close
            )
        }
        is TurnTableStatus.Rewarded -> {
            TurnTableRewardedDialog(
                points = status.points,
                onDismiss = close
            )
        }
    }
}
