package com.xephorium.crystalnote.ui.select

import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.model.Note

class SelectPresenter : SelectContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: SelectContract.View) {
        super.attachView(view)

        refreshNoteList()
    }


    /*--- Action Handling Methods ---*/

    override fun handleToolbarBackClick() {
        view?.navigateBack()
    }

    override fun handleNewNoteButtonClick() {
        view?.navigateToNewNote()
    }

    override fun handleNoteClick(note: Note) {
        sharedPreferencesRepository.setDisplayNoteId(note.id)
        view?.refreshWidget()
        view?.navigateBack()
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