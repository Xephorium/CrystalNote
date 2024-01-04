package com.xephorium.crystalnote.ui.colorpicker

import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab

interface ColorPickerDialogContract {

    interface View: BaseView {
        fun populateDialogViews(title: String, buttonText: String)
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        var dialogTitle: String = "Select a Color"
        var dialogButtonText: String = "Select"

        var currentTab: ColorPickerTab = ColorPickerTab.PALETTE

        abstract fun handleTabChange(colorPickerTab: ColorPickerTab)
    }
}