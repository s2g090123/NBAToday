package com.jiachian.nbatoday.test.compose.screen.card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.game.ui.GameCard
import com.jiachian.nbatoday.game.ui.model.GameCardData
import com.jiachian.nbatoday.testing.testtag.GameCardTestTag
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.onNodeWithTag
import com.jiachian.nbatoday.utils.onNodeWithUnmergedTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameCardTest : BaseAndroidTest() {
    private var requestLogin: Boolean? = null
    private var requestBet: Boolean? = null

    @After
    fun teardown() {
        requestLogin = null
        requestBet = null
    }

    @Test
    fun finalGameCard() {
        val gameAndBets = GameAndBetsGenerator.getFinal()
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true }
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(gameAndBets.game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
        }
    }

    @Test
    fun playingGameCard() {
        val gameAndBets = GameAndBetsGenerator.getPlaying()
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true }
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(gameAndBets.game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
        }
    }

    @Test
    fun comingSoonGameCard() = runTest {
        val gameAndBets = GameAndBetsGenerator.getComingSoon(includeBet = false)
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true }
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Home).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.homeTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.homeTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameDetail_GameTeamInfo_Away).apply {
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_TeamAbbr)
                    .assertTextEquals(gameAndBets.game.awayTeam.team.abbreviation)
                onNodeWithTag(GameCardTestTag.GameTeamInfo_Text_Score)
                    .assertTextEquals("${gameAndBets.game.awayTeam.score}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Text_Status)
                .assertTextEquals(gameAndBets.game.statusFormattedText)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertIsDisplayed()
                .performClick()
            requestLogin.assertIsTrue()
            useCaseProvider.user.userLogin(UserAccount, UserPassword)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .performClick()
            requestBet.assertIsTrue()
        }
    }

    @Test
    fun comingSoonGameCard_hadBet() = runTest {
        val gameAndBets = GameAndBetsGenerator.getComingSoon(includeBet = true)
        repositoryProvider.bet.addBet(gameAndBets.bets.first())
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true }
            )
        }
        composeTestRule.let {
            useCaseProvider.user.userLogin(UserAccount, UserPassword)
            it.onNodeWithUnmergedTree(GameCardTestTag.GameStatusAndBetButton_Button_Bet)
                .assertDoesNotExist()
        }
    }

    @Test
    fun finalGameCard_expandedContent() {
        val gameAndBets = GameAndBetsGenerator.getFinal()
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true },
                expandable = true,
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.homeLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.homeLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.points.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.rebounds.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.assists.toInt()}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.awayLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.awayLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.points.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.rebounds.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.assists.toInt()}")
            }
        }
    }

    @Test
    fun playingGameCard_expandedContent() {
        val gameAndBets = GameAndBetsGenerator.getPlaying()
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true },
                expandable = true,
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.homeLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.homeLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.points.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.rebounds.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.homeLeader.assists.toInt()}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.awayLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.gameLeaders!!.awayLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.points.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.rebounds.toInt()}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.gameLeaders!!.awayLeader.assists.toInt()}")
            }
        }
    }

    @Test
    fun comingSoonGameCard_expandedContent() {
        val gameAndBets = GameAndBetsGenerator.getComingSoon()
        composeTestRule.setContent {
            GameCard(
                data = GameCardData(
                    data = gameAndBets,
                    user = useCaseProvider.user.getUser(),
                ),
                onRequestLogin = { requestLogin = true },
                onRequestBet = { requestBet = true },
                expandable = true,
            )
        }
        composeTestRule.let {
            it.onNodeWithUnmergedTree(GameCardTestTag.GameExpandedContent_Button_Expand)
                .performClick()
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Home).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.teamLeaders!!.homeLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.teamLeaders!!.homeLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.homeLeader.points}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.homeLeader.rebounds}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.homeLeader.assists}")
            }
            it.onNodeWithUnmergedTree(GameCardTestTag.GameCardLeadersInfo_LeaderInfoRow_Away).apply {
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerName)
                    .assertTextEquals(gameAndBets.game.teamLeaders!!.awayLeader.name)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_Text_PlayerDetail)
                    .assertTextEquals(gameAndBets.game.teamLeaders!!.awayLeader.detail)
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Points)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.awayLeader.points}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Rebounds)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.awayLeader.rebounds}")
                onNodeWithTag(GameCardTestTag.LeaderInfoRow_LeaderStatsText_Assists)
                    .assertTextEquals("${gameAndBets.game.teamLeaders!!.awayLeader.assists}")
            }
        }
    }
}
