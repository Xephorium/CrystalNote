package com.xephorium.crystalnote.data.model

import android.content.Context
import android.content.res.TypedArray
import com.xephorium.crystalnote.R
import android.graphics.Color
import androidx.core.content.ContextCompat


data class CrystalNoteTheme(
        val colorBackground: Int,
        val colorNoteBackground: Int,
        val colorNoteColorBar: Int,
        val colorNoteUnderline: Int,
        val colorToolbar: Int,
        val colorToolbarTextPrimary: Int,
        val colorToolbarTextSecondary: Int,
        val colorTextPrimary: Int,
        val colorTextSecondary: Int,
        val colorTextTertiary: Int,
        val colorAccent: Int
) {

    enum class Themes(val displayName: String, val resourceId: Int) {
        LIGHT("Light", R.style.Light),
        DARK("Dark", R.style.Dark),
        COBALT("Cobalt", R.style.Cobalt),
        BLACK("Black", R.style.Black),
        WHITE("White", R.style.White),
        AZURE("Azure", R.style.Azure),
        CHILI("Tomato", R.style.Tomato),
        SAGE("Sage", R.style.Sage),
        OLIVE("Olive", R.style.Olive);

        companion object {
            fun fromName(name: String): Themes = values().firstOrNull { it.displayName == name } ?: values()[0]
        }
    }

    companion object {

        // Note: Undocumented obtainStyledAttributes() bug! This list of resources MUST be in
        //       alphabetical order. Any attributes that are out of place will fail the getColor()
        //       call below.
        private val ATTRIBUTES = listOf(
                R.attr.themeAccent,
                R.attr.themeBackground,
                R.attr.themeNoteBackground,
                R.attr.themeNoteColorBar,
                R.attr.themeNoteUnderline,
                R.attr.themeTextPrimary,
                R.attr.themeTextSecondary,
                R.attr.themeTextTertiary,
                R.attr.themeToolbar,
                R.attr.themeToolbarTextPrimary,
                R.attr.themeToolbarTextSecondary
        )

        fun default(context: Context): CrystalNoteTheme = CrystalNoteTheme(
                ContextCompat.getColor(context, R.color.starscapeBackdropLight),
                ContextCompat.getColor(context, R.color.starscapeBackdropDark),
                ContextCompat.getColor(context, R.color.starscapeAccent),
                ContextCompat.getColor(context, R.color.darkDivider),
                ContextCompat.getColor(context, R.color.starscapeBackdropDark),
                ContextCompat.getColor(context, R.color.starscapeTextPrimary),
                ContextCompat.getColor(context, R.color.starscapeTextSecondary),
                ContextCompat.getColor(context, R.color.starscapeTextPrimary),
                ContextCompat.getColor(context, R.color.starscapeTextSecondary),
                ContextCompat.getColor(context, R.color.starscapeTextTertiary),
                ContextCompat.getColor(context, R.color.starscapeAccent)
        )

        fun fromCurrentTheme(context: Context): CrystalNoteTheme {
            val typedArray = context.obtainStyledAttributes(ATTRIBUTES.toIntArray())

            return  getThemeFromTypedArray(typedArray)
        }

        fun fromThemeName(context: Context, themeName: String): CrystalNoteTheme {
            val typedArray = context.obtainStyledAttributes(
                    Themes.fromName(themeName).resourceId,
                    ATTRIBUTES.toIntArray()
            )

            return  getThemeFromTypedArray(typedArray)

        }

        fun getNameFromTheme(context: Context, theme: CrystalNoteTheme): String {
            for (testTheme in Themes.values()) {
                if (fromThemeName(context, testTheme.displayName).equals(theme)) {
                    return testTheme.displayName
                }
            }

            return ""
        }

        private fun getThemeFromTypedArray(typedArray: TypedArray): CrystalNoteTheme {

            // Parse Theme Colors
            val colorBackground = parseColor(typedArray, ATTRIBUTES, R.attr.themeBackground)
            val colorNoteBackground = parseColor(typedArray, ATTRIBUTES, R.attr.themeNoteBackground)
            val colorNoteColorBar = parseColor(typedArray, ATTRIBUTES, R.attr.themeNoteColorBar)
            val colorNoteUnderline = parseColor(typedArray, ATTRIBUTES, R.attr.themeNoteUnderline)
            val colorToolbar = parseColor(typedArray, ATTRIBUTES, R.attr.themeToolbar)
            val colorToolbarTextPrimary = parseColor(typedArray, ATTRIBUTES, R.attr.themeToolbarTextPrimary)
            val colorToolbarTextSecondary = parseColor(typedArray, ATTRIBUTES, R.attr.themeToolbarTextSecondary)
            val colorTextPrimary = parseColor(typedArray, ATTRIBUTES, R.attr.themeTextPrimary)
            val colorTextSecondary = parseColor(typedArray, ATTRIBUTES, R.attr.themeTextSecondary)
            val colorTextTertiary = parseColor(typedArray, ATTRIBUTES, R.attr.themeTextTertiary)
            val colorAccent = parseColor(typedArray, ATTRIBUTES, R.attr.themeAccent)
            typedArray.recycle()

            // Return Theme
            return CrystalNoteTheme(
                    colorBackground,
                    colorNoteBackground,
                    colorNoteColorBar,
                    colorNoteUnderline,
                    colorToolbar,
                    colorToolbarTextPrimary,
                    colorToolbarTextSecondary,
                    colorTextPrimary,
                    colorTextSecondary,
                    colorTextTertiary,
                    colorAccent
            )
        }

        private fun parseColor(typedArray: TypedArray, attributes: List<Any>, id: Int): Int {
            return typedArray.getColor(attributes.indexOf(id), Color.TRANSPARENT)
        }
    }
}