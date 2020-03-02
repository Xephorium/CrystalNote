package com.xephorium.crystalnote.ui.widget

import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import com.xephorium.crystalnote.data.utility.ColorUtility


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


    override fun handleBackgroundColorClick() {
        view?.showBackgroundColorPickerDialog()
    }

    override fun handleTitleColorClick() {
        view?.showTitleColorPickerDialog()
    }

    override fun handleContentColorClick() {
        view?.showContentColorPickerDialog()
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

    override fun handleContentColorChange(color: Int) {
        workingWidgetStates.setContentColorAtIndex(workingWidgetIndex, color)
        view?.populateContentColor(color)
        view?.setPreviewContentColor(getContentColorWithAlpha())
    }

    override fun handleTextSizeChange(textSize: TextSize) {
        workingWidgetStates.setTextSizeAtIndex(workingWidgetIndex, textSize)
        view?.setPreviewTextSize(getWorkingWidgetState().textSize)
    }

    override fun handleBackgroundAlphaChange(transparency: Transparency) {
        workingWidgetStates.setBackgroundAlphaAtIndex(workingWidgetIndex, transparency)
        view?.setPreviewBackgroundAlpha(getWorkingWidgetState().backgroundAlpha)
    }

    override fun handleContentAlphaChange(transparency: Transparency) {
        workingWidgetStates.setContentAlphaAtIndex(workingWidgetIndex, transparency)
        view?.setPreviewContentColor(getContentColorWithAlpha())
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
        view?.populateBackgroundColor(getWorkingWidgetState().backgroundColor)
        view?.populateTitleColor(getWorkingWidgetState().titleColor)
        view?.populateContentColor(getWorkingWidgetState().contentColor)
        view?.populateTextSize(getWorkingWidgetState().textSize)
        view?.populateBackgroundAlpha(getWorkingWidgetState().backgroundAlpha)
        view?.populateContentAlpha(getWorkingWidgetState().contentAlpha)

        // Configure Preview
        view?.setPreviewTextSize(getWorkingWidgetState().textSize)
        view?.setPreviewBackgroundColor(getWorkingWidgetState().backgroundColor)
        view?.setPreviewTitleColor(getWorkingWidgetState().titleColor)
        view?.setPreviewContentColor(getContentColorWithAlpha())
        view?.setPreviewBackgroundAlpha(getWorkingWidgetState().backgroundAlpha)
    }

    private fun getContentColorWithAlpha(): Int {
        return ColorUtility.applyTransparency(
            getWorkingWidgetState().contentColor,
            getWorkingWidgetState().contentAlpha
        )
    }

    private fun resetPreview() {
        this.view?.setPreviewBackgroundColor(WidgetState.DEFAULT_BACKGROUND_COLOR)
        this.view?.setPreviewTitleColor(WidgetState.DEFAULT_TITLE_COLOR)
        this.view?.setPreviewContentColor(WidgetState.DEFAULT_CONTENT_COLOR)
        this.view?.setPreviewTextSize(WidgetState.DEFAULT_TEXT_SIZE)
        this.view?.setPreviewBackgroundAlpha(WidgetState.DEFAULT_TRANSPARENCY)
    }

    private fun getWorkingWidgetState(): WidgetState {
        return workingWidgetStates.getWidgetStates()[workingWidgetIndex]
    }
}
