package org.libpag


/**
 *
 * Created by @juliswang on 2025/07/22 21:45
 *
 * @Description
 */

class PAGTextLayer(nativeContext: Long) : PAGLayer(nativeContext) {
    /**
     * Returns the text layer’s fill color.
     */
    external fun fillColor(): Int

    /**
     * Set the text layer’s fill color.
     */
    external fun setFillColor(color: Int)

    /**
     * Returns the text layer's font.
     */
    external fun font(): PAGFont?

    /**
     * Set the text layer's font.
     */
    fun setFont(font: PAGFont) {
        setFont(font.fontFamily, font.fontStyle)
    }

    /**
     * Returns the text layer's font size.
     */
    external fun fontSize(): Float

    /**
     * Set the text layer's font size.
     */
    external fun setFontSize(fontSize: Float)

    /**
     * Returns the text layer's stroke color.
     */
    external fun strokeColor(): Int

    /**
     * Set the text layer's stroke color.
     */
    external fun setStrokeColor(color: Int)

    /**
     * Returns the text layer's text.
     */
    external fun text(): String?

    /**
     * Set the text layer's text.
     */
    external fun setText(text: String?)

    /**
     * Reset the text layer to its default text data.
     */
    external fun reset()

    private external fun setFont(fontFamily: String, fontStyle: String)

    companion object {
        init {
            LibraryLoadUtils.loadPag4j()
            nativeInit()
        }

        @JvmStatic
        private external fun nativeInit()
    }
}