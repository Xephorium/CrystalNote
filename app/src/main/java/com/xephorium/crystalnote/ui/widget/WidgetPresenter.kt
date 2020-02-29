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
            this.workingWidgetIndex = 0

            // Hide Empty Screen
            this.view?.hideNoWidgetsMessage()

            // Populate Fields
            this.view?.setupWidgetSelector(noteNameList)
            this.view?.populateWidgetSelector(workingWidgetIndex)
            this.view?.populateTextSize(getWorkingWidgetState().textSize)
            this.view?.populateTransparency(getWorkingWidgetState().transparency)
            this.view?.populateBackgroundColor(getWorkingWidgetState().backgroundColor)
            this.view?.populateTitleColor(getWorkingWidgetState().titleColor)
            this.view?.populateTextColor(getWorkingWidgetState().textColor)

            // Configure Preview
            this.view?.setPreviewTextSize(getWorkingWidgetState().textSize)
            this.view?.setPreviewTransparency(getWorkingWidgetState().transparency)
            this.view?.setPreviewBackgroundColor(getWorkingWidgetState().backgroundColor)
            this.view?.setPreviewTitleColor(getWorkingWidgetState().titleColor)
            this.view?.setPreviewTextColor(getWorkingWidgetState().textColor)


        } else {

            // Show Empty Screen
            this.view?.showNoWidgetsMessage()

            // Reset Preview
            this.view?.setPreviewTextSize(WidgetState.DEFAULT_TEXT_SIZE)
            this.view?.setPreviewTransparency(WidgetState.DEFAULT_TRANSPARENCY)
            this.view?.setPreviewBackgroundColor(WidgetState.DEFAULT_BACKGROUND_COLOR)
            this.view?.setPreviewTitleColor(WidgetState.DEFAULT_TITLE_COLOR)
            this.view?.setPreviewTextColor(WidgetState.DEFAULT_TEXT_COLOR)
        }
    }

    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleWidgetChange(index: Int) {
        workingWidgetIndex = index

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

    private fun getWorkingWidgetState(): WidgetState {
        return workingWidgetStates.getWidgetStates()[workingWidgetIndex]
    }
}
