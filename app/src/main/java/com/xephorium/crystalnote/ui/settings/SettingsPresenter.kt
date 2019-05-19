package com.xephorium.crystalnote.ui.settings

import android.util.Log
import com.xephorium.crystalnote.data.model.DateType


class SettingsPresenter : SettingsContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: SettingsContract.View) {
        super.attachView(view)

        notePreviewLines = sharedPreferencesRepository.getNotePreviewLines()
        noteDateType = sharedPreferencesRepository.getNoteDateType()
        noteColorsEnabled = sharedPreferencesRepository.getNoteColorsEnabled()
        todayHeaderEnabled = sharedPreferencesRepository.getTodayHeaderEnabled()

        this.view?.populateNotePreviewLines(notePreviewLines)
        this.view?.populateNoteDateType(noteDateType)
        this.view?.populateNoteColorsCheckbox(noteColorsEnabled)
        this.view?.populateTodayHeaderCheckbox(todayHeaderEnabled)
    }


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleNoteLinesChange(lines: Int) {
        notePreviewLines = lines
    }

    override fun handleNoteDateTypeChange(dateType: DateType) {
        noteDateType = dateType
    }

    override fun handleNoteColorsToggle(checked: Boolean) {
        noteColorsEnabled = checked
    }

    override fun handleTodayHeaderToggle(checked: Boolean) {
        todayHeaderEnabled = checked
    }

    override fun handleSaveClick() {
        sharedPreferencesRepository.setNotePreviewLines(notePreviewLines)
        sharedPreferencesRepository.setNoteDateType(noteDateType)
        sharedPreferencesRepository.setNoteColorsEnabled(noteColorsEnabled)
        sharedPreferencesRepository.setTodayHeaderEnabled(todayHeaderEnabled)

        // TODO - Reload Screen
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

        if (sharedPreferencesRepository.getNotePreviewLines() != notePreviewLines) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteDateType() != noteDateType) unsavedChanges = true

        if (sharedPreferencesRepository.getNoteColorsEnabled() != noteColorsEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getTodayHeaderEnabled() != todayHeaderEnabled) unsavedChanges = true

        return unsavedChanges
    }
}
