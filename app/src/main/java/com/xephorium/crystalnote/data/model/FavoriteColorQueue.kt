package com.xephorium.crystalnote.data.model

import android.graphics.Color
import com.xephorium.crystalnote.data.utility.ColorUtility
import java.lang.StringBuilder

/*
  Favorite Color Queue

  The FavoriteColorQueue class represents a queue of eight colors the user
  has saved. It encapsulates the logic of converting this queue to and from
  a string to be stored in Shared Preferences.

  Shared Preferences String Format:

    "ffffff 000000 123abc ..."
*/

class FavoriteColorQueue(string: String) {


    /*--- Variable Declarations ---*/

    private var colors: ArrayDeque<Int> = ArrayDeque()


    /*--- Constructor ---*/

    init {
        parseString(string)
    }


    /*--- Public Methods ---*/

    fun getAll() : List<Int> {
        return colors
    }

    fun addColor(color: Int) {
        if (colors.size < MAX_FAVORITES) {
            colors.addFirst(color)
        } else {
            colors.removeAt(colors.lastIndex)
            colors.addFirst(color)
        }
    }

    fun removeColor(color: Int) {
        colors.remove(color)
    }

    override fun toString() : String {
        val builder = StringBuilder()
        val upperBound = if (colors.size < MAX_FAVORITES) colors.size else MAX_FAVORITES
        for (x in 0..<upperBound-1) {
            builder.append(ColorUtility.getHexStringFromIntColor(colors[x]))
            builder.append(STRING_DELIMITER)
        }
        if(colors.size > 0) builder.append(ColorUtility.getHexStringFromIntColor(colors[upperBound - 1]))

        return builder.toString()
    }

    fun copy() : FavoriteColorQueue {
        return FavoriteColorQueue(this.toString())
    }


    /*--- Private Methods ---*/

    private fun containsColor(color: Int): Boolean {
        return colors.any { it == color}
    }

    private fun parseString(string: String) {

        // Split Into Individual Hex Color Substrings
        val splitColors = string.split(Regex(STRING_DELIMITER)).map { it.trim() }

        // Store Colors
        for (c in splitColors) {
            if (c.isNotBlank()) colors.add(Color.parseColor("#$c"))
        }
    }


    /*--- Constants ---*/

    companion object {
        const val MAX_FAVORITES = 8
        const val STRING_DELIMITER = " "
    }

}