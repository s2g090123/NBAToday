package com.jiachian.nbatoday.test.compose.screen.state

import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsNull
import com.jiachian.nbatoday.utils.assertIsTrue
import org.junit.Test

class UIStateTest {
    @Test
    fun `loading with UIState#Loading expects true`() {
        val state = UIState.Loading<String>()
        assertIsTrue(state.loading)
    }

    @Test
    fun `loading with UIState#Loaded expects false`() {
        val state = UIState.Loaded("")
        assertIsFalse(state.loading)
    }

    @Test
    fun `getDataOrNull() with UIState#Loading expects null`() {
        val state = UIState.Loading<String>()
        assertIsNull(state.getDataOrNull())
    }

    @Test
    fun `getDataOrNull() with UIState#Loaded expects correct`() {
        val state = UIState.Loaded("")
        assertIs(state.getDataOrNull(), "")
    }
}
