package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.models.local.team.NBATeam

const val FinalGameId = "0"
const val PlayingGameId = "1"
const val ComingSoonGameId = "2"

const val GameCode = "20230101/TWHTWA"
const val GameStatusPrepare = "3:00 pm ET"
const val GameStatusFinal = "Final"
const val GameDate = "2023-01-01"
const val GameSeason = "2022-23"
const val GameSeasonNext = "2023-24"

const val HomeTeamId = 123456
const val AwayTeamId = 654321

const val TeamCity = "TW"
const val HomeTeamFullName = "Taiwan Home"
const val AwayTeamFullName = "Taiwan Away"
const val HomeTeamName = "Home"
const val AwayTeamName = "Away"
const val HomeTeamAbbr = "TWH"
const val AwayTeamAbbr = "TWA"
const val HomeTeamLocation = "Home"
const val AwayTeamLocation = "Away"

val HomeTeam = object : NBATeam {
    override val teamId: Int = HomeTeamId
    override val abbreviation: String = HomeTeamAbbr
    override val teamName: String = HomeTeamName
    override val location: String = HomeTeamLocation
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHEAST
    override val colors: NbaColors = OfficialColors
}
val AwayTeam = object : NBATeam {
    override val teamId: Int = AwayTeamId
    override val abbreviation: String = AwayTeamAbbr
    override val teamName: String = AwayTeamName
    override val location: String = AwayTeamLocation
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHWEST
    override val colors: NbaColors = OfficialColors
}

const val HomePlayerId = 123456
const val AwayPlayerId = 654321
const val HomePlayerFullName = "Du Allen"
const val HomePlayerFirstName = "Du"
const val HomePlayerLastName = "Allen"
const val AwayPlayerFullName = "Jia Chian"
const val AwayPlayerFirstName = "Jia"
const val AwayPlayerLastName = "Chian"

const val BasicNumber = 10
const val BasicPercentage = 100.0
const val BasicPosition = "G"
const val BasicTime = 1672549200000L
const val NextTime = 1672635600000L
const val BasicMinutes = "10:00"

const val UserAccount = "allen.du"
const val UserPassword = "0000"
const val UserName = "Account"
const val UserPoints = 1000L
const val UserToken = "123456"
