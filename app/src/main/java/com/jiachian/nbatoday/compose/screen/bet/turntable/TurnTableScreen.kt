package com.jiachian.nbatoday.compose.screen.bet.turntable

import androidx.compose.runtime.Composable
import com.jiachian.nbatoday.compose.screen.bet.models.TurnTableUIState

@Composable
fun TurnTableScreen(
    uiState: TurnTableUIState,
    idle: (@Composable () -> Unit)?,
    asking: @Composable (TurnTableUIState.Asking) -> Unit,
    turntable: @Composable (TurnTableUIState.TurnTable) -> Unit,
    rewarded: @Composable (TurnTableUIState.Rewarded) -> Unit,
) {
    when (uiState) {
        TurnTableUIState.Idle -> idle?.invoke()
        is TurnTableUIState.Asking -> asking(uiState)
        is TurnTableUIState.TurnTable -> turntable(uiState)
        is TurnTableUIState.Rewarded -> rewarded(uiState)
    }
}
