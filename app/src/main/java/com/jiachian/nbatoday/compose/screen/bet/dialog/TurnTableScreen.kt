package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(viewModel: BetViewModel) {
    val askTurnTable by viewModel.askTurnTableVisible.collectAsState()
    val showTurnTable by viewModel.tryTurnTableVisible.collectAsState()
    NullCheckScreen(
        data = askTurnTable,
        ifNull = {}
    ) { points ->
        AskTurnTableDialog(
            turnTablePoints = points,
            onContinue = {
                viewModel.showTurnTable(it)
                viewModel.closeAskTurnTable()
            },
            onCancel = { viewModel.closeAskTurnTable() }
        )
    }
    NullCheckScreen(
        data = showTurnTable,
        ifNull = {}
    ) { points ->
        BetTurnTable(
            modifier = Modifier
                .testTag(BetTestTag.TurnTableScreen)
                .fillMaxSize(),
            viewModel = viewModel,
            onStart = { viewModel.startTurnTable(points) },
            onClose = { viewModel.closeTurnTable() }
        )
    }
}
