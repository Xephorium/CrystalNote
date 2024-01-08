package com.xephorium.crystalnote.ui.colorpicker

import com.xephorium.crystalnote.data.model.FavoriteColorQueue
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor.Companion.DEFAULT_PRECISE_COLOR
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab

interface ColorPickerDialogContract {

    interface View: BaseView {
        fun populateDialogViews(title: String, buttonText: String)

        fun enableSelectButton()
        fun disableSelectButton()
        fun returnSelectedColor(color: Int)

        fun setCustomColor(color: PreciseColor)
        fun setFavoriteColors(favoriteColorQueue: FavoriteColorQueue)
        fun notifyTabChange(tab: ColorPickerTab)

        fun showFavoriteButton()
        fun hideFavoriteButton()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        var dialogTitle: String = "Select a Color"
        var dialogButtonText: String = "Select"

        var currentTab: ColorPickerTab = ColorPickerTab.PALETTE

        var selectedPaletteColor: Int? = null
        var selectedCustomColor: PreciseColor = DEFAULT_PRECISE_COLOR

        var favoriteColors: FavoriteColorQueue = FavoriteColorQueue("")

        abstract fun handleTabChange(colorPickerTab: ColorPickerTab)
        abstract fun handleSelectButtonClick()
        abstract fun handleFavoriteButtonClick()

        abstract fun handlePaletteColorChange(color: Int)

        abstract fun handleCustomHexChange(hex: String)
        abstract fun handleCustomHueChange(hue: String)
        abstract fun handleCustomSatChange(sat: String)
        abstract fun handleCustomValChange(value: String)
        abstract fun handleRainbowClick(x: Float, y: Float)
        abstract fun handleFavoriteClick(color: Int)
        abstract fun handleFavoriteLongClick(color: Int)
    }
}