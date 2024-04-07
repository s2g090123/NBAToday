package com.jiachian.nbatoday.bet.ui.turntable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jiachian.nbatoday.bet.ui.turntable.model.Lose
import com.jiachian.nbatoday.bet.ui.turntable.model.TurnTableStatus
import com.jiachian.nbatoday.bet.ui.turntable.model.Win
import com.jiachian.nbatoday.bet.ui.turntable.state.TurnTableState
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(
    state: TurnTableState,
    onOpenTurnTable: (Win, Lose) -> Unit,
    onStartTurnTable: (Win, Lose) -> Unit,
    onClose: () -> Unit,
) {
    when (val status = state.status) {
        TurnTableStatus.Idle -> Unit
        is TurnTableStatus.Asking -> {
            AskTurnTableDialog(
                win = status.win,
                lose = status.lose,
                onContinue = { onOpenTurnTable(status.win, status.lose) },
                onCancel = onClose
            )
        }
        is TurnTableStatus.TurnTable -> {
            BetTurnTable(
                modifier = Modifier
                    .testTag(BetTestTag.BetTurnTable)
                    .fillMaxSize(),
                status = status,
                onStart = { onStartTurnTable(status.win, status.lose) },
                onClose = onClose
            )
        }
        is TurnTableStatus.Rewarded -> {
            TurnTableRewardedDialog(
                points = status.points,
                onDismiss = onClose
            )
        }
    }
}
