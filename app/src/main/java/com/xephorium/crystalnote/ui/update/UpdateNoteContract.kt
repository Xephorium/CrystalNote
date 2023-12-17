package com.xephorium.crystalnote.ui.update

import android.net.Uri
import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteDiskRepository
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView


interface UpdateNoteContract {

    interface View : BaseView {
        fun populateFields(name: String, content: String)
        fun populateColor(color: Int)
        fun showBottomButton()
        fun hideBottomButton()

        fun showTextUnderline()
        fun hideTextUnderline()
        fun showMonospacedFont()

        fun scrollToBottom()
        fun showNoteOptionsDialog(isInEditMode: Boolean, isLocked: Boolean)
        fun showColorPickerDialog()
        fun showSetNewPasswordDialog()
        fun showVerifyNewPasswordDialog(password: String)
        fun showNoteLockedMessage()
        fun showRemovePasswordDialog(password: String)
        fun showNoteUnlockedMessage()
        fun showInvalidNameDialog()
        fun showDiscardChangesDialog()
        fun showDeleteNoteDialog()
        fun showExportDialog(noteName: String)
        fun showExportConfirmationMessage()
        fun showExportErrorMessage()
        fun showRestoreDialog()
        fun showRestoreConfirmationMessage()
        fun navigateHome()
        fun navigateBack()
        fun refreshWidget()
        fun hideKeyboard()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRoomRepository: NoteRoomRepository
        lateinit var noteDiskRepository: NoteDiskRepository

        var isInEditMode: Boolean = false
        var isLaunchFromWidget: Boolean = false
        var isLaunchFromSelect: Boolean = false
        var isLaunchFromUpdateFile: Boolean = false
        var noteId: Int = NO_NOTE
        var initialName: String = ""
        var initialContent: String = ""
        var initialColor: Int = NoteUtility.getDefaultColor()
        var initialPassword: String = ""
        var name: String = ""
        var content: String = ""
        var color: Int = NoteUtility.getDefaultColor()
        var password: String = ""

        abstract fun handleNameTextChange(name: String)
        abstract fun handleContentTextChange(content: String)
        abstract fun handleBottomClick()
        abstract fun handleColorClick()
        abstract fun handleColorChange(color: Int)
        abstract fun handleBackClick()
        abstract fun handleBackground()
        abstract fun handleNoteOptionsClicked()
        abstract fun handleLockClick()
        abstract fun handleNewPasswordSet(password: String)
        abstract fun handleNewPasswordVerify(password: String)
        abstract fun handleUnlockClick()
        abstract fun handleOldPasswordVerify()
        abstract fun handleExportClick()
        abstract fun handleExportFileCreated(uri: Uri)
        abstract fun handleRestoreClick()
        abstract fun handleRestoreConfirm()
        abstract fun handleDeleteClick()
        abstract fun handleDeleteConfirm()
        abstract fun handleDiscardChangesConfirm()

        companion object {
            const val SHOW_BOTTOM_BUTTON_THRESHOLD = 100 // 4 screens worth of text on a Pixel 1
        }
    }
}
