package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.base.BaseView

class HomeContract {

    interface View : BaseView {
        fun populateNoteList(notes: List<Note>)

        fun showNavigationDrawer()
        fun showSearchDialog()
        fun navigateToNewNote()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun handleMenuButtonClick()
        abstract fun handleSearchButtonClick()
        abstract fun handleNewNoteButtonClick()
        abstract fun handleNoteClick(note: Note)
        abstract fun handleNoteLongClick(note: Note)
        abstract fun handleNoteListRefresh()
    }
}
