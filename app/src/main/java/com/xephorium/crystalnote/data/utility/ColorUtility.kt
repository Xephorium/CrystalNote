package com.xephorium.crystalnote.data.utility

import android.graphics.Color
import kotlin.math.max
import kotlin.math.min

object ColorUtility {

    fun calculateContrastRatio(colorOne: Int, colorTwo: Int): Double {
        val colorOneLuminance =
            (Color.red(colorOne) + Color.green(colorOne) + Color.blue(colorOne))  / 3.0
        val colorTwoLuminance =
            (Color.red(colorTwo) + Color.green(colorTwo) + Color.blue(colorTwo))  / 3.0

        val bright = max(colorOneLuminance, colorTwoLuminance)
        val dark = min(colorOneLuminance, colorTwoLuminance)

        return bright / dark
    }
}