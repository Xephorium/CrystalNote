package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.xephorium.crystalnote.R

class CrystalNoteDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.crystal_note_dialog_layout)
        .create()
    private var listener: CrystalNoteDialogListener = DEFAULT_LISTENER


    /*--- Public Methods ---*/

    fun setTitle(title: String) {
        alertDialog.findViewById<TextView>(R.id.textDialogTitle)?.text = title
    }

    fun setMessage(message: String) {
        if (message.isNotEmpty()) {
            alertDialog.findViewById<TextView>(R.id.textDialogMessage)?.run {
                visibility = View.VISIBLE
                text = message
            }
        } else {
            alertDialog.findViewById<TextView>(R.id.textDialogMessage)?.run {
                visibility = View.GONE
            }
        }
    }

    fun setPositiveButtonName(name: String) {
        alertDialog.findViewById<AppCompatButton>(R.id.buttonDialogPositive)?.text = name
    }

    fun setNegativeButtonName(name: String) {
        alertDialog.findViewById<AppCompatButton>(R.id.buttonDialogNegative)?.run {
            visibility = View.VISIBLE
            text = name
        }
    }

    fun setListener(listener: CrystalNoteDialogListener) {
        this.listener = listener
    }

    fun show() {
        alertDialog.show()

        setupDialogAppearance()
        setupEventListeners()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun setupDialogAppearance() {
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupEventListeners() {
        alertDialog.findViewById<ConstraintLayout>(R.id.dialogBackground)?.setOnClickListener {
            alertDialog.dismiss()
            listener.onBackClick()
        }
        alertDialog.findViewById<AppCompatButton>(R.id.buttonDialogPositive)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onPositiveClick()
        }
        alertDialog.findViewById<AppCompatButton>(R.id.buttonDialogNegative)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onNegativeClick()
        }
    }


    /*--- Builder Class ---*/

    class Builder(val context: Context) {
        fun create() = CrystalNoteDialog(context)
    }


    /*--- Constants ---*/

    companion object {
        private const val ANIMATION_DELAY: Long = 120

        private val DEFAULT_LISTENER = object : CrystalNoteDialogListener {
            override fun onPositiveClick() = Unit
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        }

        interface CrystalNoteDialogListener {
            fun onPositiveClick()
            fun onNegativeClick()
            fun onBackClick()
        }
    }
}