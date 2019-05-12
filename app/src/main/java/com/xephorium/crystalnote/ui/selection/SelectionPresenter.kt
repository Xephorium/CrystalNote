package com.xephorium.crystalnote.ui.selection

import com.xephorium.crystalnote.data.utility.NoteUtility
import com.xephorium.crystalnote.data.utility.NoteUtility.SortType
import com.xephorium.crystalnote.data.model.Note

class SelectionPresenter : SelectionContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: SelectionContract.View) {
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
        sharedPreferencesRepository.setDisplayNoteName(note)
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
        view?.populateNoteList(NoteUtility.sortNotes(
                noteRepository.getNotes().toMutableList(),
                SortType.DATE_NEW
        ))
    }
}

// Text Edit Launch Intent
// Intent intent = new Intent(Intent.ACTION_VIEW);
// Uri uri = Uri.parse("file://" + note.getPath());
// intent.setDataAndType(uri, "text/plain");
// this.startActivity(intent);