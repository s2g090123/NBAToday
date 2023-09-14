package com.jiachian.nbatoday.data.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.local.NbaGame
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
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        val expected = testGeneralGson.toJson(pointsLeader, type)
        val actual = converter.from(pointsLeader)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringListNbaPointsLeader_isCorrect() {
        val homePointsLeader = GameLeaderFactory.getHomePointsLeader()
        val awayPointsLeader = GameLeaderFactory.getAwayPointsLeader()
        val pointsLeader = listOf(homePointsLeader, awayPointsLeader)
        val type = object : TypeToken<List<NbaGame.NbaPointsLeader>>() {}.type
        val actual = converter.to(testGeneralGson.toJson(pointsLeader, type))
        assertThat(actual, `is`(pointsLeader))
    }
}
