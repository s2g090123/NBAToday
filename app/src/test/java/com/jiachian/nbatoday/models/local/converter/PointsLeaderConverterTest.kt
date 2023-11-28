package com.jiachian.nbatoday.models.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.converter.PointsLeaderConverter
import com.jiachian.nbatoday.models.GameLeaderFactory
import com.jiachian.nbatoday.models.local.game.Game
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PointsLeaderConverterTest {
    private val converter = PointsLeaderConverter(testGeneralGson)

    @Test
    fun from_ListNbaPointsLeaderToString_isCorrect() {
        val homePointsLeader = GameLeaderFactory.getHomePointsLeader()
        val awayPointsLeader = GameLeaderFactory.getAwayPointsLeader()
        val pointsLeader = listOf(homePointsLeader, awayPointsLeader)
        val type = object : TypeToken<List<Game.PointsLeader>>() {}.type
        val expected = testGeneralGson.toJson(pointsLeader, type)
        val actual = converter.from(pointsLeader)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringListNbaPointsLeader_isCorrect() {
        val homePointsLeader = GameLeaderFactory.getHomePointsLeader()
        val awayPointsLeader = GameLeaderFactory.getAwayPointsLeader()
        val pointsLeader = listOf(homePointsLeader, awayPointsLeader)
        val type = object : TypeToken<List<Game.PointsLeader>>() {}.type
        val actual = converter.to(testGeneralGson.toJson(pointsLeader, type))
        assertThat(actual, `is`(pointsLeader))
    }
}
