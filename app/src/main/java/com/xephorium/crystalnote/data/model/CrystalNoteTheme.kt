package com.xephorium.crystalnote.data.model

import android.content.Context
import android.content.res.TypedArray
import com.xephorium.crystalnote.R
import android.graphics.Color
import androidx.core.content.ContextCompat


data class CrystalNoteTheme(
        val colorBackground: Int,
        val colorNoteBackground: Int,
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
        SAGE("Sage", R.style.Sage);

        companion object {
            fun fromName(name: String): Themes = values().firstOrNull { it.displayName == name } ?: values()[0]
        }
    }

    companion object {
        fun default(context: Context): CrystalNoteTheme = CrystalNoteTheme(
                ContextCompat.getColor(context, R.color.lightBackground),
                ContextCompat.getColor(context, R.color.lightNoteBackground),
                ContextCompat.getColor(context, R.color.lightToolbar),
                ContextCompat.getColor(context, R.color.lightToolbarTextPrimary),
                ContextCompat.getColor(context, R.color.lightToolbarTextSecondary),
                ContextCompat.getColor(context, R.color.lightTextPrimary),
                ContextCompat.getColor(context, R.color.lightTextSecondary),
                ContextCompat.getColor(context, R.color.lightTextTertiary),
                ContextCompat.getColor(context, R.color.lightAccent)
        )

        fun fromThemeName(context: Context, themeName: String): CrystalNoteTheme {

            // Get Theme Attributes
            // Note: Undocumented obtainStyledAttributes() bug! This list of resources MUST be in
            //       alphabetical order. Any attributes that are out of place will fail the getColor()
            //       call below.
            val attributes = listOf(
                    R.attr.themeAccent,
                    R.attr.themeBackground,
                    R.attr.themeNoteBackground,
                    R.attr.themeTextPrimary,
                    R.attr.themeTextSecondary,
                    R.attr.themeTextTertiary,
                    R.attr.themeToolbar,
                    R.attr.themeToolbarTextPrimary,
                    R.attr.themeToolbarTextSecondary
            )
            val typedArray =
                    context.obtainStyledAttributes(Themes.fromName(themeName).resourceId, attributes.toIntArray())

            // Parse Theme Colors
            val colorBackground = parseColor(typedArray, attributes, R.attr.themeBackground)
            val colorNoteBackground = parseColor(typedArray, attributes, R.attr.themeNoteBackground)
            val colorToolbar = parseColor(typedArray, attributes, R.attr.themeToolbar)
            val colorToolbarTextPrimary = parseColor(typedArray, attributes, R.attr.themeToolbarTextPrimary)
            val colorToolbarTextSecondary = parseColor(typedArray, attributes, R.attr.themeToolbarTextSecondary)
            val colorTextPrimary = parseColor(typedArray, attributes, R.attr.themeTextPrimary)
            val colorTextSecondary = parseColor(typedArray, attributes, R.attr.themeTextSecondary)
            val colorTextTertiary = parseColor(typedArray, attributes, R.attr.themeTextTertiary)
            val colorAccent = parseColor(typedArray, attributes, R.attr.themeAccent)
            typedArray.recycle()

            // Return Theme
            return CrystalNoteTheme(
                    colorBackground,
                    colorNoteBackground,
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