package com.xephorium.crystalnote.ui.widget

import android.os.Build
import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.CornerCurve
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
        view?.showBackgroundColorPickerDialog(getWorkingWidgetState().backgroundColor)
    }

    override fun handleTitleColorClick() {
        view?.showTitleColorPickerDialog(getWorkingWidgetState().titleColor)
    }

    override fun handleContentColorClick() {
        view?.showContentColorPickerDialog(getWorkingWidgetState().contentColor)
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

    override fun handleCornerCurveChange(cornerCurve: CornerCurve) {
        workingWidgetStates.setCornerCurveAtIndex(workingWidgetIndex, cornerCurve)
        view?.setPreviewCornerCurve(cornerCurve)
    }

    override fun handleCornerCurveWarningClick() {
        view?.showCornerCurveWarningDialog()
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


    override fun handleBackClick() {
        if (initialWidgetStates.toString() == workingWidgetStates.toString()) {

            // No Changes - Navigate Back
            view?.navigateBack()

        } else {

            // Widget State Changed - Build Name List
            val builder = StringBuilder()
            val initialStates = initialWidgetStates!!.getWidgetStates()
            val workingStates = workingWidgetStates.getWidgetStates()
            for (index in 0 until initialStates.size) {
                if (initialStates[index].toString() != workingStates[index].toString()) {
                    builder.append("• ")
                    builder.append(noteNameList[index])
                    builder.append("\n")
                }
            }

            // Prompt User
            view?.showDiscardChangesDialog(builder.toString())
        }
    }

    override fun handleBackConfirm() {
        view?.navigateBack()
    }

    /*--- Private Methods ---*/

    private fun widgetStateListChanged(): Boolean {
        if (this.initialWidgetStates == null) return true
        val newWidgetStateList = sharedPreferencesRepository.getWidgetStateList()
        return newWidgetStateList.toString() != initialWidgetStates.toString()
    }

    private fun generateNoteNameList(): List<String> {
        val notes = noteRoomRepository.getPreviewNotes()
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
        view?.populateCornerCurve(getWorkingWidgetState().cornerCurve)

        // Configure Preview
        view?.setPreviewTextSize(getWorkingWidgetState().textSize)
        view?.setPreviewBackgroundColor(getWorkingWidgetState().backgroundColor)
        view?.setPreviewTitleColor(getWorkingWidgetState().titleColor)
        view?.setPreviewContentColor(getContentColorWithAlpha())
        view?.setPreviewBackgroundAlpha(getWorkingWidgetState().backgroundAlpha)
        view?.setPreviewCornerCurve(CornerCurve.Huge)

        // Configure Corner Curve Warning
        if (isCornerCurveSupported()) view?.hideCornerCurveWarningIcon()
        else view?.showCornerCurveWarningIcon()
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
        this.view?.setPreviewCornerCurve(WidgetState.DEFAULT_CORNER_CURVE)
    }

    private fun getWorkingWidgetState(): WidgetState {
        return workingWidgetStates.getWidgetStates()[workingWidgetIndex]
    }

    /* Note: Versions of Android above API level 31 automatically apply a corner
     *       radius of 16dp to all widget backgrounds. To avoid a confusing user
     *       experience, I've added a warning icon and dialog explaining this to
     *       users on newer devices.
     */
    private fun isCornerCurveSupported(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    }
}
