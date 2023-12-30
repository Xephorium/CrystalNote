package com.xephorium.crystalnote.data.repository

import android.content.Context
import com.xephorium.crystalnote.data.model.*

import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton

class SharedPreferencesRepository(private val context: Context) {


    /*--- Public UI Methods ---*/

    fun setTheme(theme: String) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putString(THEME, theme)
        editor.apply()
    }

    fun getTheme(): String {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val backupValue = CrystalNoteTheme.Themes.values()[1].displayName
        return prefs.getString(THEME, backupValue) ?: backupValue
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

    fun setNoteDateType(dateType: DateType) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putInt(NOTE_DATE_TYPE, dateType.ordinal)
        editor.apply()
    }

    fun getNoteDateType(): DateType {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return DateType.values()[prefs.getInt(NOTE_DATE_TYPE, 0)]
    }

    fun setNoteColorBarEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(NOTE_COLOR_BAR_ENABLED, enabled)
        editor.apply()
    }

    fun getNoteColorBarEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(NOTE_COLOR_BAR_ENABLED, true)
    }

    fun setNoteThemedBarEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(NOTE_COLOR_BAR_THEME_ENABLED, enabled)
        editor.apply()
    }

    fun getNoteThemedBarEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(NOTE_COLOR_BAR_THEME_ENABLED, false)
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

    fun setNoteUnderlineEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(NOTE_UNDERLINE_ENABLED, enabled)
        editor.apply()
    }

    fun getNoteUnderlineEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(NOTE_UNDERLINE_ENABLED, false)
    }

    fun setScrollButtonEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(SCROLL_BUTTON_ENABLED, enabled)
        editor.apply()
    }

    fun getScrollButtonEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(SCROLL_BUTTON_ENABLED, true)
    }

    fun setColorOrbEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(COLOR_ORB_ENABLED, enabled)
        editor.apply()
    }

    fun getColorOrbEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(COLOR_ORB_ENABLED, true)
    }

    fun setMonospacedFontEnabled(enabled: Boolean) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(MONOSPACED_FONT, enabled)
        editor.apply()
    }

    fun getMonospacedFontEnabled(): Boolean {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(MONOSPACED_FONT, false)
    }


    /*--- Public State Methods ---*/

    fun createNewWidgetState(widgetId: Int) {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val widgetStateList = WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()

        widgetStateList.setNoteIdForWidget(widgetId, Note.NO_NOTE)

        editor.putString(WIDGET_STATE_LIST, widgetStateList.toString())
        editor.apply()
    }

    fun setNoteIdForWidget(widgetId: Int, noteId: Int) {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val widgetStateList = WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()

        widgetStateList.setNoteIdForWidget(widgetId, noteId)

        editor.putString(WIDGET_STATE_LIST, widgetStateList.toString())
        editor.apply()
    }

    fun getNoteIdForWidget(widgetId: Int): Int? {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val widgetStateList = WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")

        return widgetStateList.getNoteIdForWidget(widgetId)
    }

    fun getWidgetState(widgetId: Int): WidgetState? {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val widgetStateList = WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")

        return widgetStateList.getWidgetState(widgetId)
    }

    fun removeWidgetState(widgetId: Int) {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        val widgetStateList = WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()

        widgetStateList.removeWidgetState(widgetId)

        editor.putString(WIDGET_STATE_LIST, widgetStateList.toString())
        editor.apply()
    }

    fun getWidgetStateList(): WidgetStateList {
        val prefs = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE)
        return WidgetStateList(prefs.getString(WIDGET_STATE_LIST, "") ?: "")
    }

    fun setWidgetStateList(widgetStateList: WidgetStateList) {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()

        editor.putString(WIDGET_STATE_LIST, widgetStateList.toString())
        editor.apply()
    }

    fun removeAllWidgetStates() {
        val editor = context.getSharedPreferences(APP_PRIMARY_KEY, Context.MODE_PRIVATE).edit()
        editor.putString(WIDGET_STATE_LIST, "")
        editor.apply()
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
        private const val THEME = "Theme"
        private const val NOTE_PREVIEW_LINES = "NotePreviewLines"
        private const val NOTE_DATE_TYPE = "NoteDateType"
        private const val NOTE_COLOR_BAR_ENABLED = "NoteColorBarEnabled"
        private const val NOTE_COLOR_BAR_THEME_ENABLED = "NoteColorBarThemeEnabled"
        private const val TODAY_HEADER_ENABLED = "TodayHeaderEnabled"
        private const val NOTE_UNDERLINE_ENABLED = "NoteUnderlineEnabled"
        private const val SCROLL_BUTTON_ENABLED = "ScrollButtonEnabled"
        private const val COLOR_ORB_ENABLED = "ColorOrbEnabled"
        private const val MONOSPACED_FONT = "MonospacedFont"
        private const val WIDGET_STATE_LIST = "WidgetStateList"
        private const val SELECTED_DRAWER_BUTTON_NAME = "SelectedDrawerButtonName"
    }
}
