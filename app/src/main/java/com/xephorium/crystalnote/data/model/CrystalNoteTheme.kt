package com.xephorium.crystalnote.data.model

import android.content.Context


class CrystalNoteTheme {

    enum class Themes(val displayName: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SAGE("Sage")
    }

    companion object {
        fun fromSystemTheme(context: Context, themeName: String): CrystalNoteTheme {

            return CrystalNoteTheme()
        }
    }
}