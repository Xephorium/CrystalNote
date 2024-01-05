package com.xephorium.crystalnote.ui.colorpicker

import android.graphics.Color
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab

interface ColorPickerDialogContract {

    interface View: BaseView {
        fun populateDialogViews(title: String, buttonText: String)

        fun enableSelectButton()
        fun disableSelectButton()
        fun returnSelectedColor(color: Int)
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        var dialogTitle: String = "Select a Color"
        var dialogButtonText: String = "Select"

        var currentTab: ColorPickerTab = ColorPickerTab.PALETTE

        var selectedPaletteColor: Int? = null
        var selectedCustomColor: Int = Color.parseColor("#78b6ff")

        abstract fun handleTabChange(colorPickerTab: ColorPickerTab)
        abstract fun handleSelectButtonClick()

        abstract fun handlePaletteColorChange(color: Int)
    }
}