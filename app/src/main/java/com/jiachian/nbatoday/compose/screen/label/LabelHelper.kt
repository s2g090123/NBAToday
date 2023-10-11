package com.jiachian.nbatoday.compose.screen.label

import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel

object LabelHelper {
    fun createScoreLabel(): Array<ScoreLabel> {
        return ScoreLabel.values()
    }

    fun createScoreTeamLabel(): Array<ScoreTeamLabel> {
        return ScoreTeamLabel.values()
    }

    fun createScoreLeaderLabel(): Array<ScoreLeaderLabel> {
        return ScoreLeaderLabel.values()
    }
}
