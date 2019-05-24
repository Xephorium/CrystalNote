package com.xephorium.crystalnote.data.model

import android.content.Context
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
            val a = listOf(
                    R.attr.themeBackground,
                    R.attr.themeNoteBackground,
                    R.attr.themeToolbar,
                    R.attr.themeToolbarTextPrimary,
                    R.attr.themeToolbarTextSecondary,
                    R.attr.themeTextPrimary,
                    R.attr.themeTextSecondary,
                    R.attr.themeTextTertiary,
                    R.attr.themeAccent
            )
            val ta = context.obtainStyledAttributes(Themes.fromName(themeName).resourceId, a.toIntArray())

            // Parse Theme Colors
            val colorBackground = ta.getColor(a.indexOf(R.attr.themeBackground), Color.WHITE)
            val colorNoteBackground = ta.getColor(a.indexOf(R.attr.themeNoteBackground), Color.WHITE)
            val colorToolbar = ta.getColor(a.indexOf(R.attr.themeToolbar), Color.WHITE)
            val colorToolbarTextPrimary = ta.getColor(a.indexOf(R.attr.themeToolbarTextPrimary), Color.WHITE)
            val colorToolbarTextSecondary = ta.getColor(a.indexOf(R.attr.themeToolbarTextSecondary), Color.WHITE)
            val colorTextPrimary = ta.getColor(a.indexOf(R.attr.themeTextPrimary), Color.WHITE)
            val colorTextSecondary = ta.getColor(a.indexOf(R.attr.themeTextSecondary), Color.WHITE)
            val colorTextTertiary = ta.getColor(a.indexOf(R.attr.themeTextTertiary), Color.WHITE)
            val colorAccent = ta.getColor(a.indexOf(R.attr.themeAccent), Color.WHITE)
            ta.recycle()

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
    }
}