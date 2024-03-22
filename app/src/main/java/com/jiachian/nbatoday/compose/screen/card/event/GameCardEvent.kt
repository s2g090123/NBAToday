package com.jiachian.nbatoday.compose.screen.card.event

sealed class GameCardEvent {
    object Login : GameCardEvent()
    object Unavailable : GameCardEvent()
    data class Bet(val gameId: String) : GameCardEvent()
}
