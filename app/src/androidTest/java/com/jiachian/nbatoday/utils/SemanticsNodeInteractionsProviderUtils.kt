package com.jiachian.nbatoday.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import com.jiachian.nbatoday.compose.BorderColorKey

fun SemanticsNodeInteractionsProvider.onNodeWithMergedTag(
    testTag: String
): SemanticsNodeInteraction = onNodeWithTag(testTag, true)

fun SemanticsNodeInteractionsProvider.onAllNodesWithMergedTag(
    testTag: String
): SemanticsNodeInteractionCollection = onAllNodesWithTag(testTag, true)

fun SemanticsNodeInteractionsProvider.onDialog(): SemanticsNodeInteraction =
    onAllNodes(isDialog()).onLast()

fun SemanticsNodeInteractionsProvider.assertDialogExist(): SemanticsNodeInteractionsProvider {
    onAllNodes(isDialog()).onFirst().assertExists()
    return this
}

fun SemanticsNodeInteractionsProvider.assertDialogDoesNotExist(): SemanticsNodeInteractionsProvider {
    onAllNodes(isDialog()).assertCountEquals(0)
    return this
}

fun SemanticsNodeInteractionsProvider.onNodeWithBorderColor(expectedColor: Color) =
    onNode(SemanticsMatcher.expectValue(BorderColorKey, expectedColor))