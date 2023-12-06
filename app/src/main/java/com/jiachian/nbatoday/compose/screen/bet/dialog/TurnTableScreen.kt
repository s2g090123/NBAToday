package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.compose.foundation.background
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
import com.jiachian.nbatoday.utils.color
import com.jiachian.nbatoday.utils.noRippleClickable

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
                .testTag("BetScreen_BetTurnTable")
                .fillMaxSize()
                .background("#66000000".color)
                .noRippleClickable { },
            viewModel = viewModel,
            onStart = {
                viewModel.startTurnTable(points)
            },
            onClose = { viewModel.closeTurnTable() }
        )
    }
}
