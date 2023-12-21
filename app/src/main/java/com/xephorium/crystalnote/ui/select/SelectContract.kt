package com.xephorium.crystalnote.ui.select

import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

interface SelectContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<PreviewNote>)
        fun showEmptyNotesList()

        fun refreshWidget()
        fun navigateBack()
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        lateinit var noteRepository: NoteRoomRepository
        lateinit var sharedPreferencesRepository: SharedPreferencesRepository
        var widgetId = 0

        abstract fun handleToolbarBackClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: PreviewNote)
        abstract fun handleNoteLongClick(note: PreviewNote)
        abstract fun handleNoteListRefresh()
    }
}
