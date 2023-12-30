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
        noteUnderlineEnabled = sharedPreferencesRepository.getNoteUnderlineEnabled()
        noteScrollButtonEnabled = sharedPreferencesRepository.getScrollButtonEnabled()
        noteColorOrbEnabled = sharedPreferencesRepository.getColorOrbEnabled()
        useMonospacedFont = sharedPreferencesRepository.getMonospacedFontEnabled()

        this.view?.populateTheme(theme)
        this.view?.populateHomePreviewLines(notePreviewLines)
        this.view?.populateHomeDateType(noteDateType)
        this.view?.populateHomeColorBarSwitch(colorBarEnabled)
        this.view?.populateHomeThemedColorBarSwitch(themedBarEnabled)
        this.view?.populateHomeTodayHeaderSwitch(todayHeaderEnabled)
        this.view?.populateNoteUnderlineSwitch(noteUnderlineEnabled)
        this.view?.populateNoteScrollButtonSwitch(noteScrollButtonEnabled)
        this.view?.populateNoteColorOrbSwitch(noteColorOrbEnabled)
        this.view?.populateNoteMonospaceSwitch(useMonospacedFont)

        this.view?.setHomePreviewLines(notePreviewLines)
        this.view?.setHomePreviewDateType(noteDateType)
        this.view?.setHomePreviewColorBarVisibility(colorBarEnabled)
        this.view?.setHomePreviewColorBarThemed(themedBarEnabled)
        this.view?.setHomePreviewHeaderVisibility(todayHeaderEnabled)
        this.view?.setNotePreviewUnderlineVisibility(noteUnderlineEnabled)
        this.view?.setNoteScrollButtonVisibility(noteScrollButtonEnabled)
        this.view?.setNoteColorOrbVisibility(noteColorOrbEnabled)
        this.view?.setNoteMonospaceFontVisibility(useMonospacedFont)
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
