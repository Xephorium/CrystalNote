package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.utility.NoteUtility

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

    override fun handleNewNoteButtonClick() {
        view?.navigateToNewNote()
    }

    override fun handleNoteClick(note: Note) {
        view?.navigateToEditNote(note.name)
    }

    override fun handleNoteLongClick(note: Note) {
        // Do Nothing
    }

    override fun handleNoteListRefresh() {
        refreshNoteList()
    }


    /*--- Private Methods ---*/

    private fun refreshNoteList() {
        val list = noteRepository.getNotes().toMutableList()

        if (list.isNotEmpty()) {
            view?.populateNoteList(NoteUtility.sortNotes(list, NoteUtility.SortType.DATE_NEW))
        } else {
            view?.showEmptyNotesList()
        }
    }
}
