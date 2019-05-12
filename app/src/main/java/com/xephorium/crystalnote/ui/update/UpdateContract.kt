package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface UpdateContract {

    interface View : BaseView {
        fun populateFields(name: String, content: String)

        fun showDraftDiscardedMessage()
        fun showInvalidNoteMessage(message: String)
        fun navigateBack()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRepository: NoteRepository

        var isInEditMode: Boolean = false
        var initialName: String = ""
        var initialContent: String = ""
        var name: String = ""
        var content: String = ""

        abstract fun handleNameTextChange(name: String)
        abstract fun handleContentTextChange(content: String)
        abstract fun handleBackClick()
        abstract fun handleSaveClick()
    }
}
