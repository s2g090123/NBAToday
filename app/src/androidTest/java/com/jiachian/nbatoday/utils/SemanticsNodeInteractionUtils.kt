package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performScrollTo

fun SemanticsNodeInteraction.onNodeWithTag(tag: String, index: Int): SemanticsNodeInteraction {
    return onChildren()
        .filter(hasTestTag(tag))[index]
        .also {
            try {
                it.performScrollTo()
            } catch (_: Throwable) {
            }
        }
}

fun SemanticsNodeInteraction.onNodeWithTag(tag: String) = onNodeWithTag(tag, 0)
