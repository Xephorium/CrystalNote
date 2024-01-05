package com.xephorium.crystalnote.ui.colorpicker

import android.graphics.Color
import com.xephorium.crystalnote.data.utility.ColorUtility
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab

class ColorPickerDialogPresenter : ColorPickerDialogContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: ColorPickerDialogContract.View) {
        super.attachView(view)

        view.populateDialogViews(dialogTitle, dialogButtonText)
    }


    /*--- Action Handling Methods ---*/

    override fun handleTabChange(colorPickerTab: ColorPickerTab) {
        currentTab = colorPickerTab
        updateSelectButtonState()
    }

    override fun handleSelectButtonClick() {
        if (currentTab == ColorPickerTab.PALETTE) {
            selectedPaletteColor?.let { view?.returnSelectedColor(it) }
        } else {
            view?.returnSelectedColor(selectedCustomColor)
        }
    }

    override fun handlePaletteColorChange(color: Int) {
        selectedPaletteColor = color
        updateSelectButtonState()
    }

    override fun handleCustomHexChange(hex: String) {
        val color = ColorUtility.intFromHex(hex)
        if (color != null) {
            view?.setCustomColor(color)
        }
    }


    /*--- Private Methods ---*/

    private fun updateSelectButtonState() {
        if (currentTab == ColorPickerTab.PALETTE) {
            if (selectedPaletteColor != null) view?.enableSelectButton()
            else view?.disableSelectButton()
        } else {
            view?.enableSelectButton()
        }
    }
}