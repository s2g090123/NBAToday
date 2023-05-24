package com.jiachian.nbatoday.compose.screen.player

import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.data.PlayerCareerFactory
import com.jiachian.nbatoday.data.TestRepository
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import com.jiachian.nbatoday.utils.launchAndCollect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerInfoViewModelTest {

    private lateinit var viewModel: PlayerInfoViewModel
    private val repository = TestRepository()
    private val coroutineEnvironment = TestCoroutineEnvironment()
    private val homePlayer = PlayerCareerFactory.createHomePlayerCareer()

    @Before
    fun setup() {
        viewModel = PlayerInfoViewModel(
            playerId = HOME_PLAYER_ID,
            repository = repository,
            dispatcherProvider = coroutineEnvironment.testDispatcherProvider,
            coroutineScope = coroutineEnvironment.testScope
        )
    }

    @Test
    fun player_homePlayer_checksPlayerCareer() {
        viewModel.playerCareer.launchAndCollect(coroutineEnvironment)
        val playerCareer = viewModel.playerCareer.value
        assertThat(playerCareer?.personId, `is`(homePlayer.personId))
        assertThat(playerCareer?.info, `is`(notNullValue()))
        assertThat(playerCareer?.info?.playerName, `is`(homePlayer.info.playerName))
        assertThat(playerCareer?.info?.teamName, `is`(homePlayer.info.teamName))
        assertThat(playerCareer?.info?.teamNameAbbr, `is`(homePlayer.info.teamNameAbbr))
        assertThat(playerCareer?.info?.teamId, `is`(homePlayer.info.teamId))
        assertThat(playerCareer?.stats?.careerStats, `is`(homePlayer.stats.careerStats))
        assertThat(playerCareer?.stats?.careerRank, `is`(homePlayer.stats.careerRank))
    }

    @Test
    fun player_homePlayer_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        val homeCareerStats = homePlayer.stats.careerStats.first()
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateTimeFrameSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.TIME_FRAME)
        val homeCareerStats = homePlayer.stats.careerStats.first()
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateGamePlayedSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.GP)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateGameWinSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.W)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateLoseSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.L)
        val homeCareerStats = homePlayer.stats.careerStats.first()
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateWinPercentageSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.WINP)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePointsSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PTS)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFGMSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FGM)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFGASort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FGA)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFGPSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FGP)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePM3Sort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PM3)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePA3Sort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PA3)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePP3Sort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PP3)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFTMSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FTM)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFTASort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FTA)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateFTPSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.FTP)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateOREBSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.OREB)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateDREBSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.DREB)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateREBSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.REB)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateASTSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.AST)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateTOVSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.TOV)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateSTLSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.STL)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updateBLKSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.BLK)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePFSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PF)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    @Test
    fun player_updatePLUSMINUSSort_checksFirstRowCareerStats() {
        viewModel.careerStats.launchAndCollect(coroutineEnvironment)
        viewModel.updateStatsSort(CareerStatsSort.PLUSMINUS)
        val homeCareerStats = homePlayer.stats.careerStats[1]
        val careerStats = viewModel.careerStats.value.first()
        assertCareerStats(careerStats, homeCareerStats)
    }

    private fun assertCareerStats(
        result: PlayerCareer.PlayerCareerStats.Stats,
        expected: PlayerCareer.PlayerCareerStats.Stats
    ) {
        assertThat(result.gamePlayed, `is`(expected.gamePlayed))
        assertThat(result.win, `is`(expected.win))
        assertThat(result.lose, `is`(expected.lose))
        assertThat(result.winPercentage, `is`(expected.winPercentage))
        assertThat(result.fieldGoalsMade, `is`(expected.fieldGoalsMade))
        assertThat(result.fieldGoalsAttempted, `is`(expected.fieldGoalsAttempted))
        assertThat(result.fieldGoalsPercentage, `is`(expected.fieldGoalsPercentage))
        assertThat(result.threePointersMade, `is`(expected.threePointersMade))
        assertThat(result.threePointersAttempted, `is`(expected.threePointersAttempted))
        assertThat(result.threePointersPercentage, `is`(expected.threePointersPercentage))
        assertThat(result.freeThrowsMade, `is`(expected.freeThrowsMade))
        assertThat(result.freeThrowsAttempted, `is`(expected.freeThrowsAttempted))
        assertThat(result.freeThrowsPercentage, `is`(expected.freeThrowsPercentage))
        assertThat(result.reboundsOffensive, `is`(expected.reboundsOffensive))
        assertThat(result.reboundsDefensive, `is`(expected.reboundsDefensive))
        assertThat(result.reboundsTotal, `is`(expected.reboundsTotal))
        assertThat(result.assists, `is`(expected.assists))
        assertThat(result.turnovers, `is`(expected.turnovers))
        assertThat(result.steals, `is`(expected.steals))
        assertThat(result.blocks, `is`(expected.blocks))
        assertThat(result.foulsPersonal, `is`(expected.foulsPersonal))
        assertThat(result.points, `is`(expected.points))
        assertThat(result.plusMinus, `is`(expected.plusMinus))
    }
}