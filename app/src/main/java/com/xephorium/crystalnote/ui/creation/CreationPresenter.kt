package com.xephorium.crystalnote.ui.creation

import com.xephorium.crystalnote.data.util.NoteUtils

class CreationPresenter(view: CreationContract.View) : CreationContract.Presenter() {


    /*--- Business Logic Methods ---*/

    override fun handleNameTextChange(name: String) {
        this.name = name
    }

    override fun handleContentTextChange(content: String) {
        this.content = content
    }

    override fun handleBackClick() {
        // TODO - Implement Warning
        if (isNoteValid()) view?.showDraftDiscardedMessage()
        view?.navigateBack()
    }

    override fun handleSaveClick() {
        if (!isNoteValid()) {
            view?.showInvalidNoteMessage(getInvalidNoteMessage())
        } else {
            noteRepository.writeToNote(name, content)
            view?.navigateBack()
        }
    }


    /*--- Private Methods ---*/

    private fun isNoteValid(): Boolean = NoteUtils.isValidNoteName(name)
            && NoteUtils.isValidNoteContent(content)
            && !noteRepository.noteExists(name)

    private fun getInvalidNoteMessage() : String {
        return when {
            name.isBlank() && content.isBlank() -> "No Changes to Save"
            !NoteUtils.isValidNoteName(name) -> "Invalid Note Name"
            noteRepository.noteExists(name) -> "Note Already Exists"
            else -> "Note Invalid"
        }
    }
}
