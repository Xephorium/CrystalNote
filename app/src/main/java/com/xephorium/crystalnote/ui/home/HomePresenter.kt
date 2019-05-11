package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.util.NoteUtils

class HomePresenter : HomeContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: HomeContract.View) {
        super.attachView(view)

        refreshNoteList()
    }


    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleSearchButtonClick() {
        view?.showSearchDialog()
    }

    override fun handleNewNoteButtonClick() {
        view?.navigateToNewNote()
    }

    override fun handleNoteClick(note: Note) {
        // Do Nothing
    }

    override fun handleNoteLongClick(note: Note) {
        // Do Nothing
    }

    override fun handleNoteListRefresh() {
        refreshNoteList()
    }


    /*--- Private Methods ---*/

    private fun refreshNoteList() {
        view?.populateNoteList(NoteUtils.sortNotes(
                noteRepository.notes,
                NoteUtils.SortType.DATE_NEW
        ))
    }
}
