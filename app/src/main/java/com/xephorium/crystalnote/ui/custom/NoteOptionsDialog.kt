package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import kotlinx.android.synthetic.main.note_options_layout.*
import kotlinx.android.synthetic.main.password_dialog_layout.*


class NoteOptionsDialog private constructor(private val context: Context) {


    /*--- Variable Declarations ---*/

    private val alertDialog = AlertDialog.Builder(context, R.style.DialogTheme)
        .setView(R.layout.note_options_layout)
        .create()
    private var listener: NoteOptionsListener = DEFAULT_LISTENER
    private var showLock = true
    private var showUnlock = true
    private var showExport = true
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

    fun hideDeleteOption() {
        showDelete = false
    }

    fun setListener(listener: NoteOptionsListener) {
        this.listener = listener
    }

    fun show() {
        alertDialog.show()
        setupViewItems()
        setupClickListeners()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }


    /*--- Private Methods ---*/

    private fun setupViewItems() {
        alertDialog.textNoteOptionsLock.visibility = if (showLock) View.VISIBLE else View.GONE
        alertDialog.textNoteOptionsUnlock.visibility = if (showUnlock) View.VISIBLE else View.GONE
        alertDialog.textNoteOptionsExport.visibility = if (showExport) View.VISIBLE else View.GONE
        alertDialog.textNoteOptionsDelete.visibility = if (showDelete) View.VISIBLE else View.GONE
    }

    private fun setupClickListeners() {
        alertDialog.textNoteOptionsLock.setOnClickListener {
            alertDialog.dismiss()
            listener.onLockClick()
        }
        alertDialog.textNoteOptionsUnlock.setOnClickListener {
            alertDialog.dismiss()
            listener.onUnlockClick()
        }
        alertDialog.textNoteOptionsExport.setOnClickListener {
            alertDialog.dismiss()
            listener.onExportClick()
        }
        alertDialog.textNoteOptionsDelete.setOnClickListener {
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
        private val DEFAULT_LISTENER = object : NoteOptionsListener {
            override fun onLockClick() = Unit
            override fun onUnlockClick() = Unit
            override fun onExportClick() = Unit
            override fun onDeleteClick() = Unit
        }

        interface NoteOptionsListener {
            fun onLockClick()
            fun onUnlockClick()
            fun onExportClick()
            fun onDeleteClick()
        }
    }
}
