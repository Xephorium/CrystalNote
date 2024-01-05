package com.xephorium.crystalnote.ui.colorpicker

import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab

interface ColorPickerDialogContract {

    interface View: BaseView {
        fun populateDialogViews(title: String, buttonText: String)

        fun enableSelectButton()
        fun disableSelectButton()
        fun returnSelectedColor(color: Int)

        fun setCustomColor(color: PreciseColor)
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        var dialogTitle: String = "Select a Color"
        var dialogButtonText: String = "Select"

        var currentTab: ColorPickerTab = ColorPickerTab.PALETTE

        var selectedPaletteColor: Int? = null
        var selectedCustomColor: PreciseColor = DEFAULT_CUSTOM_COLOR

        abstract fun handleTabChange(colorPickerTab: ColorPickerTab)
        abstract fun handleSelectButtonClick()

        abstract fun handlePaletteColorChange(color: Int)

        abstract fun handleCustomHexChange(hex: String)
        abstract fun handleCustomHueChange(hue: String)
        abstract fun handleCustomSatChange(sat: String)
        abstract fun handleCustomValChange(value: String)

        companion object {
            val DEFAULT_CUSTOM_COLOR = PreciseColor(212, 100, 75)
        }
    }
}