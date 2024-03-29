package com.xephorium.crystalnote.ui.settings

import com.xephorium.crystalnote.data.model.DateType


class SettingsPresenter : SettingsContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: SettingsContract.View) {
        super.attachView(view)

        theme = sharedPreferencesRepository.getTheme()
        notePreviewLines = sharedPreferencesRepository.getNotePreviewLines()
        noteDateType = sharedPreferencesRepository.getNoteDateType()
        colorBarEnabled = sharedPreferencesRepository.getNoteColorBarEnabled()
        themedBarEnabled = sharedPreferencesRepository.getNoteThemedBarEnabled()
        todayHeaderEnabled = sharedPreferencesRepository.getTodayHeaderEnabled()
        homeOptionsEnabled = sharedPreferencesRepository.getHomeOptionsEnabled()
        noteUnderlineEnabled = sharedPreferencesRepository.getNoteUnderlineEnabled()
        noteScrollButtonEnabled = sharedPreferencesRepository.getScrollButtonEnabled()
        noteColorOrbEnabled = sharedPreferencesRepository.getColorOrbEnabled()
        useMonospacedFont = sharedPreferencesRepository.getMonospacedFontEnabled()

        view.populateTheme(theme)
        view.populateHomePreviewLines(notePreviewLines)
        view.populateHomeDateType(noteDateType)
        view.populateHomeColorBarSwitch(colorBarEnabled)
        view.populateHomeThemedColorBarSwitch(themedBarEnabled)
        view.populateHomeTodayHeaderSwitch(todayHeaderEnabled)
        view.populateHomeOptionsSwitch(homeOptionsEnabled)
        view.populateNoteUnderlineSwitch(noteUnderlineEnabled)
        view.populateNoteScrollButtonSwitch(noteScrollButtonEnabled)
        view.populateNoteColorOrbSwitch(noteColorOrbEnabled)
        view.populateNoteMonospaceSwitch(useMonospacedFont)

        view.setHomePreviewLines(notePreviewLines)
        view.setHomePreviewDateType(noteDateType)
        view.setHomePreviewColorBarVisibility(colorBarEnabled)
        view.setHomePreviewColorBarThemed(themedBarEnabled)
        view.setHomePreviewHeaderVisibility(todayHeaderEnabled)
        view.setHomePreviewOptionsVisibility(homeOptionsEnabled)
        view.setNotePreviewUnderlineVisibility(noteUnderlineEnabled)
        view.setNoteScrollButtonVisibility(noteScrollButtonEnabled)
        view.setNoteColorOrbVisibility(noteColorOrbEnabled)
        view.setNoteMonospaceFontVisibility(useMonospacedFont)
    }


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleThemeChange(newTheme: String) {
        theme = newTheme
        view?.setPreviewTheme(theme)
    }

    override fun handleHomeLinesChange(lines: Int) {
        notePreviewLines = lines
        sharedPreferencesRepository.setNotePreviewLines(notePreviewLines)
        view?.setHomePreviewLines(notePreviewLines)
    }

    override fun handleHomeDateTypeChange(dateType: DateType) {
        noteDateType = dateType
        sharedPreferencesRepository.setNoteDateType(noteDateType)
        view?.setHomePreviewDateType(noteDateType)
    }

    override fun handleHomeColorBarToggle(checked: Boolean) {
        colorBarEnabled = checked
        sharedPreferencesRepository.setNoteColorBarEnabled(colorBarEnabled)
        view?.setHomePreviewColorBarVisibility(colorBarEnabled)
    }

    override fun handleHomeThemedColorBarToggle(checked: Boolean) {
        themedBarEnabled = checked
        sharedPreferencesRepository.setNoteThemedBarEnabled(themedBarEnabled)
        view?.setHomePreviewColorBarThemed(checked)
    }

    override fun handleHomeTodayHeaderToggle(checked: Boolean) {
        todayHeaderEnabled = checked
        sharedPreferencesRepository.setTodayHeaderEnabled(todayHeaderEnabled)
        view?.setHomePreviewHeaderVisibility(todayHeaderEnabled)
    }

    override fun handleHomeOptionsToggle(checked: Boolean) {
        homeOptionsEnabled = checked
        sharedPreferencesRepository.setHomeOptionsEnabled(homeOptionsEnabled)
        view?.setHomePreviewOptionsVisibility(homeOptionsEnabled)
    }

    override fun handleNoteUnderlineToggle(checked: Boolean) {
        noteUnderlineEnabled = checked
        sharedPreferencesRepository.setNoteUnderlineEnabled(noteUnderlineEnabled)
        view?.setNotePreviewUnderlineVisibility(noteUnderlineEnabled)
    }

    override fun handleNoteScrollButtonToggle(checked: Boolean) {
        noteScrollButtonEnabled = checked
        sharedPreferencesRepository.setScrollButtonEnabled(noteScrollButtonEnabled)
        view?.setNoteScrollButtonVisibility(noteScrollButtonEnabled)
    }

    override fun handleNoteColorOrbToggle(checked: Boolean) {
        noteColorOrbEnabled = checked
        sharedPreferencesRepository.setColorOrbEnabled(noteColorOrbEnabled)
        view?.setNoteColorOrbVisibility(noteColorOrbEnabled)
    }

    override fun handleNoteMonospaceToggle(checked: Boolean) {
        useMonospacedFont = checked
        sharedPreferencesRepository.setMonospacedFontEnabled(useMonospacedFont)
        view?.setNoteMonospaceFontVisibility(useMonospacedFont)
    }

    override fun handleApplyThemeClick() {
        if (hasSelectedThemeChanged()) {
            sharedPreferencesRepository.setTheme(theme)
            view?.refreshScreen()
        }
    }

    override fun handleBackClick() {
        if (hasSelectedThemeChanged()) {
            view?.showDiscardThemeChangeDialog()
        } else {
            view?.navigateBack()
        }
    }

    override fun handleBackConfirm() {
        view?.navigateBack()
    }


    /*--- Private Methods ---*/

    private fun hasSelectedThemeChanged(): Boolean {
        return sharedPreferencesRepository.getTheme() != theme
    }
}
