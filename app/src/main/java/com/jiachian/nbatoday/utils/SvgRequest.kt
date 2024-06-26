package com.jiachian.nbatoday.utils

import android.content.Context
import coil.decode.SvgDecoder
import coil.request.ImageRequest

/**
 * A builder class for creating [ImageRequest] instances specifically for loading SVG images using Coil.
 *
 * This class provides a convenient way to construct [ImageRequest] instances tailored for SVG images.
 *
 * @usage:
 * AsyncImage(
 *   model = SvgRequest.Builder(LocalContext.current)
 *       .data(`svg data source`)
 *       .build()
 * )
 */
class SvgRequest private constructor() {
    class Builder(context: Context) {

        private val requestBuilder = ImageRequest.Builder(context)

        fun data(data: Any?) = apply {
            requestBuilder.data(data)
        }

        fun build(): ImageRequest {
            return requestBuilder
                .decoderFactory(SvgDecoder.Factory())
                .build()
        }
    }
}
