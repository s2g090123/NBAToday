package com.jiachian.nbatoday.compose.screen.score

import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.screen.ComposeViewModel
import com.jiachian.nbatoday.compose.screen.label.LabelHelper
import com.jiachian.nbatoday.compose.screen.score.data.ScoreLeaderRowData
import com.jiachian.nbatoday.compose.screen.score.data.ScoreRowData
import com.jiachian.nbatoday.compose.screen.score.data.ScoreTeamRowData
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreLeaderLabel
import com.jiachian.nbatoday.compose.screen.score.label.ScoreTeamLabel
import com.jiachian.nbatoday.compose.screen.score.tab.BoxScoreTab
import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.repository.game.GameRepository
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
import com.jiachian.nbatoday.utils.getOrNA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoxScoreViewModel(
    game: NbaGame,
    private val repository: GameRepository,
    private val screenStateHelper: ScreenStateHelper,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) : ComposeViewModel(coroutineScope) {

    private val gameId = game.gameId

    private val isRefreshingImp = MutableStateFlow(false)
    val isRefreshing = isRefreshingImp.asStateFlow()

    val boxScore = repository.getGameBoxScore(gameId)
        .stateIn(coroutineScope, SharingStarted.Lazily, null)

    val date = boxScore.map {
        it?.gameDate ?: ""
    }.stateIn(coroutineScope, SharingStarted.Lazily, "")

    val isNotFound = boxScore.map {
        it == null
    }.stateIn(coroutineScope, SharingStarted.Lazily, true)

    val periodLabel = boxScore.map {
        it?.homeTeam?.periods?.map { period ->
            period.periodLabel
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    val statsLabels = LabelHelper.createScoreLabel()

    private val teamStatsLabels = LabelHelper.createScoreTeamLabel()

    private val leaderStatsLabels = LabelHelper.createScoreLeaderLabel()

    val homeTeam = boxScore.map { score ->
        score?.homeTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val awayTeam = boxScore.map { score ->
        score?.awayTeam?.team ?: teamOfficial
    }.stateIn(coroutineScope, SharingStarted.Eagerly, teamOfficial)

    val homeLeader = boxScore.map { score ->
        val homePersonId = game.homeLeaderPersonId
        score?.homeTeam?.players?.firstOrNull {
            it.personId == homePersonId
        } ?: score?.homeTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val awayLeader = boxScore.map { score ->
        val awayPersonId = game.awayLeadersPersonId
        score?.awayTeam?.players?.firstOrNull {
            it.personId == awayPersonId
        } ?: score?.awayTeam?.getMostPointsPlayer()
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val selectedTabImp = MutableStateFlow(BoxScoreTab.HOME)
    val selectedTab = selectedTabImp.asStateFlow()
    val selectedTabIndex = selectedTab.map {
        BoxScoreTab.indexOf(it)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)

    val homeScoreRowData = boxScore.map { score ->
        score?.homeTeam?.players?.map { player ->
            val statsRowData = statsLabels.map { label ->
                label.transformRowData(player.statistics)
            }
            player.createRowData(statsRowData)
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val awayScoreRowData = boxScore.map { score ->
        score?.awayTeam?.players?.map { player ->
            val statsRowData = statsLabels.map { label ->
                label.transformRowData(player.statistics)
            }
            player.createRowData(statsRowData)
        } ?: emptyList()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val teamStatsRowData = boxScore.map { score ->
        teamStatsLabels.map { label ->
            label.transformRowData(score)
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    val leaderStatsRowData = combine(
        homeLeader,
        awayLeader
    ) { home, away ->
        leaderStatsLabels.map { label ->
            label.transformRowData(home, away)
        }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    init {
        refreshScore()
    }

    fun refreshScore() {
        coroutineScope.launch {
            isRefreshingImp.value = true
            withContext(dispatcherProvider.io) {
                repository.refreshGameBoxScore(gameId)
            }
            isRefreshingImp.value = false
        }
    }

    fun selectTab(tab: BoxScoreTab) {
        selectedTabImp.value = tab
    }

    private fun ScoreLabel.transformRowData(stats: GameBoxScore.BoxScoreTeam.Player.Statistics): ScoreRowData.RowData {
        val value = when (textRes) {
            R.string.label_min -> stats.minutes
            R.string.label_fgm -> stats.fieldGoalProportion
            R.string.label_3pm -> stats.threePointProportion
            R.string.label_ftm -> stats.freeThrowProportion
            R.string.label_plus_minus -> stats.plusMinusPoints
            R.string.label_or -> stats.reboundsOffensive
            R.string.label_dr -> stats.reboundsDefensive
            R.string.label_tr -> stats.reboundsTotal
            R.string.label_as -> stats.assists
            R.string.label_pf -> stats.foulsPersonal
            R.string.label_st -> stats.steals
            R.string.label_to -> stats.turnovers
            R.string.label_bs -> stats.blocks
            R.string.label_ba -> stats.blocksReceived
            R.string.label_pts -> stats.points
            R.string.label_eff -> stats.efficiency
            else -> ""
        }.toString()
        return ScoreRowData.RowData(
            value = value,
            textWidth = width,
            textAlign = textAlign
        )
    }

    private fun ScoreTeamLabel.transformRowData(score: GameBoxScore?): ScoreTeamRowData {
        val homeStats = score?.homeTeam?.statistics
        val awayStats = score?.awayTeam?.statistics
        val (home, away) = when (textRes) {
            R.string.box_score_statistics_points -> homeStats?.points.getOrNA() to awayStats?.points.getOrNA()
            R.string.box_score_statistics_fieldGoal -> homeStats?.fieldGoalsFormat.getOrNA() to awayStats?.fieldGoalsFormat.getOrNA()
            R.string.box_score_statistics_twoPoints -> homeStats?.twoPointsFormat.getOrNA() to awayStats?.twoPointsFormat.getOrNA()
            R.string.box_score_statistics_threePoints -> homeStats?.threePointsFormat.getOrNA() to awayStats?.threePointsFormat.getOrNA()
            R.string.box_score_statistics_freeThrows -> homeStats?.freeThrowFormat.getOrNA() to awayStats?.freeThrowFormat.getOrNA()
            R.string.box_score_statistics_rebounds -> homeStats?.reboundsTotal.getOrNA() to awayStats?.reboundsTotal.getOrNA()
            R.string.box_score_statistics_reboundsDef -> homeStats?.reboundsDefensive.getOrNA() to awayStats?.reboundsDefensive.getOrNA()
            R.string.box_score_statistics_reboundsOff -> homeStats?.reboundsOffensive.getOrNA() to awayStats?.reboundsOffensive.getOrNA()
            R.string.box_score_statistics_assists -> homeStats?.assists.getOrNA() to awayStats?.assists.getOrNA()
            R.string.box_score_statistics_blocks -> homeStats?.blocks.getOrNA() to awayStats?.blocks.getOrNA()
            R.string.box_score_statistics_steals -> homeStats?.steals.getOrNA() to awayStats?.steals.getOrNA()
            R.string.box_score_statistics_turnovers -> homeStats?.turnovers.getOrNA() to awayStats?.turnovers.getOrNA()
            R.string.box_score_statistics_pointsFastBreak -> homeStats?.pointsFastBreak.getOrNA() to awayStats?.pointsFastBreak.getOrNA()
            R.string.box_score_statistics_pointsFromTurnOvers -> homeStats?.pointsFromTurnovers.getOrNA() to awayStats?.pointsFromTurnovers.getOrNA()
            R.string.box_score_statistics_pointsInPaint -> homeStats?.pointsInThePaint.getOrNA() to awayStats?.pointsInThePaint.getOrNA()
            R.string.box_score_statistics_pointsSecondChance -> homeStats?.pointsSecondChance.getOrNA() to awayStats?.pointsSecondChance.getOrNA()
            R.string.box_score_statistics_benchPoints -> homeStats?.benchPoints.getOrNA() to awayStats?.benchPoints.getOrNA()
            R.string.box_score_statistics_foulsPersonal -> homeStats?.foulsPersonal.getOrNA() to awayStats?.foulsPersonal.getOrNA()
            R.string.box_score_statistics_foulsTechnical -> homeStats?.foulsTechnical.getOrNA() to awayStats?.foulsTechnical.getOrNA()
            else -> "" to ""
        }
        return ScoreTeamRowData(
            homeValue = home,
            awayValue = away,
            label = this
        )
    }

    private fun ScoreLeaderLabel.transformRowData(
        homeLeader: GameBoxScore.BoxScoreTeam.Player?,
        awayLeader: GameBoxScore.BoxScoreTeam.Player?
    ): ScoreLeaderRowData {
        val homeStats = homeLeader?.statistics
        val awayStats = awayLeader?.statistics
        val (home, away) = when (textRes) {
            R.string.box_score_leader_statistics_name -> homeLeader?.nameAbbr.getOrNA() to awayLeader?.nameAbbr.getOrNA()
            R.string.box_score_leader_statistics_position -> homeLeader?.position.getOrNA() to awayLeader?.position.getOrNA()
            R.string.box_score_leader_statistics_time -> homeStats?.minutes.getOrNA() to awayStats?.minutes.getOrNA()
            R.string.box_score_leader_statistics_points -> homeStats?.points.getOrNA() to awayStats?.points.getOrNA()
            R.string.box_score_leader_statistics_plusMinusPoints -> homeStats?.plusMinusPoints.getOrNA() to awayStats?.plusMinusPoints.getOrNA()
            R.string.box_score_leader_statistics_fieldGoal -> homeStats?.fieldGoalsFormat.getOrNA() to awayStats?.fieldGoalsFormat.getOrNA()
            R.string.box_score_leader_statistics_twoPoints -> homeStats?.twoPointsFormat.getOrNA() to awayStats?.twoPointsFormat.getOrNA()
            R.string.box_score_leader_statistics_threePoints -> homeStats?.threePointsFormat.getOrNA() to awayStats?.threePointsFormat.getOrNA()
            R.string.box_score_leader_statistics_freeThrows -> homeStats?.freeThrowFormat.getOrNA() to awayStats?.freeThrowFormat.getOrNA()
            R.string.box_score_leader_statistics_rebounds -> homeStats?.reboundsTotal.getOrNA() to awayStats?.reboundsTotal.getOrNA()
            R.string.box_score_leader_statistics_reboundsDef -> homeStats?.reboundsDefensive.getOrNA() to awayStats?.reboundsDefensive.getOrNA()
            R.string.box_score_leader_statistics_reboundsOff -> homeStats?.reboundsOffensive.getOrNA() to awayStats?.reboundsOffensive.getOrNA()
            R.string.box_score_leader_statistics_assists -> homeStats?.assists.getOrNA() to awayStats?.assists.getOrNA()
            R.string.box_score_leader_statistics_blocks -> homeStats?.blocks.getOrNA() to awayStats?.blocks.getOrNA()
            R.string.box_score_leader_statistics_steals -> homeStats?.steals.getOrNA() to awayStats?.steals.getOrNA()
            R.string.box_score_leader_statistics_turnovers -> homeStats?.turnovers.getOrNA() to awayStats?.turnovers.getOrNA()
            R.string.box_score_leader_statistics_foulsPersonal -> homeStats?.foulsPersonal.getOrNA() to awayStats?.foulsPersonal.getOrNA()
            R.string.box_score_leader_statistics_foulsTechnical -> homeStats?.foulsTechnical.getOrNA() to awayStats?.foulsTechnical.getOrNA()
            else -> "" to ""
        }
        return ScoreLeaderRowData(
            homeValue = home,
            awayValue = away,
            label = this
        )
    }

    fun openPlayerCareer(playerId: Int) {
        screenStateHelper.openScreen(NbaScreenState.Player(playerId))
    }
}
