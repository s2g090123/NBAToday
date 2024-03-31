package com.jiachian.nbatoday.bet.domain.error

import com.jiachian.nbatoday.common.domain.Error

enum class AddBetError : Error {
    NOT_LOGIN,
    POINTS_NOT_ENOUGH,
    UPDATE_FAIL,
}
