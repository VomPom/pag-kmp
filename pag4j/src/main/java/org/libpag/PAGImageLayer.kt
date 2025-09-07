package org.libpag

import java.nio.ByteBuffer

/**
 *
 * Created by @juliswang on 2025/07/22 11:34
 *
 * @Description
 */
class PAGImageLayer(nativeContext: Long) : PAGLayer(nativeContext) {

    /**
     * Returns the content duration in microseconds, which indicates the minimal length required for
     * replacement.
     */
    external fun contentDuration(): Long

    /**
     * Returns the time ranges of the source video for replacement.
     */
    external fun getVideoRanges(): Array<PAGVideoRange?>?

    /**
     * Replace the original image content with the specified PAGImage object. Passing in null for the image parameter
     * resets the layer to its default image content.
     *
     * @param image The PAGImage object to replace with.
     */
    fun replaceImage(image: PAGImage?) {
        replaceImage(image?.nativeContext ?: 0)
    }

    private external fun replaceImage(image: Long)

    /**
     * The default image data of this layer, which is webp format.
     */
    external fun imageBytes(): ByteBuffer?


    companion object {
        init {
            LibraryLoadUtils.loadPag4j()
            nativeInit()
        }

        /**
         * Make a PAGImageLayer with with, height and duration(in microseconds).
         */
        fun Make(width: Int, height: Int, duration: Long): PAGImageLayer? {
            val context = nativeMake(width, height, duration)
            if (context == 0L) {
                return null
            }
            return PAGImageLayer(context)
        }

        @JvmStatic
        private external fun nativeMake(width: Int, height: Int, duration: Long): Long

        @JvmStatic
        private external fun nativeInit()
    }
}