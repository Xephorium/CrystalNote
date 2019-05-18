package com.xephorium.crystalnote.ui.settings


class SettingsPresenter : SettingsContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: SettingsContract.View) {
        super.attachView(view)

        noteColorsEnabled = sharedPreferencesRepository.getNoteColorsEnabled()
        todayHeaderEnabled = sharedPreferencesRepository.getTodayHeaderEnabled()

        this.view?.populateNoteColorsCheckbox(noteColorsEnabled)
        this.view?.populateTodayHeaderCheckbox(todayHeaderEnabled)
    }


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleNoteColorsToggle(checked: Boolean) {
        noteColorsEnabled = checked
    }

    override fun handleTodayHeaderToggle(checked: Boolean) {
        todayHeaderEnabled = checked
    }

    override fun handleSaveClick() {
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

        if (sharedPreferencesRepository.getNoteColorsEnabled() != noteColorsEnabled) unsavedChanges = true

        if (sharedPreferencesRepository.getTodayHeaderEnabled() != todayHeaderEnabled) unsavedChanges = true

        return unsavedChanges
    }
}
