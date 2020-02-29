package com.xephorium.crystalnote.ui.widget

import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency


class WidgetPresenter : WidgetContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: WidgetContract.View) {
        super.attachView(view)

        // Initialize State Variables
        this.initialWidgetStates = sharedPreferencesRepository.getWidgetStateList()
        this.workingWidgetStates = sharedPreferencesRepository.getWidgetStateList()
        this.noteNameList = generateNoteNameList()

        if (noteNameList.isNotEmpty()) {

            // Set State & Populate Screen
            this.workingWidgetIndex = 0
            this.view?.hideNoWidgetsMessage()
            this.view?.setupWidgetSelector(noteNameList)
            populateFieldsAndConfigurePreview()

        } else {

            // Set Screen to Empty State
            this.view?.showNoWidgetsMessage()
            resetPreview()
        }
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

    override fun handleSaveClick() {
        sharedPreferencesRepository.setWidgetStateList(workingWidgetStates)
        view?.refreshWidgets()
    }


    /*--- Private Methods ---*/

    private fun generateNoteNameList(): List<String> {
        val notes = noteRoomRepository.getNotes()
        val names = mutableListOf<String>()
        var blankCount = 1
        for (widgetState in initialWidgetStates.getWidgetStates()) {
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
