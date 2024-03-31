package com.jiachian.nbatoday.common.ui.bet.event

sealed class BetDialogUIEvent {
    class TextHomePoints(val points: Long) : BetDialogUIEvent()
    class TextAwayPoints(val points: Long) : BetDialogUIEvent()
    object Bet : BetDialogUIEvent()
    object Confirm : BetDialogUIEvent()
    object CancelConfirm : BetDialogUIEvent()
    object EventReceived : BetDialogUIEvent()
}
