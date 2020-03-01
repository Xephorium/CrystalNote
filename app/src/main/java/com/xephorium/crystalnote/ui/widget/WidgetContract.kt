package com.xephorium.crystalnote.ui.widget

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
        fun populateTextSize(size: TextSize)
        fun populateTransparency(transparency: Transparency)
        fun populateBackgroundColor(color: Int)
        fun populateTitleColor(color: Int)
        fun populateTextColor(color: Int)

        fun setPreviewTextSize(size: TextSize)
        fun setPreviewTransparency(transparency: Transparency)
        fun setPreviewBackgroundColor(color: Int)
        fun setPreviewTitleColor(color: Int)
        fun setPreviewTextColor(color: Int)

        fun showNoWidgetsMessage()
        fun hideNoWidgetsMessage()
        fun showBackgroundColorPickerDialog()
        fun showTitleColorPickerDialog()
        fun showTextColorPickerDialog()
        fun showNavigationDrawer()

        fun refreshWidgets()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRoomRepository: NoteRoomRepository

        var initialWidgetStates: WidgetStateList? = null
        lateinit var workingWidgetStates: WidgetStateList
        lateinit var noteNameList: List<String>
        var workingWidgetIndex = 0

        abstract fun handleWidgetChange(index: Int)
        abstract fun handleTextSizeChange(textSize: TextSize)
        abstract fun handleTransparencyChange(transparency: Transparency)
        abstract fun handleBackgroundColorClick()
        abstract fun handleTitleColorClick()
        abstract fun handleTextColorClick()

        abstract fun handleMenuButtonClick()
        abstract fun handleSaveClick()
    }
}
