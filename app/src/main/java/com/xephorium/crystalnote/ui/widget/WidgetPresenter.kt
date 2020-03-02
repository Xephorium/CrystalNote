package com.xephorium.crystalnote.ui.widget

import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency


class WidgetPresenter : WidgetContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: WidgetContract.View) {
        super.attachView(view)

        // Conditionally Initialize State Variables
        if (widgetStateListChanged()) {
            this.initialWidgetStates = sharedPreferencesRepository.getWidgetStateList()
            this.workingWidgetStates = sharedPreferencesRepository.getWidgetStateList()
            this.noteNameList = generateNoteNameList()
            this.workingWidgetIndex = 0
        }

        if (noteNameList.isNotEmpty()) {

            // Set State & Populate Screen
            this.view?.hideNoWidgetsMessage()
            this.view?.setupWidgetSelector(noteNameList)
            populateFieldsAndConfigurePreview()

        } else {

            // Set Screen to Empty State
            this.view?.showNoWidgetsMessage()
            resetPreview()
        }

        // Set Preview Background
        this.view?.setPreviewBackgroundBrightness(previewBackgroundBright)
    }

    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleWidgetChange(index: Int) {
        workingWidgetIndex = index
        populateFieldsAndConfigurePreview()
    }

    override fun handleTextSizeChange(textSize: TextSize) {
        workingWidgetStates.setTextSizeAtIndex(workingWidgetIndex, textSize)
        view?.setPreviewTextSize(getWorkingWidgetState().textSize)
    }

    override fun handleTransparencyChange(transparency: Transparency) {
        workingWidgetStates.setTransparencyAtIndex(workingWidgetIndex, transparency)
        view?.setPreviewTransparency(getWorkingWidgetState().transparency)
    }

    override fun handleBackgroundColorClick() {
        view?.showBackgroundColorPickerDialog()
    }

    override fun handleTitleColorClick() {
        view?.showTitleColorPickerDialog()
    }

    override fun handleTextColorClick() {
        view?.showTextColorPickerDialog()
    }

    override fun handleBackgroundColorChange(color: Int) {
        workingWidgetStates.setBackgroundColorAtIndex(workingWidgetIndex, color)
        view?.populateBackgroundColor(color)
        view?.setPreviewBackgroundColor(getWorkingWidgetState().backgroundColor)
    }

    override fun handleTitleColorChange(color: Int) {
        workingWidgetStates.setTitleColorAtIndex(workingWidgetIndex, color)
        view?.populateTitleColor(color)
        view?.setPreviewTitleColor(getWorkingWidgetState().titleColor)
    }

    override fun handleTextColorChange(color: Int) {
        workingWidgetStates.setTextColorAtIndex(workingWidgetIndex, color)
        view?.populateTextColor(color)
        view?.setPreviewTextColor(getWorkingWidgetState().textColor)
    }

    override fun handlePreviewBackgroundBrightnessToggle() {
        previewBackgroundBright = previewBackgroundBright.not()
        view?.setPreviewBackgroundBrightness(previewBackgroundBright)
    }

    override fun handleSaveClick() {
        sharedPreferencesRepository.setWidgetStateList(workingWidgetStates)
        this.initialWidgetStates = sharedPreferencesRepository.getWidgetStateList()
        view?.refreshWidgets()
    }


    /*--- Private Methods ---*/

    private fun widgetStateListChanged(): Boolean {
        if (this.initialWidgetStates == null) return true
        val newWidgetStateList = sharedPreferencesRepository.getWidgetStateList()
        return newWidgetStateList.toString() != initialWidgetStates.toString()
    }

    private fun generateNoteNameList(): List<String> {
        val notes = noteRoomRepository.getNotes()
        val names = mutableListOf<String>()
        var blankCount = 1
        for (widgetState in initialWidgetStates!!.getWidgetStates()) {
            var noteName = notes.firstOrNull { it.id == widgetState.noteId }?.name
            if (noteName == null) {
                noteName = "New Widget $blankCount"
                blankCount++
            }
            names.add(noteName)
        }

        return names
    }

    private fun populateFieldsAndConfigurePreview() {

        // Populate Fields
        view?.populateWidgetSelector(workingWidgetIndex)
        view?.populateTextSize(getWorkingWidgetState().textSize)
        view?.populateTransparency(getWorkingWidgetState().transparency)
        view?.populateBackgroundColor(getWorkingWidgetState().backgroundColor)
        view?.populateTitleColor(getWorkingWidgetState().titleColor)
        view?.populateTextColor(getWorkingWidgetState().textColor)

        // Configure Preview
        view?.setPreviewTextSize(getWorkingWidgetState().textSize)
        view?.setPreviewTransparency(getWorkingWidgetState().transparency)
        view?.setPreviewBackgroundColor(getWorkingWidgetState().backgroundColor)
        view?.setPreviewTitleColor(getWorkingWidgetState().titleColor)
        view?.setPreviewTextColor(getWorkingWidgetState().textColor)
    }

    private fun resetPreview() {
        this.view?.setPreviewTextSize(WidgetState.DEFAULT_TEXT_SIZE)
        this.view?.setPreviewTransparency(WidgetState.DEFAULT_TRANSPARENCY)
        this.view?.setPreviewBackgroundColor(WidgetState.DEFAULT_BACKGROUND_COLOR)
        this.view?.setPreviewTitleColor(WidgetState.DEFAULT_TITLE_COLOR)
        this.view?.setPreviewTextColor(WidgetState.DEFAULT_TEXT_COLOR)
    }

    private fun getWorkingWidgetState(): WidgetState {
        return workingWidgetStates.getWidgetStates()[workingWidgetIndex]
    }
}
