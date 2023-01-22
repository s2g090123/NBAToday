package com.jiachian.nbatoday.utils

import android.annotation.SuppressLint
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
) = if (condition) modify() else this

/**
 * If [condition] use [modify] else use [elseModify]
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.modifyIf(
    condition: Boolean,
    modify: Modifier.() -> Modifier,
    elseModify: Modifier.() -> Modifier
) = if (condition) modify() else elseModify()

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier): Modifier {
    return if (condition) {
        this.then(modifier)
    } else {
        this
    }
}