package org.libpag

/**
 *
 * Created by @juliswang on 2025/07/21 17:31
 *
 * @Description
 */


class PAGText {
    object Justification {
        const val Left: Int = 0
        const val Center: Int = 1
        const val Right: Int = 2
        const val FullJustifyLastLineLeft: Int = 3
        const val FullJustifyLastLineRight: Int = 4
        const val FullJustifyLastLineCenter: Int = 5
        const val FullJustifyLastLineFull: Int = 6
    }


    /**
     * When true, the text layer shows a fill.
     */
    var applyFill: Boolean = true

    /**
     * When true, the text layer shows a stroke.
     */
    var applyStroke: Boolean = false

    var baselineShift: Float = 0f

    /**
     * When true, the text layer is paragraph (bounded) text.
     */
    var boxText: Boolean = false

    /**
     * For box text, the pixel boundary for the text bounds.
     */
//    var boxTextRect: RectF = RectF()

    var firstBaseLine: Float = 0f

    var fauxBold: Boolean = false

    var fauxItalic: Boolean = false

    /**
     * The text layer’s fill color.
     */
    var fillColor: Int = 0

    /**
     * A string with the name of the font family.
     */
    var fontFamily: String = ""

    /**
     * A string with the style information — e.g., “bold”, “italic”.
     */
    var fontStyle: String = ""

    /**
     * The text layer’s font size in pixels.
     */
    var fontSize: Float = 24f

    /**
     * The text layer’s stroke color.
     */
    var strokeColor: Int = 0

    /**
     * Indicates the rendering order for the fill and stroke of a text layer.
     */
    var strokeOverFill: Boolean = true

    /**
     * The text layer’s stroke thickness.
     */
    var strokeWidth: Float = 1f

    /**
     * The text layer’s Source Text value.
     */
    var text: String = ""

    /**
     * The paragraph justification for the text layer. Such as : PAGJustificationLeftJustify, PAGJustificationCenterJustify...
     */
    var justification: Int = Justification.Left

    /**
     * The space between lines, 0 indicates 'auto', which is fontSize * 1.2
     */
    var leading: Float = 0f

    /**
     * The text layer’s spacing between characters.
     */
    var tracking: Float = 0f

    /**
     * The text layer’s background color.
     */
    var backgroundColor: Int = 0

    /**
     * The text layer’s background alpha. 0 = 100% transparent, 255 = 100% opaque.
     */
    var backgroundAlpha: Int = 0
}