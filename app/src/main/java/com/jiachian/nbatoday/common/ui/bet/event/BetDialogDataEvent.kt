package com.jiachian.nbatoday.common.ui.bet.event

import com.jiachian.nbatoday.common.ui.bet.error.BetDialogError

sealed class BetDialogDataEvent {
    class Error(val error: BetDialogError) : BetDialogDataEvent()
    object Done : BetDialogDataEvent()
}
