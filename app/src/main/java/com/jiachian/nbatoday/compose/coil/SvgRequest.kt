package com.jiachian.nbatoday.compose.coil

import android.content.Context
import coil.decode.SvgDecoder
import coil.request.ImageRequest

class SvgRequest private constructor(
    val context: Context
) {
    class Builder(context: Context) {

        private val requestBuilder = ImageRequest.Builder(context)

        private var data: Any? = null

        fun data(data: Any?) = apply {
            this.data = data
            requestBuilder.data(data)
        }

        fun build(): ImageRequest {
            return requestBuilder
                .decoderFactory(SvgDecoder.Factory())
                .build()
        }
    }
}
