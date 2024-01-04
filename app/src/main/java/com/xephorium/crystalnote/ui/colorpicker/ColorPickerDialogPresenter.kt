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
    }
}