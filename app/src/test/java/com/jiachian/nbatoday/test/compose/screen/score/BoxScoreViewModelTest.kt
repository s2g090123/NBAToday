package com.jiachian.nbatoday.test.compose.screen.score

import androidx.lifecycle.SavedStateHandle
import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreViewModel
import com.jiachian.nbatoday.boxscore.ui.main.event.BoxScoreUIEvent
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.main.ui.navigation.MainRoute
import com.jiachian.nbatoday.rule.SetMainDispatcherRule
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxScoreViewModelTest : BaseUnitTest() {
    @get:Rule
    val mainDispatcherRule = SetMainDispatcherRule(dispatcherProvider)

    private lateinit var viewModel: BoxScoreViewModel

    @Before
    fun setup() {
        viewModel = BoxScoreViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MainRoute.BoxScore.param to GameGenerator.getFinal().gameId)),
            boxScoreUseCase = useCaseProvider.boxScore,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `onEvent with EventReceived and check event is null`() {
        viewModel.onEvent(BoxScoreUIEvent.EventReceived)
        viewModel.state.event.assertIsNull()
    }

    @Test
    fun `check state and boxScore not found`() {
        viewModel.state.notFound.assertIsTrue()
    }
}
