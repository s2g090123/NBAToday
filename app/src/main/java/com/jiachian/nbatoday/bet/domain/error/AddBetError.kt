package com.jiachian.nbatoday.bet.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class AddBetError : Error {
    POINTS_NOT_ENOUGH,
    UPDATE_FAIL,
}
