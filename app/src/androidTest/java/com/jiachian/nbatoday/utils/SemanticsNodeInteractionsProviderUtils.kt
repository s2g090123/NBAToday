package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag

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

fun SemanticsNodeInteractionsProvider.onPopup(): SemanticsNodeInteraction =
    onAllNodes(isPopup()).onLast()

fun SemanticsNodeInteractionsProvider.assertPopupExist(): SemanticsNodeInteractionsProvider {
    onAllNodes(isPopup()).onFirst().assertExists()
    return this
}

fun SemanticsNodeInteractionsProvider.assertPopupDoesNotExist(): SemanticsNodeInteractionsProvider {
    onAllNodes(isPopup()).assertCountEquals(0)
    return this
}
