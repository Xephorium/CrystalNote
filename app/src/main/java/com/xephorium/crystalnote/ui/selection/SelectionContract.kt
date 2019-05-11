package com.xephorium.crystalnote.ui.selection

import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SelectionContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<Note>)

        fun refreshWidget()
        fun navigateBack()
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRepository: NoteRepository
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        abstract fun handleToolbarBackClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: Note)
        abstract fun handleNoteLongClick(note: Note)
        abstract fun handleNoteListRefresh()
    }
}
