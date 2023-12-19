package com.jiachian.nbatoday.compose.screen.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
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
    fun `showWarning expects warning is true`() {
        viewModel.showWarning()
        assertThat(viewModel.warning.value, `is`(true))
    }

    @Test
    fun `hideWarning expects warning is false`() {
        viewModel.hideWarning()
        assertThat(viewModel.warning.value, `is`(false))
    }

    @Test
    fun `updateHomePoints expects homePoints is updated`() {
        viewModel.updateHomePoints(BasicNumber.toLong())
        assertThat(viewModel.homePoints.value, `is`(BasicNumber.toLong()))
    }

    @Test
    fun `updateAwayPoints expects awayPoints is updated`() {
        viewModel.updateAwayPoints(BasicNumber.toLong())
        assertThat(viewModel.awayPoints.value, `is`(BasicNumber.toLong()))
    }
}
