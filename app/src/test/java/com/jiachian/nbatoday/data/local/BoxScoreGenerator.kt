package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.remote.RemoteBoxScoreGenerator
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.remote.score.extensions.toBoxScore
import com.jiachian.nbatoday.utils.getOrAssert

object BoxScoreGenerator {
    fun getFinal(): BoxScore {
        return RemoteBoxScoreGenerator
            .getFinal()
            .game
            ?.toBoxScore()
            .getOrAssert()
    }
}
