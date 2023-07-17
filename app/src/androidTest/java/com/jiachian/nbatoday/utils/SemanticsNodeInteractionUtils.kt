package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performScrollToNode

fun SemanticsNodeInteraction.onNodeWithTag(tag: String, index: Int): SemanticsNodeInteraction {
    try {
        assert(hasScrollAction())
        performScrollToTag(tag)
    } catch (t: Throwable) {

    }
    return onChildren()
        .filter(hasTestTag(tag))[index]
}

fun SemanticsNodeInteraction.onNodeWithTag(tag: String) = onNodeWithTag(tag, 0)

fun SemanticsNodeInteraction.performScrollToTag(tag: String) = performScrollToNode(hasTestTag(tag))