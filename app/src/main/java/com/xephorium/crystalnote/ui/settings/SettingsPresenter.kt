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
        useMonospacedFont = sharedPreferencesRepository.getMonospacedFontEnabled()

        this.view?.populateTheme(theme)
        this.view?.populateNotePreviewLines(notePreviewLines)
        this.view?.populateNoteDateType(noteDateType)
        this.view?.populateNoteColorBarSwitch(colorBarEnabled)
        this.view?.populateThemedColorBarSwitch(themedBarEnabled)
        this.view?.populateTodayHeaderSwitch(todayHeaderEnabled)
        this.view?.populateNoteUnderlineSwitch(noteUnderlineEnabled)
        this.view?.populateMonospaceSwitch(useMonospacedFont)

        this.view?.setPreviewLines(notePreviewLines)
        this.view?.setPreviewDateType(noteDateType)
        this.view?.setPreviewColorBarVisibility(colorBarEnabled)
        this.view?.setPreviewColorBarThemed(themedBarEnabled)
        this.view?.setPreviewHeaderVisibility(todayHeaderEnabled)
    }


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleThemeChange(newTheme: String) {
        theme = newTheme
        view?.setPreviewTheme(theme)
    }

    override fun handleNoteLinesChange(lines: Int) {
        notePreviewLines = lines
        sharedPreferencesRepository.setNotePreviewLines(notePreviewLines)
        view?.setPreviewLines(notePreviewLines)
    }

    override fun handleNoteDateTypeChange(dateType: DateType) {
        noteDateType = dateType
        sharedPreferencesRepository.setNoteDateType(noteDateType)
        view?.setPreviewDateType(noteDateType)
    }

    override fun handleNoteColorBarToggle(checked: Boolean) {
        colorBarEnabled = checked
        sharedPreferencesRepository.setNoteColorBarEnabled(colorBarEnabled)
        view?.setPreviewColorBarVisibility(colorBarEnabled)
    }

    override fun handleThemedColorBarToggle(checked: Boolean) {
        themedBarEnabled = checked
        sharedPreferencesRepository.setNoteThemedBarEnabled(themedBarEnabled)
        view?.setPreviewColorBarThemed(checked)
    }

    override fun handleTodayHeaderToggle(checked: Boolean) {
        todayHeaderEnabled = checked
        sharedPreferencesRepository.setTodayHeaderEnabled(todayHeaderEnabled)
        view?.setPreviewHeaderVisibility(todayHeaderEnabled)
    }

    override fun handleNoteUnderlineToggle(checked: Boolean) {
        noteUnderlineEnabled = checked
        sharedPreferencesRepository.setNoteUnderlineEnabled(noteUnderlineEnabled)
    }

    override fun handleMonospaceToggle(checked: Boolean) {
        useMonospacedFont = checked
        sharedPreferencesRepository.setMonospacedFontEnabled(useMonospacedFont)
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
