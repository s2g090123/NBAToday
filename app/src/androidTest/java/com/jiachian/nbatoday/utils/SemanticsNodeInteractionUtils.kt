package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren

fun SemanticsNodeInteraction.onNodeWithTag(tag: String, index: Int) =
    onChildren()
        .filter(hasTestTag(tag))[index]

fun SemanticsNodeInteraction.onNodeWithTag(tag: String) = onNodeWithTag(tag, 0)