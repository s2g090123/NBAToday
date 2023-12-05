package com.jiachian.nbatoday.models.local.team

data class TeamRank(
    val standing: Int,
    val pointsRank: Int,
    val reboundsRank: Int,
    val assistsRank: Int,
    val plusMinusRank: Int
) {
    companion object {
        fun default(): TeamRank {
            return TeamRank(
                standing = 0,
                pointsRank = 0,
                reboundsRank = 0,
                assistsRank = 0,
                plusMinusRank = 0
            )
        }
    }
}
