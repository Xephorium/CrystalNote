package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.xephorium.crystalnote.R


class NoteOptionsDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.note_options_dialog_layout)
        .create()
    private var listener: NoteOptionsListener = DEFAULT_LISTENER
    private var showLock = true
    private var showUnlock = true
    private var showExport = true
    private var showRestore = false
    private var showDelete = true


    /*--- Public Methods ---*/

    fun hideLockOption() {
        showLock = false
    }

    fun hideUnlockOption() {
        showUnlock = false
    }

    fun hideExportOption() {
        showExport = false
    }

    fun showRestoreOption() {
        showRestore = true
    }

    fun hideDeleteOption() {
        showDelete = false
    }

    fun setListener(listener: NoteOptionsListener) {
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
        var textLock = alertDialog.findViewById<TextView>(R.id.textNoteOptionsLock)
        var textUnlock = alertDialog.findViewById<TextView>(R.id.textNoteOptionsUnlock)
        var textExport = alertDialog.findViewById<TextView>(R.id.textNoteOptionsExport)
        var textRestore = alertDialog.findViewById<TextView>(R.id.textNoteOptionsRestore)
        var textDelete = alertDialog.findViewById<TextView>(R.id.textNoteOptionsDelete)

        textLock?.visibility = if (showLock) View.VISIBLE else View.GONE
        textUnlock?.visibility = if (showUnlock) View.VISIBLE else View.GONE
        textExport?.visibility = if (showExport) View.VISIBLE else View.GONE
        textRestore?.visibility = if (showRestore) View.VISIBLE else View.GONE
        textDelete?.visibility = if (showDelete) View.VISIBLE else View.GONE
    }

    private fun setupEventListeners() {
        alertDialog.findViewById<ConstraintLayout>(R.id.dialogBackground)?.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.findViewById<TextView>(R.id.textNoteOptionsLock)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onLockClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsUnlock)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onUnlockClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsExport)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onExportClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsRestore)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onRestoreClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsDelete)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onDeleteClick()
        }
    }


    /*--- Builder Class ---*/

    class Builder(val context: Context) {
        fun create() = NoteOptionsDialog(context)
    }


    /*--- Constants ---*/

    companion object {
        private const val ANIMATION_DELAY: Long = 120

        private val DEFAULT_LISTENER = object : NoteOptionsListener {
            override fun onLockClick() = Unit
            override fun onUnlockClick() = Unit
            override fun onExportClick() = Unit
            override fun onRestoreClick() = Unit
            override fun onDeleteClick() = Unit
        }

        interface NoteOptionsListener {
            fun onLockClick()
            fun onUnlockClick()
            fun onExportClick()
            fun onRestoreClick()
            fun onDeleteClick()
        }
    }
}
