package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface HomeContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<Note>)
        fun showEmptyNotesList()

        fun showNavigationDrawer()
        fun navigateToEditNote(id: Int)
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRepository: NoteRoomRepository
        var fromUpdateActivity: Boolean = false

        abstract fun handleMenuButtonClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: Note)
        abstract fun handleNoteLongClick(note: Note)
        abstract fun handleNoteListRefresh()
    }
}
