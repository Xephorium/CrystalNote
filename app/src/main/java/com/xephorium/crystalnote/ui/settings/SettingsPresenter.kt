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
        view?.setPreviewLines(notePreviewLines)
    }

    override fun handleNoteDateTypeChange(dateType: DateType) {
        noteDateType = dateType
        view?.setPreviewDateType(noteDateType)
    }

    override fun handleNoteColorBarToggle(checked: Boolean) {
        colorBarEnabled = checked
        view?.setPreviewColorBarVisibility(colorBarEnabled)
    }

    override fun handleThemedColorBarToggle(checked: Boolean) {
        themedBarEnabled = checked
        view?.setPreviewColorBarThemed(checked)
    }

    override fun handleTodayHeaderToggle(checked: Boolean) {
        todayHeaderEnabled = checked
        view?.setPreviewHeaderVisibility(todayHeaderEnabled)
    }

    override fun handleNoteUnderlineToggle(checked: Boolean) {
        noteUnderlineEnabled = checked
    }

    override fun handleMonospaceToggle(checked: Boolean) {
        useMonospacedFont = checked
    }

    override fun handleSaveClick() {
        sharedPreferencesRepository.setNotePreviewLines(notePreviewLines)
        sharedPreferencesRepository.setNoteDateType(noteDateType)
        sharedPreferencesRepository.setNoteColorBarEnabled(colorBarEnabled)
        sharedPreferencesRepository.setNoteThemedBarEnabled(themedBarEnabled)
        sharedPreferencesRepository.setTodayHeaderEnabled(todayHeaderEnabled)
        sharedPreferencesRepository.setNoteUnderlineEnabled(noteUnderlineEnabled)
        sharedPreferencesRepository.setMonospacedFontEnabled(useMonospacedFont)

        if (sharedPreferencesRepository.getTheme() != theme) {
            sharedPreferencesRepository.setTheme(theme)
            view?.refreshScreen()
        }
    }

    override fun handleBackClick() {
        if (unsavedChanges()) {
            view?.showDiscardChangesDialog()
        } else {
            view?.navigateBack()
        }
    }

    override fun handleBackConfirm() {
        view?.navigateBack()
    }


    /*--- Private Methods ---*/

    private fun unsavedChanges(): Boolean {
        var unsavedChanges = false

        if (sharedPreferencesRepository.getTheme() != theme) unsavedChanges = true

        if (sharedPreferencesRepository.getNotePreviewLines() != notePreviewLines) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteDateType() != noteDateType) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteColorBarEnabled() != colorBarEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteThemedBarEnabled() != themedBarEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getTodayHeaderEnabled() != todayHeaderEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteUnderlineEnabled() != noteUnderlineEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getMonospacedFontEnabled() != useMonospacedFont) unsavedChanges = true

        return unsavedChanges
    }
}
