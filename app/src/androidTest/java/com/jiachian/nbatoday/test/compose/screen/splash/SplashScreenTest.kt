package com.jiachian.nbatoday.test.compose.screen.splash

import androidx.compose.material.MaterialTheme
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.common.data.Transparency25
import com.jiachian.nbatoday.navigation.TestNavigationController
import com.jiachian.nbatoday.splash.ui.SplashScreen
import com.jiachian.nbatoday.splash.ui.SplashViewModel
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Before
import org.junit.Test

class SplashScreenTest : BaseAndroidTest() {
    private lateinit var navigationController: TestNavigationController
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        viewModel = SplashViewModel(
            scheduleUseCase = useCaseProvider.schedule,
            teamUseCase = useCaseProvider.team,
            userUseCase = useCaseProvider.user,
        )
        composeTestRule.setContent {
            SplashScreen(
                state = viewModel.state,
                colors = listOf(
                    MaterialTheme.colors.secondary.copy(Transparency25),
                    MaterialTheme.colors.secondary
                ),
                navigationController = TestNavigationController().apply {
                    navigationController = this
                }
            )
        }
    }

    @Test
    fun splashScreen_navigateToHome() {
        navigationController.toHome.assertIsTrue()
    }
}
