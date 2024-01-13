package com.xephorium.crystalnote.ui.home

import android.net.Uri
import com.xephorium.crystalnote.data.model.PreviewNote
import com.xephorium.crystalnote.data.utility.NoteUtility


class HomePresenter : HomeContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: HomeContract.View) {
        super.attachView(view)

        showArchived = sharedPreferencesRepository.getShowArchivedNotesEnabled()

        // TODO - Find Better Solution
        // Note: Refresh delayed minimum amount to account for async
        //       database access in UpdateActivity.
        if (fromUpdateActivity) beginDelayedNoteListRefresh {
            refreshNoteList()
        } else refreshNoteList()

        if (sharedPreferencesRepository.getHomeOptionsEnabled()) view.showHomeOptionsIcon()
    }

    /*--- Action Handling Methods ---*/

    override fun handleMenuButtonClick() {
        view?.showNavigationDrawer()
    }

    override fun handleHomeOptionsClick() {
        view?.showHomeOptionsDialog(showArchived)
    }

    override fun handleNewNoteButtonClick() {
        view?.navigateToNewNote()
    }

    override fun handleNoteClick(note: PreviewNote) {
        if (note.password.isNotEmpty()) {
            view?.showNotePasswordDialog(note.password, note.id)
        } else {
            view?.navigateToEditNote(note.id)
        }
    }

    override fun handleNoteAuthenticate(id: Int) {
        view?.navigateToEditNote(id)
    }

    override fun handleNoteLongClick(note: PreviewNote) {
        selectedPreviewNote = note
        if (note.password.isBlank()) {
            view?.showNoteOptionsDialog(note.archived)
        } else {
            view?.showLockedNoteOptionsDialog(note.archived)
        }
    }

    override fun handleLockClick() {
        view?.showSetNewPasswordDialog()
    }

    override fun handleNewPasswordSet(password: String) {
        view?.showVerifyNewPasswordDialog(password)
    }

    override fun handleNewPasswordVerify(password: String) {
        selectedPreviewNote?.let {
            view?.showNoteLockedMessage()
            noteRoomRepository.updateNotePassword(it.id, password)
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleUnlockClick() {
        selectedPreviewNote?.let { view?.showRemovePasswordDialog(it.password) }
    }

    override fun handleOldPasswordVerify() {
        selectedPreviewNote?.let {
            view?.showNoteUnlockedMessage()
            noteRoomRepository.updateNotePassword(it.id, "")
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleExportClick() {
        selectedPreviewNote?.run {
            view?.showExportDialog(noteDiskRepository.getSanitizedExportFileName(name))
        }
    }

    override fun handleExportFileCreated(uri: Uri) {
        selectedPreviewNote?.run {
            noteRoomRepository.getNote(id)?.run {
                if (noteDiskRepository.writeStringToTextFile(uri, contents)) {
                    view?.showExportConfirmationMessage()
                } else {
                    view?.showExportErrorMessage()
                }
            }
        }
    }

    override fun handleExportConfirm() {
        selectedPreviewNote?.run {
            noteRoomRepository.getNote(id)?.run {
                if (noteDiskRepository.exportNoteToDownloads(name, contents)) {
                    view?.showExportConfirmationMessage()
                } else {
                    view?.showExportErrorMessage()
                }
            }
        }
    }

    override fun handleArchiveClick() {
        selectedPreviewNote?.run {
            archived = !archived
            noteRoomRepository.updateNoteArchival(id, archived)
            if (!archived) view?.showNoteUnarchivedMessage()
            else view?.showNoteArchivedMessage()
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleDeleteClick() {
        view?.showDeleteNoteDialog()
    }

    override fun handleDeleteConfirm() {
        selectedPreviewNote?.let {
            noteRoomRepository.deleteNote(it.id)
            view?.showNoteDeletedMessage()
            beginDelayedNoteListRefresh {
                refreshNoteList()
            }
        }
    }

    override fun handleShowArchivedClick() {
        showArchived = !showArchived
        sharedPreferencesRepository.setShowArchivedNotesEnabled(showArchived)
        refreshNoteList()
    }

    override fun handleNoteListRefresh() {
        refreshNoteList()
    }


    /*--- Private Methods ---*/

    private fun refreshNoteList() {
        val list = noteRoomRepository.getPreviewNotes().toMutableList()

        if (list.isNotEmpty()) {
            view?.populateNoteList(
                NoteUtility.sortPreviewNotes(list, NoteUtility.SortType.DATE_NEW),
                showArchived
            )
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
