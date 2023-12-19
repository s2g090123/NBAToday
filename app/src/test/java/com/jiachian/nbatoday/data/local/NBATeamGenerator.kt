package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.AwayTeamLocation
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamLocation
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.R
import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.compose.theme.OfficialColors
import com.jiachian.nbatoday.models.local.team.NBATeam

object NBATeamGenerator {
    fun getHome(): NBATeam {
        return object : NBATeam {
            override val teamId: Int = HomeTeamId
            override val abbreviation: String = HomeTeamAbbr
            override val teamName: String = HomeTeamName
            override val location: String = HomeTeamLocation
            override val logoRes: Int = R.drawable.ic_logo_nba
            override val conference: NBATeam.Conference = NBATeam.Conference.EAST
            override val colors: NBAColors = OfficialColors
        }
    }

    fun getAway(): NBATeam {
        return object : NBATeam {
            override val teamId: Int = AwayTeamId
            override val abbreviation: String = AwayTeamAbbr
            override val teamName: String = AwayTeamName
            override val location: String = AwayTeamLocation
            override val logoRes: Int = R.drawable.ic_logo_nba
            override val conference: NBATeam.Conference = NBATeam.Conference.WEST
            override val colors: NBAColors = OfficialColors
        }
    }
}
