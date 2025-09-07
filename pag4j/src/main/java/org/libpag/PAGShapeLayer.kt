package org.libpag


/**
 *
 * Created by @juliswang on 2025/07/22 21:17
 *
 * @Description
 */

class PAGShapeLayer(nativeContext: Long) : PAGLayer(nativeContext) {

    companion object {
        init {
            nativeInit()
        }

        @JvmStatic
        private external fun nativeInit()
    }
}
