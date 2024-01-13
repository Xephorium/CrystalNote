package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.xephorium.crystalnote.R

class HomeOptionsDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.home_options_dialog_layout)
        .create()
    private var listener: HomeOptionsListener = DEFAULT_LISTENER
    private var title = "Home Options"
    private var showArchivedText = "Show Archived"


    /*--- Public Methods ---*/

    fun setTitle(newTitle: String) {
        title = newTitle
    }

    fun setArchivedShown(archivedShown: Boolean) {
        showArchivedText = if (archivedShown) "Hide Archived" else "Show Archived"
    }

    fun setListener(listener: HomeOptionsListener) {
        this.listener = listener
    }

    fun show() {
        alertDialog.show()

        setupDialogAppearance()
        setupViewItems()
        setupEventListeners()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun setupDialogAppearance() {
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupViewItems() {
        var textTitle = alertDialog.findViewById<TextView>(R.id.textNoteTitle)
        var textArchived = alertDialog.findViewById<TextView>(R.id.textHomeOptionsShowArchived)

        textTitle?.text = title
        textArchived?.text = showArchivedText
    }

    private fun setupEventListeners() {
        alertDialog.findViewById<ConstraintLayout>(R.id.dialogBackground)?.setOnClickListener {
            dismiss()
        }

        alertDialog.findViewById<TextView>(R.id.textHomeOptionsShowArchived)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            dismiss()
            listener.onShowArchivedClick()
        }
    }


    /*--- Builder Class ---*/

    class Builder(val context: Context) {
        fun create() = HomeOptionsDialog(context)
    }


    /*--- Constants ---*/

    companion object {
        private const val ANIMATION_DELAY: Long = 120

        private val DEFAULT_LISTENER = object : HomeOptionsListener {
            override fun onShowArchivedClick() = Unit
        }

        interface HomeOptionsListener {
            fun onShowArchivedClick()
        }
    }
}