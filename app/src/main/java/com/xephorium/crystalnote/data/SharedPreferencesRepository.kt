package com.xephorium.crystalnote.data

import android.content.Context

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton

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

    fun setSelectedDrawerButton(button: DrawerButton) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putString(SELECTED_DRAWER_BUTTON_NAME, button.name)
        editor.apply()
    }

    fun getSelectedDrawerButton(): DrawerButton? {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val buttonName = prefs.getString(SELECTED_DRAWER_BUTTON_NAME, "")
        return DrawerButton.values().firstOrNull { it.name == buttonName }
    }


    /*--- Constants ---*/

    companion object {
        private const val APP_PRIMARY_KEY = "CrystalNotePreferences"
        private const val DISPLAY_NOTE_NAME = "DisplayNoteName"
        private const val SELECTED_DRAWER_BUTTON_NAME = "SelectedDrawerButtonName"
    }
}
