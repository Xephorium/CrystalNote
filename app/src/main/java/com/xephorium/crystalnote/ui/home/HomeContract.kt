package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface HomeContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<Note>)
        fun showEmptyNotesList()

        fun showNavigationDrawer()
        fun showNoteOptionsDialog()
        fun showSetNewPasswordDialog()
        fun showVerifyNewPasswordDialog(password: String)
        fun showNoteLockedMessage()
        fun showRemovePasswordDialog(password: String)
        fun showNoteUnlockedMessage()
        fun showFileWritePermissionDeniedMessage()
        fun showExportDialog()
        fun showExportConfirmationMessage()
        fun showDeleteNoteDialog()
        fun showNoteDeletedMessage()
        fun showLockedNoteOptionsDialog()
        fun showNotePasswordDialog(password: String, id: Int)
        fun navigateToEditNote(id: Int)
        fun navigateToNewNote()

        fun requestFileWritePermission()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRoomRepository: NoteRoomRepository
        lateinit var noteDiskRepository: NoteDiskRepository
        var fromUpdateActivity: Boolean = false
        var isFileWritePermissionGranted: Boolean = false
        var selectedNote: Note? = null

        abstract fun handleMenuButtonClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: Note)
        abstract fun handleNoteAuthenticate(id: Int)
        abstract fun handleNoteLongClick(note: Note)
        abstract fun handleLockClick()
        abstract fun handleNewPasswordSet(password: String)
        abstract fun handleNewPasswordVerify(password: String)
        abstract fun handleUnlockClick()
        abstract fun handleOldPasswordVerify()
        abstract fun handleExportClick()
        abstract fun handleFileWritePermissionGranted()
        abstract fun handleFileWritePermissionDenied()
        abstract fun handleExportConfirm()
        abstract fun handleDeleteClick()
        abstract fun handleDeleteConfirm()
        abstract fun handleNoteListRefresh()
    }
}
