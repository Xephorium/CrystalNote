package com.xephorium.crystalnote.ui.settings

import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.DateType
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun populateTheme(theme: String)
        fun populateNotePreviewLines(lines: Int)
        fun populateNoteDateType(dateType: DateType)
        fun populateNoteColorsSwitch(checked: Boolean)
        fun populateTodayHeaderSwitch(checked: Boolean)

        fun setPreviewTheme(theme: String)
        fun setPreviewLines(lines: Int)
        fun setPreviewDateType(type: DateType)
        fun setPreviewColorBoxVisibility(visible: Boolean)
        fun setPreviewHeaderVisibility(visible: Boolean)

        fun showNavigationDrawer()
        fun showDiscardChangesDialog()
        fun refreshScreen()
        fun navigateBack()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var theme: String
        var notePreviewLines: Int = 1
        lateinit var noteDateType: DateType
        var noteColorsEnabled: Boolean = true
        var todayHeaderEnabled: Boolean = true

        abstract fun handleMenuButtonClick()
        abstract fun handleThemeChange(newTheme: String)
        abstract fun handleNoteLinesChange(lines: Int)
        abstract fun handleNoteDateTypeChange(dateType: DateType)
        abstract fun handleNoteColorsToggle(checked: Boolean)
        abstract fun handleTodayHeaderToggle(checked: Boolean)
        abstract fun handleSaveClick()
        abstract fun handleBackClick()
        abstract fun handleBackConfirm()
    }
}
