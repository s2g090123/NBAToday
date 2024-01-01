package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.theme.CelticsColors
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.models.local.team.data.teamCeltics
import com.jiachian.nbatoday.models.local.team.data.teamLakers

const val FinalGameId = "0"
const val PlayingGameId = "1"
const val ComingSoonGameId = "2"

const val GameCode = "20230101/TWHTWA"
const val GameStatusPrepare = "3:00 pm ET"
const val GameStatusFinal = "Final"
const val GameDate = "2023-01-01"
const val FinalGameDateTime = "2022-12-31T12:00:00"
const val PlayingGameDateTime = "2023-01-01T12:00:00"
const val ComingSoonGameDateTime = "2023-01-02T12:00:00"
const val FinalGameTimeMs = 1672506000000L
const val PlayingGameTimeMs = 1672592400000L
const val ComingSoonGameTimeMs = 1672678800000L
const val GameDay = "Sun"
const val GameSeason = "2022-23"
const val GameSeasonNext = "2023-24"

val HomeTeamId = teamLakers.teamId
val AwayTeamId = teamCeltics.teamId

const val TeamCity = "TW"
const val HomeTeamFullName = "Taiwan Home"
const val AwayTeamFullName = "Taiwan Away"
const val HomeTeamName = "Home"
const val AwayTeamName = "Away"
const val HomeTeamAbbr = "TWH"
const val AwayTeamAbbr = "TWA"
const val HomeTeamLocation = "Home"
const val AwayTeamLocation = "Away"
val HomeTeamColors = LakersColors
val AwayTeamColors = CelticsColors

const val HomePlayerId = 123456
const val AwayPlayerId = 654321
const val HomePlayerFullName = "Du Allen"
const val HomePlayerFirstName = "Du"
const val HomePlayerLastName = "Allen"
const val AwayPlayerFullName = "Jia Chian"
const val AwayPlayerFirstName = "Jia"
const val AwayPlayerLastName = "Chian"
const val PlayerBirthDateTime = "2013-01-01T00:00:00"

const val BasicNumber = 10
const val BasicPercentage = 100.0
const val BasicPosition = "G"
const val BasicTime = 1672549200000L
const val NextTime = 1672635600000L
const val BasicMinutes = "10:00"
const val GamePlayed = BasicNumber * 2

const val UserAccount = "allen.du"
const val UserPassword = "0000"
const val UserName = "Account"
const val UserPoints = 1000L
const val UserToken = "123456"

const val FinalBetId = 1L
const val PlayingBetId = 2L
const val ComingSoonBetId = 3L
const val BetPoints = 1L
