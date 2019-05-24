package com.xephorium.crystalnote.data.model

import android.content.Context
import com.xephorium.crystalnote.R
import android.graphics.Color
import android.util.Log


data class CrystalNoteTheme(val themeColor: Int) {

    enum class Themes(val displayName: String, val resourceId: Int) {
        LIGHT("Light", R.style.Light),
        DARK("Dark", R.style.Dark),
        SAGE("Sage", R.style.Sage);

        companion object {
            fun fromName(name: String): Themes = values().firstOrNull { it.displayName == name } ?: Themes.values()[0]
        }
    }

    companion object {
        fun fromSystemTheme(context: Context, themeName: String): CrystalNoteTheme {

            val themeAttributes = listOf(
                    R.attr.themeToolbar
            )
            val typedAttributes = context.obtainStyledAttributes(
                    Themes.fromName(themeName).resourceId,
                    themeAttributes.toIntArray()
            )

            val color = typedAttributes.getColor(themeAttributes.indexOf(R.attr.themeToolbar), Color.WHITE)

            Log.d("Aardvark", "Value: " + Integer.toHexString(color))

            typedAttributes.recycle()

            return CrystalNoteTheme(color)
        }
    }
}