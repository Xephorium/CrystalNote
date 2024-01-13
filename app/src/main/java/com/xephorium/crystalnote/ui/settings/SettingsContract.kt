package com.xephorium.crystalnote.ui.settings

import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.DateType
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SettingsContract {

    interface View : BaseView {
        fun populateTheme(theme: String)
        fun populateHomePreviewLines(lines: Int)
        fun populateHomeDateType(dateType: DateType)
        fun populateHomeColorBarSwitch(checked: Boolean)
        fun populateHomeThemedColorBarSwitch(checked: Boolean)
        fun populateHomeTodayHeaderSwitch(checked: Boolean)
        fun populateHomeOptionsSwitch(checked: Boolean)
        fun populateNoteUnderlineSwitch(checked: Boolean)
        fun populateNoteScrollButtonSwitch(checked: Boolean)
        fun populateNoteColorOrbSwitch(checked: Boolean)
        fun populateNoteMonospaceSwitch(checked: Boolean)

        fun setPreviewTheme(theme: String)
        fun setHomePreviewLines(lines: Int)
        fun setHomePreviewDateType(type: DateType)
        fun setHomePreviewColorBarVisibility(visible: Boolean)
        fun setHomePreviewColorBarThemed(themed: Boolean)
        fun setHomePreviewHeaderVisibility(visible: Boolean)
        fun setHomePreviewOptionsVisibility(visible: Boolean)
        fun setNotePreviewUnderlineVisibility(visible: Boolean)
        fun setNoteScrollButtonVisibility(visible: Boolean)
        fun setNoteColorOrbVisibility(visible: Boolean)
        fun setNoteMonospaceFontVisibility(visible: Boolean)

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
        var homeOptionsEnabled: Boolean = true
        var noteUnderlineEnabled: Boolean = false
        var noteScrollButtonEnabled: Boolean = true
        var noteColorOrbEnabled: Boolean = true
        var useMonospacedFont: Boolean = true

        abstract fun handleMenuButtonClick()
        abstract fun handleThemeChange(newTheme: String)
        abstract fun handleHomeLinesChange(lines: Int)
        abstract fun handleHomeDateTypeChange(dateType: DateType)
        abstract fun handleHomeColorBarToggle(checked: Boolean)
        abstract fun handleHomeThemedColorBarToggle(checked: Boolean)
        abstract fun handleHomeTodayHeaderToggle(checked: Boolean)
        abstract fun handleHomeOptionsToggle(checked: Boolean)
        abstract fun handleNoteUnderlineToggle(checked: Boolean)
        abstract fun handleNoteScrollButtonToggle(checked: Boolean)
        abstract fun handleNoteColorOrbToggle(checked: Boolean)
        abstract fun handleNoteMonospaceToggle(checked: Boolean)
        abstract fun handleApplyThemeClick()
        abstract fun handleBackClick()
        abstract fun handleBackConfirm()
    }
}
