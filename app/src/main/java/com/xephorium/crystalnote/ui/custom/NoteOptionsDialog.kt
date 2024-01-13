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
    private var title = "Note Options"
    private var showLock = true
    private var showUnlock = true
    private var showImport = false
    private var showExport = true
    private var showOpen = false
    private var showRestore = false
    private var showArchive = false
    private var isArchived = false
    private var showDelete = true


    /*--- Public Methods ---*/

    fun setTitle(newTitle: String) {
        title = newTitle
    }

    fun hideLockOption() {
        showLock = false
    }

    fun hideUnlockOption() {
        showUnlock = false
    }

    fun showImportOption() {
        showImport = true
    }

    fun hideExportOption() {
        showExport = false
    }

    fun showOpenOption() {
        showOpen = true
    }

    fun showRestoreOption() {
        showRestore = true
    }

    fun showArchiveOption() {
        showArchive = true
    }

    fun setIsArchived(archived: Boolean) {
        isArchived = archived
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
        val textTitle = alertDialog.findViewById<TextView>(R.id.textNoteTitle)
        val textLock = alertDialog.findViewById<TextView>(R.id.textNoteOptionsLock)
        val textUnlock = alertDialog.findViewById<TextView>(R.id.textNoteOptionsUnlock)
        val textImport = alertDialog.findViewById<TextView>(R.id.textNoteOptionsImport)
        val textExport = alertDialog.findViewById<TextView>(R.id.textNoteOptionsExport)
        val textOpen = alertDialog.findViewById<TextView>(R.id.textNoteOptionsOpen)
        val textRestore = alertDialog.findViewById<TextView>(R.id.textNoteOptionsRestore)
        val textArchive = alertDialog.findViewById<TextView>(R.id.textNoteOptionsArchive)
        val textDelete = alertDialog.findViewById<TextView>(R.id.textNoteOptionsDelete)

        textTitle?.text = title
        textLock?.visibility = if (showLock) View.VISIBLE else View.GONE
        textUnlock?.visibility = if (showUnlock) View.VISIBLE else View.GONE
        textImport?.visibility = if (showImport) View.VISIBLE else View.GONE
        textExport?.visibility = if (showExport) View.VISIBLE else View.GONE
        textOpen?.visibility = if (showOpen) View.VISIBLE else View.GONE
        textRestore?.visibility = if (showRestore) View.VISIBLE else View.GONE
        textArchive?.visibility = if (showArchive) View.VISIBLE else View.GONE
        textArchive?.text = if (isArchived) context.resources.getString(R.string.noteOptionsUnarchive)
        else context.resources.getString(R.string.noteOptionsArchive)
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
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsImport)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onImportClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsExport)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onExportClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsOpen)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onOpenClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsRestore)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onRestoreClick()
        }
        alertDialog.findViewById<TextView>(R.id.textNoteOptionsArchive)?.setOnClickListener {
            Thread.sleep(ANIMATION_DELAY)
            alertDialog.dismiss()
            listener.onArchiveClick()
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
            override fun onImportClick() = Unit
            override fun onExportClick() = Unit
            override fun onOpenClick() = Unit
            override fun onRestoreClick() = Unit
            override fun onArchiveClick() = Unit
            override fun onDeleteClick() = Unit
        }

        interface NoteOptionsListener {
            fun onLockClick()
            fun onUnlockClick()
            fun onImportClick()
            fun onExportClick()
            fun onOpenClick()
            fun onRestoreClick()
            fun onArchiveClick()
            fun onDeleteClick()
        }
    }
}
