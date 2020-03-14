package com.xephorium.crystalnote.ui.update

import android.net.Uri
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface UpdateFileContract {

    interface View : BaseView {
        fun populateFields(name: String, content: String)
        fun showTextUnderline()
        fun hideTextUnderline()
        fun showMonospacedFont()

        fun showRevertDialog()
        fun showImportDialog()
        fun showImportSuccessMessage()
        fun showOpenNoteMenuOption()
        fun showFileSavedMessage()
        fun showErrorReadingFileMessage()
        fun navigateBack()
        fun navigateToUpdateNote(id: Int)
        fun hideKeyboard()

        fun requestFileWritePermission()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRoomRepository: NoteRoomRepository
        lateinit var noteDiskRepository: NoteDiskRepository

        var fileUri: Uri? = null
        var isFileWriteInitiallyPermitted: Boolean = false
        var isFileWriteGranted: Boolean = false
        var isFileWriteDenied: Boolean = false
        var newNoteId: Int = NO_NOTE
        var name: String = ""
        var initialContent: String = ""
        var content: String = ""

        abstract fun handleContentTextChange(content: String)
        abstract fun handleFileWritePermissionGranted()
        abstract fun handleFileWritePermissionDenied()
        abstract fun handleFileWritePermissionDeniedConfirm()
        abstract fun handleRevertClick()
        abstract fun handleRevertConfirm()
        abstract fun handleImportClick()
        abstract fun handleImportConfirm()
        abstract fun handleOpenNoteClick()
        abstract fun handleBackClick()
        abstract fun handleDestroy()
    }
}
