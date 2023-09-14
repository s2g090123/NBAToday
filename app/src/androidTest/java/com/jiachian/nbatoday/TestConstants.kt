package com.jiachian.nbatoday

import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.data.local.team.NBATeam

const val FINAL_GAME_ID = "0"
const val PLAYING_GAME_ID = "1"
const val COMING_SOON_GAME_ID = "2"

const val GAME_CODE = "20230101/TWHTWA"
const val GAME_STATUS_PREPARE = "3:00 pm ET"
const val GAME_STATUS_FINAL = "Final"
const val GAME_DATE = "2023-01-01"
const val GAME_SEASON = "2022-23"
const val GAME_SEASON_NEXT = "2023-24"

const val HOME_TEAM_ID = 123456
const val AWAY_TEAM_ID = 654321

const val TEAM_CITY = "TW"
const val HOME_TEAM_FULL_NAME = "Taiwan Home"
const val AWAY_TEAM_FULL_NAME = "Taiwan Away"
const val HOME_TEAM_NAME = "Home"
const val AWAY_TEAM_NAME = "Away"
const val HOME_TEAM_ABBR = "TWH"
const val AWAY_TEAM_ABBR = "TWA"
const val HOME_TEAM_LOCATION = "Home"
const val AWAY_TEAM_LOCATION = "Away"

val HOME_TEAM = object : NBATeam {
    override val teamId: Int = HOME_TEAM_ID
    override val abbreviation: String = HOME_TEAM_ABBR
    override val teamName: String = HOME_TEAM_NAME
    override val location: String = HOME_TEAM_LOCATION
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.EAST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHEAST
    override val colors: NBAColors = OfficialColors
}
val AWAY_TEAM = object : NBATeam {
    override val teamId: Int = AWAY_TEAM_ID
    override val abbreviation: String = AWAY_TEAM_ABBR
    override val teamName: String = AWAY_TEAM_NAME
    override val location: String = AWAY_TEAM_LOCATION
    override val logoRes: Int = R.drawable.ic_logo_nba
    override val conference: NBATeam.Conference = NBATeam.Conference.WEST
    override val division: NBATeam.Division = NBATeam.Division.SOUTHWEST
    override val colors: NBAColors = OfficialColors
}

const val HOME_PLAYER_ID = 123456
const val AWAY_PLAYER_ID = 654321
const val HOME_PLAYER_FULL_NAME = "Du Allen"
const val HOME_PLAYER_FIRST_NAME = "Du"
const val HOME_PLAYER_LAST_NAME = "Allen"
const val AWAY_PLAYER_FULL_NAME = "Jia Chian"
const val AWAY_PLAYER_FIRST_NAME = "Jia"
const val AWAY_PLAYER_LAST_NAME = "Chian"

const val BASIC_NUMBER = 10
const val BASIC_PERCENTAGE = 100.0
const val BASIC_POSITION = "G"
const val BASIC_TIME = 1672549200000L
const val NEXT_TIME = 1672635600000L
const val BASIC_MINUTES = "10:00"

const val USER_ACCOUNT = "allen.du"
const val USER_PASSWORD = "0000"
const val USER_NAME = "Account"
const val USER_POINTS = 1000L
const val USER_TOKEN = "123456"
