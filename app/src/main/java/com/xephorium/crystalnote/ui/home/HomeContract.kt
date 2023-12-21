package com.xephorium.crystalnote.ui.home

import android.net.Uri
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView


interface HomeContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<PreviewNote>)
        fun showEmptyNotesList()

        fun showNavigationDrawer()
        fun showNoteOptionsDialog()
        fun showSetNewPasswordDialog()
        fun showVerifyNewPasswordDialog(password: String)
        fun showNoteLockedMessage()
        fun showRemovePasswordDialog(password: String)
        fun showNoteUnlockedMessage()
        fun showFileWritePermissionDeniedMessage()
        fun showExportDialog(noteName: String)
        fun showExportConfirmationMessage()
        fun showExportErrorMessage()
        fun showDeleteNoteDialog()
        fun showNoteDeletedMessage()
        fun showLockedNoteOptionsDialog()
        fun showNotePasswordDialog(password: String, id: Int)
        fun navigateToEditNote(id: Int)
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRoomRepository: NoteRoomRepository
        lateinit var noteDiskRepository: NoteDiskRepository
        var fromUpdateActivity: Boolean = false
        var selectedPreviewNote: PreviewNote? = null

        abstract fun handleMenuButtonClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: PreviewNote)
        abstract fun handleNoteAuthenticate(id: Int)
        abstract fun handleNoteLongClick(note: PreviewNote)
        abstract fun handleLockClick()
        abstract fun handleNewPasswordSet(password: String)
        abstract fun handleNewPasswordVerify(password: String)
        abstract fun handleUnlockClick()
        abstract fun handleOldPasswordVerify()
        abstract fun handleExportClick()
        abstract fun handleExportFileCreated(uri: Uri)
        abstract fun handleExportConfirm()
        abstract fun handleDeleteClick()
        abstract fun handleDeleteConfirm()
        abstract fun handleNoteListRefresh()
    }
}
