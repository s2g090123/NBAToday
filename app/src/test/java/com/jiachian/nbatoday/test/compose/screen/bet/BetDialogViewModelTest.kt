package com.jiachian.nbatoday.test.compose.screen.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.compose.screen.bet.BetDialogViewModel
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Before
import org.junit.Test

class BetDialogViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BetDialogViewModel

    @Before
    fun setup() {
        viewModel = BetDialogViewModel(
            gameAndBets = GameAndBetsGenerator.getFinal(),
            userPoints = UserPoints,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `showWarning() expects warning is true`() {
        viewModel.showWarning()
        assertIs(viewModel.warning.value, true)
    }

    @Test
    fun `hideWarning() expects warning is false`() {
        viewModel.hideWarning()
        assertIs(viewModel.warning.value, false)
    }

    @Test
    fun `updateHomePoints() expects homePoints is updated`() {
        viewModel.updateHomePoints(BasicNumber.toLong())
        assertIs(viewModel.homePoints.value, BasicNumber.toLong())
    }

    @Test
    fun `updateAwayPoints() expects awayPoints is updated`() {
        viewModel.updateAwayPoints(BasicNumber.toLong())
        assertIs(viewModel.awayPoints.value, BasicNumber.toLong())
    }
}
