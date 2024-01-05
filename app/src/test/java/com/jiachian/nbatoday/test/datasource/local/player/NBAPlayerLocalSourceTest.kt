package com.jiachian.nbatoday.test.datasource.local.player

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.datasource.local.player.NBAPlayerLocalSource
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBAPlayerLocalSourceTest : BaseUnitTest() {
    private lateinit var localSource: NBAPlayerLocalSource

    @Before
    fun setup() {
        localSource = NBAPlayerLocalSource(get())
    }

    @Test
    fun `getPlayer(home) expects null`() {
        val actual = localSource.getPlayer(HomePlayerId).stateIn(null).value
        assertIsNull(actual)
    }

    @Test
    fun `insertPlayer(home) expects the player is inserted`() = launch {
        val expected = PlayerGenerator.getHome()
        localSource.insertPlayer(expected)
        val actual = localSource.getPlayer(expected.playerId).stateIn(null).value
        assertIs(actual, expected)
    }
}
