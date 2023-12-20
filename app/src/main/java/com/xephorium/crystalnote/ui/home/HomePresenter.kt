package com.xephorium.crystalnote.ui.home

import android.net.Uri
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
        selectedNote = note
        if (note.password.isBlank()) {
            view?.showNoteOptionsDialog()
        } else {
            view?.showLockedNoteOptionsDialog()
        }
    }

    override fun handleLockClick() {
        view?.showSetNewPasswordDialog()
    }

    override fun handleNewPasswordSet(password: String) {
        view?.showVerifyNewPasswordDialog(password)
    }

    override fun handleNewPasswordVerify(password: String) {
        selectedNote?.let {
            view?.showNoteLockedMessage()
            noteRoomRepository.updateNotePassword(it.id, password)
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleUnlockClick() {
        selectedNote?.let { view?.showRemovePasswordDialog(it.password) }
    }

    override fun handleOldPasswordVerify() {
        selectedNote?.let {
            view?.showNoteUnlockedMessage()
            noteRoomRepository.updateNotePassword(it.id, "")
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleExportClick() {
        selectedNote?.run {
            view?.showExportDialog(noteDiskRepository.getSanitizedExportFileName(name))
        }
    }

    override fun handleExportFileCreated(uri: Uri) {
        selectedNote?.run {
            noteRoomRepository.getFullNote(id)?.run {
                if (contents != null && noteDiskRepository.writeStringToTextFile(uri, contents!!)) {
                    view?.showExportConfirmationMessage()
                } else {
                    view?.showExportErrorMessage()
                }
            }
        }
    }

    override fun handleExportConfirm() {
        selectedNote?.run {
            noteRoomRepository.getFullNote(id)?.run {
                if (contents != null && noteDiskRepository.exportNoteToDownloads(name, contents!!)) {
                    view?.showExportConfirmationMessage()
                } else {
                    view?.showExportErrorMessage()
                }
            }
        }
    }

    override fun handleDeleteClick() {
        view?.showDeleteNoteDialog()
    }

    override fun handleDeleteConfirm() {
        selectedNote?.let {
            noteRoomRepository.deleteNote(it.id)
            view?.showNoteDeletedMessage()
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleNoteListRefresh() {
        refreshNoteList()
    }


    /*--- Private Methods ---*/

    private fun refreshNoteList() {
        val list = noteRoomRepository.getLightweightNotes().toMutableList()

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
