package com.xephorium.crystalnote.ui.update

import android.net.Uri
import android.os.Build
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface UpdateFileContract {

    interface View : BaseView {
        fun populateFields(name: String, content: String)
        fun disableFileEdit()
        fun showTextUnderline()
        fun hideTextUnderline()
        fun showMonospacedFont()

        fun showFileOptionsDialog(isFileImported: Boolean, isLegacyBuild: Boolean)
        fun showRestoreDialog()
        fun showImportDialog()
        fun showImportSuccessMessage()
        fun showFileSavedMessage()
        fun showFileAccessDeniedMessage()
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
        var name: String = ""
        var initialContent: String? = null
        var content: String = ""
        var newNoteId: Int = NO_NOTE

        val isFirstLaunch: Boolean
            get() = initialContent == null

        val isFileImported: Boolean
            get() = newNoteId != NO_NOTE

        val isLegacyBuild: Boolean
            get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q

        // Legacy Permissions (API <= Android 10)
        var isFileWriteInitiallyPermitted: Boolean = false
        var isFileWriteGranted: Boolean = false
        var isFileWriteDenied: Boolean = false

        abstract fun handleContentTextChange(content: String)
        abstract fun handleFileWritePermissionGranted()
        abstract fun handleFileWritePermissionDenied()
        abstract fun handleFileWritePermissionDeniedConfirm()
        abstract fun handleFileOptionsClick()
        abstract fun handleRestoreClick()
        abstract fun handleRestoreConfirm()
        abstract fun handleImportClick()
        abstract fun handleImportConfirm()
        abstract fun handleOpenNoteClick()
        abstract fun handleBackClick()
        abstract fun handleDestroy()
    }
}
