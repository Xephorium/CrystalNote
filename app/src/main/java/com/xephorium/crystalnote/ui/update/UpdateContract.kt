package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.model.Note.Companion.NO_NOTE
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface UpdateContract {

    interface View : BaseView {
        fun populateFields(name: String, content: String)
        fun populateColor(color: Int)
        fun showTextUnderline()
        fun hideTextUnderline()
        fun showMonospacedFont()
        fun showLockMenuOption()
        fun showUnlockMenuOption()

        fun showColorPickerDialog()
        fun showInvalidNameDialog()
        fun showDiscardChangesDialog()
        fun showDeleteNoteDialog()
        fun navigateHome()
        fun navigateBack()
        fun refreshWidget()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        lateinit var noteRepository: NoteRoomRepository

        var isInEditMode: Boolean = false
        var isLaunchFromWidget: Boolean = false
        var isLaunchFromSelect: Boolean = false
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
        abstract fun handleColorClick()
        abstract fun handleColorChange(color: Int)
        abstract fun handleBackClick()
        abstract fun handleBackground()
        abstract fun handleLockClick()
        abstract fun handleUnlockClick()
        abstract fun handleDeleteClick()
        abstract fun handleDeleteConfirm()
        abstract fun handleDiscardChangesConfirm()
    }
}
