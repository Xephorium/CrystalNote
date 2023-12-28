package com.xephorium.crystalnote.ui.settings

import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.DateType
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun populateTheme(theme: String)
        fun populateNotePreviewLines(lines: Int)
        fun populateNoteDateType(dateType: DateType)
        fun populateNoteColorBarSwitch(checked: Boolean)
        fun populateThemedColorBarSwitch(checked: Boolean)
        fun populateTodayHeaderSwitch(checked: Boolean)
        fun populateNoteUnderlineSwitch(checked: Boolean)
        fun populateMonospaceSwitch(checked: Boolean)

        fun setPreviewTheme(theme: String)
        fun setPreviewLines(lines: Int)
        fun setPreviewDateType(type: DateType)
        fun setPreviewColorBarVisibility(visible: Boolean)
        fun setPreviewColorBarThemed(themed: Boolean)
        fun setPreviewHeaderVisibility(visible: Boolean)

        fun showNavigationDrawer()
        fun showDiscardThemeChangeDialog()
        fun refreshScreen()
        fun navigateBack()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        lateinit var theme: String
        var notePreviewLines: Int = 1
        lateinit var noteDateType: DateType
        var colorBarEnabled: Boolean = true
        var themedBarEnabled: Boolean = true
        var todayHeaderEnabled: Boolean = true
        var noteUnderlineEnabled: Boolean = true
        var useMonospacedFont: Boolean = true

        abstract fun handleMenuButtonClick()
        abstract fun handleThemeChange(newTheme: String)
        abstract fun handleNoteLinesChange(lines: Int)
        abstract fun handleNoteDateTypeChange(dateType: DateType)
        abstract fun handleNoteColorBarToggle(checked: Boolean)
        abstract fun handleThemedColorBarToggle(checked: Boolean)
        abstract fun handleTodayHeaderToggle(checked: Boolean)
        abstract fun handleNoteUnderlineToggle(checked: Boolean)
        abstract fun handleMonospaceToggle(checked: Boolean)
        abstract fun handleApplyThemeClick()
        abstract fun handleBackClick()
        abstract fun handleBackConfirm()
    }
}
