package com.jiachian.nbatoday.compose.screen.player

import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.compose.screen.player.models.PlayerStatsSorting
import com.jiachian.nbatoday.models.PlayerCareerFactory
import com.jiachian.nbatoday.models.TestRepository
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class PlayerViewModelTest {

    private lateinit var viewModel: PlayerViewModel
    private val repository = TestRepository()
    private val coroutineEnvironment = TestCoroutineEnvironment()
    private val homePlayer = PlayerCareerFactory.createHomePlayerCareer()

    @Before
    fun setup() {
        viewModel = PlayerViewModel(
            playerId = HomePlayerId,
            repository = repository,
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider
        )
    }

    @After
    fun teardown() {
        repository.clear()
    }

    @Test
    fun player_homePlayer_checksPlayerCareer() {
        viewModel.player.launchAndCollect(coroutineEnvironment)
        val playerCareer = viewModel.player.value
        assertThat(playerCareer?.playerId, `is`(homePlayer.playerId))
        assertThat(playerCareer?.info, `is`(notNullValue()))
        assertThat(playerCareer?.info?.playerName, `is`(homePlayer.info.playerName))
        assertThat(playerCareer?.info?.team?.teamName, `is`(homePlayer.info.team.teamName))
        assertThat(playerCareer?.info?.team?.abbreviation, `is`(homePlayer.info.team.abbreviation))
        assertThat(playerCareer?.info?.team?.teamId, `is`(homePlayer.info.team.teamId))
        assertThat(playerCareer?.stats?.stats, `is`(homePlayer.stats.stats))
        assertThat(playerCareer?.stats?.careerRank, `is`(homePlayer.stats.careerRank))
    }

    @Test
    fun player_homePlayer_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats.first(), viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateTimeFrameSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats.first(), viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateGamePlayedSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.GP)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateGameWinSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.W)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateLoseSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.L)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats.first(), viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateWinPercentageSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.WINP)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePointsSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PTS)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFGMSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FGM)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFGASort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FGA)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFGPSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FGP)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePM3Sort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PM3)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePA3Sort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PA3)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePP3Sort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PP3)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFTMSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FTM)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFTASort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FTA)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateFTPSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.FTP)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateOREBSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.OREB)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateDREBSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.DREB)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateREBSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.REB)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateASTSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.AST)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateTOVSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.TOV)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateSTLSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.STL)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updateBLKSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.BLK)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePFSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PF)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }

    @Test
    fun player_updatePLUSMINUSSort_checksFirstRowCareerStats() {
        viewModel.statsRowData.launchAndCollect(coroutineEnvironment)
        viewModel.updateSorting(PlayerStatsSorting.PLUSMINUS)
        val labels = viewModel.statsLabels.value
        val homeCareerStats = labels.map {
            it.getRowData(homePlayer.stats.stats[1], viewModel.sorting.value)
        }
        val careerStats = viewModel.statsRowData.value.first()
        assertThat(careerStats, `is`(homeCareerStats))
    }
}
