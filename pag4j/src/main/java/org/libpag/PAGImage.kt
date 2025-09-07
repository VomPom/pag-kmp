package org.libpag

/**
 *
 * Created by @juliswang on 2025/07/21 22:42
 *
 * @Description
 */

class PAGImage(var nativeContext: Long) {
    /**
     * Returns the width in pixels of the image.
     */
    external fun width(): Int

    /**
     * Returns the height in pixels of the image.
     */
    external fun height(): Int

    /**
     * Returns the current scale mode.
     */
    external fun scaleMode(): Int

    /**
     * Specifies the rule of how to scale the image content to fit the original image size. The matrix
     * changes when this method is called.
     */
//    external fun setScaleMode(mode: Int)

    /**
     * Returns a copy of current matrix.
     */
//    fun matrix(): Matrix {
//        val values = FloatArray(9)
//        nativeGetMatrix(values)
//        val matrix:  Matrix =  Matrix()
//        matrix.setValues(values)
//        return matrix
//    }

//    private external fun nativeGetMatrix(values: FloatArray)

    /**
     * Set the transformation which will be applied to the image content. The scaleMode property
     * will be set to PAGScaleMode::None when this method is called.
     */
//    fun setMatrix(matrix:  Matrix) {
//        val values = FloatArray(9)
//        matrix.getValues(values)
//        nativeSetMatrix(values[0], values[3], values[1], values[4], values[2], values[5])
//    }

    private external fun nativeSetMatrix(
        a: Float, b: Float, c: Float,
        d: Float, tx: Float, ty: Float
    )

    /**
     * Free up resources used by the PAGImage instance immediately instead of relying on the
     * garbage collector to do this for you at some point in the future.
     */
    fun release() {
        nativeRelease()
    }

    private external fun nativeRelease()


    fun finalize() {
        nativeFinalize()
    }

    private external fun nativeFinalize()

    companion object {
        init {
            LibraryLoadUtils.loadPag4j()
            nativeInit()
        }

        /**
         * Creates a PAGImage object from a path of a image file, return null if the file does not exist
         * or it's not a valid image file.
         */
        @JvmStatic
        fun FromPath(path: String): PAGImage? {
            val context = LoadFromPath(path)
            if (context == 0L) {
                return null
            }
            return PAGImage(context)
        }

        /**
         * Creates a PAGImage object from the specified byte data, return null if the bytes is empty or it's not a valid
         * image file.
         */
        @JvmStatic
        fun FromBytes(bytes: ByteArray?): PAGImage? {
            if (bytes == null || bytes.size == 0) {
                return null
            }

            val context = LoadFromBytes(bytes, bytes.size)
            if (context == 0L) {
                return null
            }
            return PAGImage(context)
        }


        /**
         * Creates a PAGImage object from the specified OpenGL texture ID, returns null if the texture
         * is invalid or there is no current openGL context. Note:
         * 1. The caller must make sure that the texture outlive the lifetime of the returned PAGImage
         * object.
         * 2. The current openGL context should be the same as the shareContext parameter of
         * [PAGSurface.FromSurfaceTexture] or the context which PAGSurface.FromTexture() uses,
         * so that the returned PAGImage object can be drawn on to that PAGSurface.
         * 3. The caller can use fence sync objects to synchronise texture content (see [ ][PAGPlayer.flushAndFenceSync] and [PAGPlayer.waitSync]).
         *
         * @param textureID   the id of the texture.
         * @param textureTarget either GL_TEXTURE_EXTERNAL_OES or GL_TEXTURE_2D.
         * @param width       the width of the texture.
         * @param height      the height of the texture.
         *
         * @see PAGSurface.FromSurfaceTexture
         * @see PAGSurface.FromTexture
         * @see PAGSurface.FromTexture
         * @see PAGSurface.FromTextureForAsyncThread
         * @see PAGSurface.FromTextureForAsyncThread
         * @see PAGPlayer.flushAndFenceSync
         * @see PAGPlayer.waitSync
         */
        @JvmStatic
        fun FromTexture(textureID: Int, textureTarget: Int, width: Int, height: Int): PAGImage? {
            return FromTexture(textureID, textureTarget, width, height, false)
        }

        /**
         * Creates a PAGImage object from the specified OpenGL texture ID, returns null if the texture
         * is invalid or there is no current openGL context. Note:
         * 1. The caller must make sure that the texture outlive the lifetime of the returned PAGImage
         * object.
         * 2. The current openGL context should be the same as the shareContext parameter of
         * [PAGSurface.FromSurfaceTexture] or the context which PAGSurface.FromTexture() uses,
         * so that the returned PAGImage object can be drawn on to that PAGSurface.
         * 3. The caller can use fence sync objects to synchronise texture content (see [ ][PAGPlayer.flushAndFenceSync] and [PAGPlayer.waitSync]).
         *
         * @param textureID   the id of the texture.
         * @param textureTarget either GL_TEXTURE_EXTERNAL_OES or GL_TEXTURE_2D.
         * @param width       the width of the texture.
         * @param height      the height of the texture.
         * @param flipY indicates the origin of the texture, true means 'bottom-left', false means 'top-left'.
         *
         * @see PAGSurface.FromSurfaceTexture
         * @see PAGSurface.FromTexture
         * @see PAGSurface.FromTexture
         * @see PAGSurface.FromTextureForAsyncThread
         * @see PAGSurface.FromTextureForAsyncThread
         * @see PAGPlayer.flushAndFenceSync
         * @see PAGPlayer.waitSync
         */
        fun FromTexture(
            textureID: Int,
            textureTarget: Int,
            width: Int,
            height: Int,
            flipY: Boolean
        ): PAGImage? {
            val context = LoadFromTexture(textureID, textureTarget, width, height, flipY)
            if (context == 0L) {
                return null
            }
            return PAGImage(context)
        }

        @JvmStatic
        private external fun LoadFromPath(path: String): Long

        @JvmStatic
        private external fun LoadFromBytes(bytes: ByteArray, length: Int): Long

        @JvmStatic
        private external fun LoadFromTexture(
            textureID: Int, textureType: Int,
            width: Int, height: Int, flipY: Boolean
        ): Long


        @JvmStatic
        private external fun nativeInit()
    }

}