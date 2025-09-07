package org.libpag

import java.io.File
import java.util.Locale

/**
 *
 * Created by @juliswang on 2025/07/22 11:47
 *
 * @Description
 */

class PAGFont {

    constructor(fontFamily: String, fontStyle: String) {
        this.fontFamily = fontFamily
        this.fontStyle = fontStyle
    }

    /**
     * A string with the name of the font family.
     */
    var fontFamily: String = ""

    /**
     * A string with the style information — e.g., “bold”, “italic”.
     */
    var fontStyle: String = ""

    companion object {
        init {
            LibraryLoadUtils.loadPag4j()
        }

        private class FontConfig {
            var language: String = ""
            var fileName: String = ""
            var ttcIndex: Int = 0
            var weight: Int = 400
        }

        @JvmStatic
        fun RegisterFont(fontPath: String?): PAGFont {
            return RegisterFont(fontPath, 0)
        }

        @JvmStatic
        fun RegisterFont(fontPath: String?, ttcIndex: Int): PAGFont {
            return RegisterFont(fontPath, ttcIndex, "", "")
        }

        @JvmStatic
        external fun RegisterFont(
            fontPath: String?, ttcIndex: Int,
            fontFamily: String?, fontStyle: String?
        ): PAGFont

        @JvmStatic
        fun UnregisterFont(font: PAGFont) {
            UnregisterFont(font.fontFamily, font.fontStyle)
        }

        @JvmStatic
        private external fun UnregisterFont(fontFamily: String, fontStyle: String)

        @JvmStatic
        private fun RegisterFontBytes(bytes: ByteArray, length: Int, ttcIndex: Int): PAGFont {
            return RegisterFontBytes(bytes, length, ttcIndex, "", "")
        }

        @JvmStatic
        private external fun RegisterFontBytes(
            bytes: ByteArray, length: Int, ttcIndex: Int,
            fontFamily: String, fontStyle: String
        ): PAGFont

        @JvmStatic
        private external fun SetFallbackFontPaths(fontNameList: Array<String>, ttcIndices: IntArray)

        private var systemFontLoaded: Boolean = false

        private fun addFont(
            fontConfig: FontConfig,
            fontPaths: ArrayList<String>,
            ttcList: ArrayList<Int>
        ) {
            if (fontPaths.contains(fontConfig.fileName)) {
                return
            }
            val file = File(fontConfig.fileName)
            if (!file.exists()) {
                return
            }
            fontPaths.add(fontConfig.fileName)
            ttcList.add(fontConfig.ttcIndex)
        }

        private fun getFontByLanguage(fontList: Array<FontConfig>, language: String): FontConfig? {
            var language = language
            language = language.lowercase(Locale.getDefault())
            for (fontConfig in fontList) {
                if (fontConfig.language.lowercase(Locale.getDefault()) == language) {
                    return fontConfig
                }
            }
            return null
        }
    }
}