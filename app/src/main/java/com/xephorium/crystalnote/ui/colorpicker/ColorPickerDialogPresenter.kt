package com.xephorium.crystalnote.ui.colorpicker

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
            view?.returnSelectedColor(selectedCustomColor.getIntColor())
        }
    }

    override fun handlePaletteColorChange(color: Int) {
        selectedPaletteColor = color
        updateSelectButtonState()
    }

    override fun handleCustomHexChange(hex: String) {
        if (selectedCustomColor.setFromHex(hex)) view?.setCustomColor(selectedCustomColor)
    }

    override fun handleCustomHueChange(hue: String) {
        if (isStringIntegerWithinRange(hue, 0, 360)) {
            selectedCustomColor.hue = hue.toInt()
            view?.setCustomColor(selectedCustomColor)
        }
    }

    override fun handleCustomSatChange(sat: String) {
        if (isStringIntegerWithinRange(sat, 0, 100)) {
            selectedCustomColor.saturation = sat.toInt()
            view?.setCustomColor(selectedCustomColor)
        }
    }

    override fun handleCustomValChange(value: String) {
        if (isStringIntegerWithinRange(value, 0, 100)) {
            selectedCustomColor.value = value.toInt()
            view?.setCustomColor(selectedCustomColor)
        }
    }


    /*--- Private Methods ---*/

    private fun isStringIntegerWithinRange(string: String, min: Int, max: Int): Boolean {
        if (string.isEmpty()) return false

        val stringInt: Int
        try {
            stringInt = string.toInt()
        } catch (exception: Exception) {
            return false
        }

        return stringInt in 0..360
    }

    private fun updateSelectButtonState() {
        if (currentTab == ColorPickerTab.PALETTE) {
            if (selectedPaletteColor != null) view?.enableSelectButton()
            else view?.disableSelectButton()
        } else {
            view?.enableSelectButton()
        }
    }
}