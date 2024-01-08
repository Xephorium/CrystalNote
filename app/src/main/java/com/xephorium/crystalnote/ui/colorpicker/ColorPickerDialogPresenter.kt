package com.xephorium.crystalnote.ui.colorpicker

import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
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
        view?.notifyTabChange(colorPickerTab)
        updateButtonStates()
    }

    override fun handleSelectButtonClick() {
        if (currentTab == ColorPickerTab.PALETTE) {
            selectedPaletteColor?.let { view?.returnSelectedColor(it) }
        } else {
            view?.returnSelectedColor(selectedCustomColor.toIntColor())
        }
    }

    override fun handleFavoriteButtonClick() {
        favoriteColors.addColor(selectedCustomColor.toIntColor())
        view?.setFavoriteColors(favoriteColors)
        sharedPreferencesRepository.setFavoriteColorQueue(favoriteColors)
    }

    override fun handlePaletteColorChange(color: Int) {
        selectedPaletteColor = color
        updateButtonStates()
    }

    override fun handleCustomHexChange(hex: String) {
        PreciseColor.fromHex(hex)?.let {
            selectedCustomColor = it
            view?.setCustomColor(selectedCustomColor)
        }
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

    override fun handleRainbowClick(x: Float, y: Float) {
        selectedCustomColor.saturation = (x * 100f).toInt()
        selectedCustomColor.value = (y * 100f).toInt()
        view?.setCustomColor(selectedCustomColor)
    }

    override fun handleFavoriteClick(color: Int) {
        selectedCustomColor = PreciseColor(color)
        view?.setCustomColor(selectedCustomColor)
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

        return stringInt in min..max
    }

    private fun updateButtonStates() {
        if (currentTab == ColorPickerTab.PALETTE) {
            if (selectedPaletteColor != null) view?.enableSelectButton()
            else view?.disableSelectButton()
            view?.hideFavoriteButton()
        } else {
            view?.enableSelectButton()
            view?.showFavoriteButton()
        }
    }
}