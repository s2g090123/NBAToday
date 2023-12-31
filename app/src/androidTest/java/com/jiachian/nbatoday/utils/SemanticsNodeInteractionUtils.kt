package com.jiachian.nbatoday.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performScrollTo

fun SemanticsNodeInteraction.onNodeWithTag(tag: String, index: Int): SemanticsNodeInteraction {
    return findNode(tag, index) ?: onNodeWithTag2(tag, index)
}

fun SemanticsNodeInteraction.onNodeWithTag2(tag: String, index: Int): SemanticsNodeInteraction {
    return onChildren()
        .filter(hasTestTag(tag))[index]
        .also {
            try {
                it.performScrollTo()
            } catch (_: Throwable) {
            }
        }
}

fun SemanticsNodeInteraction.onNodeWithTag2(tag: String) = onNodeWithTag2(tag, 0)

fun SemanticsNodeInteraction.onNodeWithTag(tag: String) = onNodeWithTag(tag, 0)

private fun SemanticsNodeInteraction.findNode(tag: String, index: Int): SemanticsNodeInteraction? {
    val children = onChildren()
    val finding = try {
        children
            .filter(hasTestTag(tag))[index]
            .assertExists()
            .also {
                try {
                    it.performScrollTo()
                } catch (_: Throwable) {
                }
            }
    } catch (e: AssertionError) {
        null
    }
    return finding ?: run {
        val size = children.fetchSemanticsNodes().size
        repeat(size) {
            children[it].findNode(tag, index)?.also { node ->
                return@run node
            }
        }
        null
    }
}
