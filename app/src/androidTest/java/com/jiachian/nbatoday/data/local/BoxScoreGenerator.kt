package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.boxscore.data.model.toBoxScore
import com.jiachian.nbatoday.data.remote.RemoteBoxScoreGenerator
import com.jiachian.nbatoday.utils.getOrAssert

object BoxScoreGenerator {
    fun getFinal(): BoxScore {
        return RemoteBoxScoreGenerator
            .getFinal()
            .game
            ?.toBoxScore()
            .getOrAssert()
    }

    fun getPlaying(): BoxScore {
        return RemoteBoxScoreGenerator
            .getPlaying()
            .game
            ?.toBoxScore()
            .getOrAssert()
    }
}
