package com.xephorium.crystalnote.ui.creation;

import android.content.Context;
import android.widget.Toast;

import com.xephorium.crystalnote.data.NoteRepository;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.ui.base.BasePresenter;

public class CreationPresenter extends BasePresenter<CreationView> {

    private Context context;
    private NoteRepository noteRepository;

    public CreationPresenter(CreationView view) {
        attachView(view);
        this.context = (Context) view;
        this.noteRepository = new NoteRepository(context);
    }

    public void handleBackClick() {
        if (getView().isNewNotePopulated())
            Toast.makeText(context, "Draft Discarded", Toast.LENGTH_SHORT).show(); // TODO - Implement Warning
        getView().closeCreationActivity();
    }

    public void handleSaveClick(String noteName, String noteContent) {
        if (!checkNoteValidity(noteName, noteContent))
            return;

        noteRepository.writeToNote(noteName, noteContent);
        getView().closeCreationActivity();
    }

    private boolean checkNoteValidity(String name, String content) {
        if (!getView().isNewNotePopulated()) {
            Toast.makeText(context, "No Changes to Save", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!NoteUtils.isValidNoteName(name)) {
            Toast.makeText(context, "Invalid Note Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (noteRepository.noteExists(name)) {
            Toast.makeText(context, "Note Already Exists", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show();
        return true;
    }
}
