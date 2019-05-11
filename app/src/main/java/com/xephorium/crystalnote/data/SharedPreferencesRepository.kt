package com.xephorium.crystalnote.data

import android.content.Context

import com.xephorium.crystalnote.data.model.Note

class SharedPreferencesRepository(private val context: Context) {


    /*--- Public Methods ---*/

    fun setDisplayNoteName(note: Note) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putString(DISPLAY_NOTE_NAME, note.name)
        editor.apply()
    }

    fun getDisplayNoteName(): String? {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)

        return prefs.getString(DISPLAY_NOTE_NAME, null)
    }


    /*--- Constants ---*/

    companion object {
        private const val APP_PRIMARY_KEY = "CrystalNotePreferences"
        private const val DISPLAY_NOTE_NAME = "DisplayNoteName"
    }
}
