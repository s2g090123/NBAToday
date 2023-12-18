package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(viewModel: BetViewModel) {
    val turnTablePoints by viewModel.turnTablePoints.collectAsState()
    var turnTableVisible by rememberSaveable { mutableStateOf(false) }
    NullCheckScreen(
        data = turnTablePoints,
        ifNull = null
    ) { points ->
        when (turnTableVisible) {
            true -> {
                BetTurnTable(
                    modifier = Modifier
                        .testTag(BetTestTag.TurnTableScreen)
                        .fillMaxSize(),
                    viewModel = viewModel,
                    onStart = { viewModel.startTurnTable(points) },
                    onClose = { viewModel.closeTurnTable() }
                )
            }
            false -> {
                AskTurnTableDialog(
                    points = points,
                    onContinue = { turnTableVisible = true },
                    onCancel = { viewModel.closeTurnTable() }
                )
            }
        }
    }
}
