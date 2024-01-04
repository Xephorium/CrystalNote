package com.xephorium.crystalnote.ui.colorpicker.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.xephorium.crystalnote.R


class ColorPickerDialog private constructor(context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme).create()
    private val colorGridView = ColorGridView(context)


    /*--- Constructor ---*/

    init {
        alertDialog.setView(colorGridView)
    }


    /*--- Public Methods ---*/

    fun setTitle(title: String) {
        alertDialog.setTitle(title)
    }

    fun setColorPickerListener(listener: ColorPickerListener) {
        colorGridView.setColorPickerListener(listener)
    }

    fun show() {
        alertDialog.show()
        setupDialogAppearance()

        colorGridView.populateOrbGrid()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun setupDialogAppearance() {
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }


    /*--- Builder Class ---*/

    class Builder(val context: Context) {
        fun create() = ColorPickerDialog(context)
    }


    /*--- Constants ---*/

    companion object {

        interface ColorPickerListener {
            fun onColorSelect(color: Int)
        }
    }
}
