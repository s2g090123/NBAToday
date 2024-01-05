package com.jiachian.nbatoday.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.rippleClickable(enabled: Boolean = true, onClick: () -> Unit) = composed {
    clickable(
        indication = rememberRipple(bounded = true),
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
}

fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: () -> Unit) = composed {
    clickable(
        indication = null,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
}

/**
 * If [condition] use [modify]
 */
fun Modifier.modifyIf(
    condition: Boolean,
    modify: Modifier.() -> Modifier
) = if (condition) this.then(modify()) else this

fun <T> Modifier.modifyIf(
    condition: Boolean,
    value: T,
    modify: Modifier.(T) -> Modifier
) = if (condition) this.then(modify(value)) else this

fun Modifier.modifyIf(
    condition: Boolean,
    modify: Modifier.() -> Modifier,
    elseModify: Modifier.() -> Modifier
) = if (condition) this.then(modify()) else this.then(elseModify())
