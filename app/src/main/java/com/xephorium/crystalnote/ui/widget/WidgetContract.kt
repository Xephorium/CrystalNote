package com.xephorium.crystalnote.ui.widget

import com.xephorium.crystalnote.data.model.WidgetState
import com.xephorium.crystalnote.data.model.WidgetState.Companion.CornerCurve
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import com.xephorium.crystalnote.data.model.WidgetStateList
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface WidgetContract {

    interface View : BaseView {
        fun setupWidgetSelector(widgetNames: List<String>)

        fun populateWidgetSelector(index: Int)
        fun populateBackgroundColor(color: Int)
        fun populateTitleColor(color: Int)
        fun populateContentColor(color: Int)
        fun populateTextSize(size: TextSize)
        fun populateBackgroundAlpha(transparency: Transparency)
        fun populateContentAlpha(transparency: Transparency)
        fun populateCornerCurve(cornerCurve: CornerCurve)

        fun setPreviewTextSize(size: TextSize)
        fun setPreviewBackgroundColor(color: Int)
        fun setPreviewTitleColor(color: Int)
        fun setPreviewContentColor(color: Int)
        fun setPreviewBackgroundAlpha(transparency: Transparency)
        fun setPreviewCornerCurve(cornerCurve: CornerCurve)
        fun setPreviewBackgroundBrightness(light: Boolean)

        fun showCornerCurveSpinner()
        fun hideCornerCurveSpinner()
        fun showNoWidgetsMessage()
        fun hideNoWidgetsMessage()
        fun showBackgroundColorPickerDialog()
        fun showTitleColorPickerDialog()
        fun showContentColorPickerDialog()
        fun showNavigationDrawer()
        fun showDiscardChangesDialog(widgetNames: String)
        fun navigateBack()

        fun refreshWidgets()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRoomRepository: NoteRoomRepository

        var initialWidgetStates: WidgetStateList? = null
        lateinit var workingWidgetStates: WidgetStateList
        lateinit var noteNameList: List<String>
        var workingWidgetIndex = 0
        var previewBackgroundBright: Boolean = true

        abstract fun handleWidgetChange(index: Int)
        abstract fun handleBackgroundColorClick()
        abstract fun handleTitleColorClick()
        abstract fun handleContentColorClick()
        abstract fun handleBackgroundColorChange(color: Int)
        abstract fun handleTitleColorChange(color: Int)
        abstract fun handleContentColorChange(color: Int)
        abstract fun handleTextSizeChange(textSize: TextSize)
        abstract fun handleBackgroundAlphaChange(transparency: Transparency)
        abstract fun handleContentAlphaChange(transparency: Transparency)
        abstract fun handleCornerCurveChange(cornerCurve: CornerCurve)
        abstract fun handlePreviewBackgroundBrightnessToggle()

        abstract fun handleMenuButtonClick()
        abstract fun handleSaveClick()
        abstract fun handleBackClick()
        abstract fun handleBackConfirm()
    }
}
