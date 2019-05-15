package com.xephorium.crystalnote.data.validation

object NoteValidator {


    /*--- Constants ---*/

    private const val RESERVED_CHARACTERS = "|\\?*<\":>+[]/'"


    /*--- Validation Methods ---*/

    fun isValidNoteName(name: String): Boolean {
        if (name.isBlank()) return false
        RESERVED_CHARACTERS.forEach { character -> if (name.contains(character)) return false }
        return true
    }
}