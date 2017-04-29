package com.xephorium.crystalnote.ui.selection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xephorium.crystalnote.data.NoteManager;
import com.xephorium.crystalnote.data.SharedPreferencesManager;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.data.util.NoteUtils.SortType;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.IntentLibrary;
import com.xephorium.crystalnote.ui.base.BasePresenter;
import com.xephorium.crystalnote.ui.creation.CreationActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectionPresenter extends BasePresenter<SelectionView> {

    private Context context;
    private NoteManager noteManager;
    private List<Note> newNoteList;
    private List<Note> oldNoteList;

    public SelectionPresenter(SelectionView view) {
        attachView(view);
        this.context = (Context) view;
        this.noteManager = new NoteManager(context);
    }

    public void refreshNoteList() {
        parseNotes(NoteUtils.sortNotes(noteManager.getNotes(), SortType.DATE_NEW));
        getView().populateNoteList(this.newNoteList, this.oldNoteList);
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
        SharedPreferencesManager.setDisplayNoteName(context, note);
        refreshWidget();
        getView().closeSelectionActivity();
    }

    public boolean handleNoteLongClick(Note note) {
        return true;
    }

    public List<Note> getNewNoteList() {
        return newNoteList;
    }

    public void setNewNoteList(List<Note> newNoteList) {
        this.newNoteList = newNoteList;
    }

    public List<Note> getOldNoteList() {
        return oldNoteList;
    }

    public void setOldNoteList(List<Note> oldNoteList) {
        this.oldNoteList = oldNoteList;
    }

    private void parseNotes(List<Note> notes) {
        List<Note> newNotes = new ArrayList<>();
        List<Note> oldNotes = new ArrayList<>();

        for (int x = 0; x < notes.size(); x++) {
            if (Calendar.getInstance().getTime().getDay() == notes.get(x).getDate().getDay())
                newNotes.add(notes.get(x));
            else
                oldNotes.add(notes.get(x));
        }

        this.newNoteList = newNotes;
        this.oldNoteList = oldNotes;
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