package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst

fun SemanticsNodeInteraction.onNodeWithTag(tag: String) =
    onChildren()
        .filter(hasTestTag(tag))
        .onFirst()