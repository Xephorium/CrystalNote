package com.xephorium.crystalnote.ui.creation

import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface CreationContract {

    interface View : BaseView {
        fun showDraftDiscardedMessage()
        fun showInvalidNoteMessage(message: String)
        fun navigateBack()
    }

    abstract class Presenter : BasePresenter<View>() {
        var name: String = ""
        var content: String = ""
        lateinit var noteRepository: NoteRepository

        abstract fun handleNameTextChange(name: String)
        abstract fun handleContentTextChange(content: String)
        abstract fun handleBackClick()
        abstract fun handleSaveClick()
    }
}
