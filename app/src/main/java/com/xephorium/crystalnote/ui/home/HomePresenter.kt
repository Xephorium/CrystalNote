package com.xephorium.crystalnote.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View

import com.xephorium.crystalnote.data.NoteRepository
import com.xephorium.crystalnote.data.model.Note
import com.xephorium.crystalnote.data.util.NoteUtils
import com.xephorium.crystalnote.ui.IntentLibrary
import com.xephorium.crystalnote.ui.base.BasePresenter
import com.xephorium.crystalnote.ui.creation.CreationActivity

class HomePresenter : HomeContract.Presenter() {


    /*--- Variable Declarations ---*/

    lateinit var noteRepository: NoteRepository


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: HomeContract.View?) {
        super.attachView(view)

        refreshNoteList()
    }


    /*--- Business Logic Methods ---*/

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
