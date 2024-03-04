package com.jiachian.nbatoday.compose.screen.bet.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.turntable.AskTurnTableDialog
import com.jiachian.nbatoday.compose.screen.bet.turntable.BetTurnTable
import com.jiachian.nbatoday.compose.widget.NullCheckScreen
import com.jiachian.nbatoday.testing.testtag.BetTestTag

@Composable
fun TurnTableScreen(viewModel: BetViewModel) {
    val turnTablePoints by viewModel.turnTablePoints.collectAsStateWithLifecycle()
    val turnTableVisible by viewModel.turnTableVisible.collectAsStateWithLifecycle()
    NullCheckScreen(
        data = turnTablePoints,
        ifNull = null
    ) { points ->
        when (turnTableVisible) {
            true -> {
                BetTurnTable(
                    modifier = Modifier
                        .testTag(BetTestTag.BetTurnTable)
                        .fillMaxSize(),
                    viewModel = viewModel,
                    onStart = { viewModel.startTurnTable(points) },
                    onClose = { viewModel.closeTurnTable() }
                )
            }
            false -> {
                AskTurnTableDialog(
                    points = points,
                    onContinue = { viewModel.showTurnTable() },
                    onCancel = { viewModel.closeTurnTable() }
                )
            }
        }
    }
}
