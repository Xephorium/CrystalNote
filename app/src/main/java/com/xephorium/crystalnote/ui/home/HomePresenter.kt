package com.xephorium.crystalnote.ui.home

import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.utility.NoteUtility

class HomePresenter : HomeContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: HomeContract.View) {
        super.attachView(view)

        // TODO - Find Better Solution
        // Note: Refresh delayed minimum amount to account for async
        //       database access in UpdateActivity.
        if (fromUpdateActivity) beginDelayedNoteListRefresh {
            refreshNoteList()
        } else refreshNoteList()
    }

    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleNewNoteButtonClick() {
        view?.navigateToNewNote()
    }

    override fun handleNoteClick(note: Note) {
        if (note.password.isNotEmpty()) {
            view?.showNotePasswordDialog(note.password, note.id)
        } else {
            view?.navigateToEditNote(note.id)
        }
    }

    override fun handleNoteAuthenticate(id: Int) {
        view?.navigateToEditNote(id)
    }

    override fun handleNoteLongClick(note: Note) {
        if (note.password.isBlank()) {
            view?.showNoteOptionsDialog()
        } else {
            view?.showLockedNoteOptionsDialog()
        }
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

    private fun beginDelayedNoteListRefresh(afterDelay: () -> Unit) {
        Thread.sleep(REFRESH_DELAY)
        afterDelay()
    }


    /*--- Constants ---*/

    companion object {
        private const val REFRESH_DELAY: Long = 50
    }
}
