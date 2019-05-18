package com.xephorium.crystalnote.data

import android.content.Context

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton

class SharedPreferencesRepository(private val context: Context) {


    /*--- Public Methods ---*/

    fun setNoteColorsEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(NOTE_COLORS_ENABLED, enabled)
        editor.apply()
    }

    fun getNoteColorsEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(NOTE_COLORS_ENABLED, true)
    }

    fun setNotePreviewLines(lines: Int) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putInt(NOTE_PREVIEW_LINES, lines)
        editor.apply()
    }

    fun getNotePreviewLines(): Int {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getInt(NOTE_PREVIEW_LINES, 1)
    }

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

    fun setTodayHeaderEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(TODAY_HEADER_ENABLED, enabled)
        editor.apply()
    }

    fun getTodayHeaderEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(TODAY_HEADER_ENABLED, true)
    }


    /*--- Constants ---*/

    companion object {
        private const val APP_PRIMARY_KEY = "CrystalNotePreferences"
        private const val NOTE_COLORS_ENABLED = "CustomNoteColorsEnabled"
        private const val NOTE_PREVIEW_LINES = "NotePreviewLines"
        private const val DISPLAY_NOTE_NAME = "DisplayNoteName"
        private const val SELECTED_DRAWER_BUTTON_NAME = "SelectedDrawerButtonName"
        private const val TODAY_HEADER_ENABLED = "TodayHeaderEnabled"
    }
}
