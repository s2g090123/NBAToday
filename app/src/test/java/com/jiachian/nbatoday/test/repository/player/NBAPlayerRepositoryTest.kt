package com.jiachian.nbatoday.test.repository.player

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.database.dao.TestPlayerDao
import com.jiachian.nbatoday.player.data.NBAPlayerRepository
import com.jiachian.nbatoday.service.TestPlayerService
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBAPlayerRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBAPlayerRepository

    @Before
    fun setup() {
        repository = NBAPlayerRepository(
            dao = TestPlayerDao(dataHolder),
            service = TestPlayerService(),
        )
    }

    @Test
    fun `addPlayer and check player is added`() = runTest {
        val player = PlayerGenerator.getHome()
        repository.addPlayer(player.playerId)
        dataHolder.players.value.assertIsTrue { it.contains(player) }
    }

    @Test
    fun `getPlayer and check player is correct`() = runTest {
        val player = PlayerGenerator.getHome()
        repository.addPlayer(player.playerId)
        repository.getPlayer(player.playerId)
            .stateIn(null)
            .value
            .assertIs(player)
    }
}
