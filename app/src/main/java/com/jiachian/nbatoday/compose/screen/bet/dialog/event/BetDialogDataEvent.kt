package com.jiachian.nbatoday.compose.screen.bet.dialog.event

import com.jiachian.nbatoday.compose.screen.bet.dialog.BetDialogError

sealed class BetDialogDataEvent {
    class Error(val error: BetDialogError) : BetDialogDataEvent()
    object Done : BetDialogDataEvent()
}
