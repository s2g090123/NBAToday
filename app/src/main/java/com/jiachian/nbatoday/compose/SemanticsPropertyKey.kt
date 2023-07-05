package com.jiachian.nbatoday.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val BorderColorKey = SemanticsPropertyKey<Color>("BorderColor")
var SemanticsPropertyReceiver.borderColor by BorderColorKey