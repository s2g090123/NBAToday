package com.jiachian.nbatoday.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import com.jiachian.nbatoday.compose.BorderColorKey

fun SemanticsNodeInteractionsProvider.onNodeWithMergedTag(
    testTag: String
): SemanticsNodeInteraction = onNode(hasTestTag(testTag), true)

fun SemanticsNodeInteractionsProvider.onNodeWithBorderColor(expectedColor: Color) =
    onNode(SemanticsMatcher.expectValue(BorderColorKey, expectedColor))