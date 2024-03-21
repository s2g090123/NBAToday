package com.jiachian.nbatoday.compose.screen.bet.dialog.event

sealed class BetDialogUIEvent {
    class TextHomePoints(val points: Long) : BetDialogUIEvent()
    class TextAwayPoints(val points: Long) : BetDialogUIEvent()
    object Bet : BetDialogUIEvent()
    object Confirm : BetDialogUIEvent()
    object CancelConfirm : BetDialogUIEvent()
    object EventReceived : BetDialogUIEvent()
}
