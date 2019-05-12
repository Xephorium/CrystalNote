package com.xephorium.crystalnote.ui.creation

import com.xephorium.crystalnote.data.validation.NoteValidator

class CreationPresenter : CreationContract.Presenter() {


    /*--- Action Handling Methods ---*/

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

    private fun isNoteValid(): Boolean = NoteValidator.isValidNoteName(name)
            && NoteValidator.isValidNoteContent(content)
            && !noteRepository.noteExists(name)

    private fun getInvalidNoteMessage() : String {
        return when {
            name.isBlank() && content.isBlank() -> "No Changes to Save"
            !NoteValidator.isValidNoteName(name) -> "Invalid Note Name"
            noteRepository.noteExists(name) -> "Note Already Exists"
            else -> "Note Invalid"
        }
    }
}
