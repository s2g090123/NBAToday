package com.jiachian.nbatoday.compose.coil

import android.content.Context
import coil.request.ImageRequest

class SvgRequest private constructor(
    val context: Context
) {
    class Builder {

        private val context: Context

        private val requestBuilder: ImageRequest.Builder

        private var data: Any?

        constructor(context: Context) {
            this.context = context
            data = null
            requestBuilder = ImageRequest.Builder(context)
        }

        @JvmOverloads
        constructor(request: SvgRequest, context: Context = request.context) {
            this.context = context
            data = null
            requestBuilder = ImageRequest.Builder(context)
        }

        fun data(data: Any?) = apply {
            this.data = data
            requestBuilder.data(data)
        }

        fun build(): ImageRequest {
            return requestBuilder.build()
        }
    }
}
