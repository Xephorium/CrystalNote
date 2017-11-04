package com.xephorium.crystalnote.ui.selection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xephorium.crystalnote.data.NoteRepository;
import com.xephorium.crystalnote.data.SharedPreferencesRepository;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.data.util.NoteUtils.SortType;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.IntentLibrary;
import com.xephorium.crystalnote.ui.base.BasePresenter;
import com.xephorium.crystalnote.ui.creation.CreationActivity;

public class SelectionPresenter extends BasePresenter<SelectionView> {

    private Context context;
    private NoteRepository noteRepository;

    public SelectionPresenter(SelectionView view) {
        attachView(view);
        this.context = (Context) view;
        this.noteRepository = new NoteRepository(context);
    }

    public void refreshNoteList() {
        getView().populateNoteList(NoteUtils.sortNotes(noteRepository.getNotes(), SortType.DATE_NEW));
    }

    public void handleToolbarBackClick() {
        getView().closeSelectionActivity();
    }

    public void handleActionButtonClick() {
        Intent intent = new Intent(context, CreationActivity.class);
        intent.setAction(IntentLibrary.CREATE_NOTE);
        ((Activity) getView()).startActivity(intent);
    }

    public void handleNoteClick(Note note) {
        SharedPreferencesRepository.setDisplayNoteName(context, note);
        refreshWidget();
        getView().closeSelectionActivity();
    }

    public boolean handleNoteLongClick(Note note) {
        // Do Nothing
        return true;
    }

    private void refreshWidget() {
        Intent intent = new Intent();
        intent.setAction(IntentLibrary.UPDATE_NOTE_INTENT);
        context.sendBroadcast(intent);
    }
}

// Text Edit Launch Intent
// Intent intent = new Intent(Intent.ACTION_VIEW);
// Uri uri = Uri.parse("file://" + note.getPath());
// intent.setDataAndType(uri, "text/plain");
// this.startActivity(intent);