package com.xephorium.crystalnote.data.utility

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import kotlin.math.max
import kotlin.math.min

object ColorUtility {


    /*--- Utility Methods ---*/

    fun applyTransparency(color: Int, transparency: Transparency): Int {
        return ColorUtils.setAlphaComponent(color, ((1.0 - transparency.value) * 255).toInt())
    }

    fun calculateContrastRatio(colorOne: Int, colorTwo: Int): Double {
        val colorOneLuminance =
            (Color.red(colorOne) + Color.green(colorOne) + Color.blue(colorOne))  / 3.0
        val colorTwoLuminance =
            (Color.red(colorTwo) + Color.green(colorTwo) + Color.blue(colorTwo))  / 3.0

        val bright = max(colorOneLuminance, colorTwoLuminance)
        val dark = min(colorOneLuminance, colorTwoLuminance)

        return bright / dark
    }

    fun areEqualHexColors(hexOne: String, hexTwo: String): Boolean {
        return getIntColorFromHexString(hexOne) == getIntColorFromHexString(hexTwo)
    }


    /*--- Conversion Methods ---*/

    fun getIntColorFromHexString(hex: String): Int? {
        var color: Int? = null
        try {
            var sanitizedHex = hex.replace("#", "")
            sanitizedHex = convertThreeDigitHexToSix(sanitizedHex)
            color = Color.parseColor("#$sanitizedHex")
        } catch (exception: Exception) { /* Do Nothing */ }
        return color
    }


    /*--- Private Methods ---*/

    private fun convertThreeDigitHexToSix(hex: String): String {
        return if(hex.length == 3) {
            StringBuilder()
                .append(hex[0])
                .append(hex[0])
                .append(hex[1])
                .append(hex[1])
                .append(hex[2])
                .append(hex[2])
                .toString()
        } else {
            hex
        }
    }
}