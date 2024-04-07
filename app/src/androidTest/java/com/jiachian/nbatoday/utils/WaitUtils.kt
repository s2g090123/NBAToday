package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeTestRule

fun ComposeTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMs: Long = 1_000L,
    useUnMergedTree: Boolean = true,
) {
    return waitUntil(timeoutMillis = timeoutMs) {
        this.onAllNodes(matcher, useUnMergedTree)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}
