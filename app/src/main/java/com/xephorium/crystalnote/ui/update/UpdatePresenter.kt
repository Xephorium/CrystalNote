package com.xephorium.crystalnote.ui.update

import com.xephorium.crystalnote.data.validation.NoteValidator

class UpdatePresenter : UpdateContract.Presenter() {


    /*--- Lifecycle Methods ---*/

    override fun attachView(view: UpdateContract.View) {
        super.attachView(view)

        if (isInEditMode) {

            // Get Note
            noteRepository.getNote(initialName)?.let {

                // Update State Fields
                name = initialName
                initialContent = noteRepository.readNoteContents(it.name)
                content = initialContent

                // Update View
                this.view?.populateFields(it.name, noteRepository.readNoteContents(it.name))
            }
        }
    }


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
