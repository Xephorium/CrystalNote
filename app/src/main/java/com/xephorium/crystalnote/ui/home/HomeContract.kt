package com.xephorium.crystalnote.ui.home

import android.net.Uri
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView


interface HomeContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<PreviewNote>, showArchived: Boolean)
        fun showHomeOptionsIcon()
        fun showEmptyNotesList()

        fun showNavigationDrawer()
        fun showNoteOptionsDialog(isArchived: Boolean)
        fun showLockedNoteOptionsDialog(isArchived: Boolean)
        fun showHomeOptionsDialog(showArchived: Boolean)
        fun showSetNewPasswordDialog()
        fun showVerifyNewPasswordDialog(password: String)
        fun showNoteLockedMessage()
        fun showRemovePasswordDialog(password: String)
        fun showNoteUnlockedMessage()
        fun showFileWritePermissionDeniedMessage()
        fun showExportDialog(noteName: String)
        fun showExportConfirmationMessage()
        fun showExportErrorMessage()
        fun showNoteArchivedMessage()
        fun showNoteUnarchivedMessage()
        fun showDeleteNoteDialog()
        fun showNoteDeletedMessage()
        fun showNotePasswordDialog(password: String, id: Int)
        fun navigateToEditNote(id: Int)
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRoomRepository: NoteRoomRepository
        lateinit var noteDiskRepository: NoteDiskRepository

        var fromUpdateActivity: Boolean = false
        var selectedPreviewNote: PreviewNote? = null
        var showArchived: Boolean = false

        abstract fun handleMenuButtonClick()
        abstract fun handleHomeOptionsClick()
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
        abstract fun handleArchiveClick()
        abstract fun handleDeleteClick()
        abstract fun handleDeleteConfirm()
        abstract fun handleShowArchivedClick()
        abstract fun handleNoteListRefresh()
    }
}
