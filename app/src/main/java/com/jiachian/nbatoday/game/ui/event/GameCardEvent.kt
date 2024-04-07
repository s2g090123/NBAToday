package com.jiachian.nbatoday.game.ui.event

sealed class GameCardEvent {
    object Login : GameCardEvent()
    object Unavailable : GameCardEvent()
    data class Bet(val gameId: String) : GameCardEvent()
}
