package com.jiachian.nbatoday.compose.screen.score.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.models.local.score.BoxScore

@Stable
interface BoxScoreInfoState {
    val dateString: String
    val periods: List<String>
    val boxScore: BoxScore?
}

class MutableBoxScoreInfoState : BoxScoreInfoState {
    override var dateString: String by mutableStateOf("")
    override var periods: List<String> by mutableStateOf(emptyList())
    override var boxScore: BoxScore? by mutableStateOf(null)
}
