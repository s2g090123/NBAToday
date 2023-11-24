package com.jiachian.nbatoday.utils

import android.content.Context
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.ColorTransparency25
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.widget.FocusableColumn
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class ComposeUtilsTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun stringToColor() {
        val color = "#ffffff".color
        assertThat(color.alpha, `is`(1f))
        assertThat(color.red, `is`(1f))
        assertThat(color.green, `is`(1f))
        assertThat(color.blue, `is`(1f))
    }

    @Test
    fun dp2Px() {
        composeTestRule.setContent {
            val actual = 12.dp.toPx()
            val expected = 12 * Resources.getSystem().displayMetrics.density
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun getDividerPrimaryColor() {
        composeTestRule.setContent {
            val actual = dividerPrimaryColor()
            val expected = MaterialTheme.colors.primary.copy(ColorTransparency25)
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun getDividerSecondaryColor() {
        composeTestRule.setContent {
            val actual = dividerSecondaryColor()
            val expected = MaterialTheme.colors.secondary.copy(ColorTransparency25)
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun px2dp() {
        composeTestRule.setContent {
            val actual = 12.px2Dp()
            val expected = (12 / Resources.getSystem().displayMetrics.density).dp
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun checksIsPhone() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule.setContent {
            val actual = isPhone()
            val expected = context.resources.getBoolean(R.bool.is_phone)
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun checksIsPortrait() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        composeTestRule.setContent {
            val actual = isPortrait()
            val expected = context.resources.getBoolean(R.bool.is_portrait)
            assertThat(actual, `is`(expected))
        }
    }

    @Test
    fun checksLocalActivity() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalActivity provides composeTestRule.activity
            ) {
                assertThat(LocalActivity.current, instanceOf(ComponentActivity::class.java))
                assertThat(LocalActivity.current, notNullValue())
            }
        }
    }

    @Test
    fun focusableColumn() {
        composeTestRule.setContent {
            FocusableColumn(
                modifier = Modifier
                    .testTag("FocusableColumn")
            ) {
                Text(
                    modifier = Modifier.testTag("Text"),
                    text = "test"
                )
            }
        }
        composeTestRule
            .onNodeWithMergedTag("FocusableColumn")
            .assertHasClickAction()
        composeTestRule
            .onNodeWithMergedTag("Text")
            .assertTextEquals("test")
    }
}
