package com.xephorium.crystalnote.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xephorium.crystalnote.data.NoteRepository;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.ui.IntentLibrary;
import com.xephorium.crystalnote.ui.base.BasePresenter;
import com.xephorium.crystalnote.ui.creation.CreationActivity;

public class HomePresenter extends BasePresenter<HomeView> {

    private Context context;
    private NoteRepository noteRepository;

    public HomePresenter(HomeView view) {
        attachView(view);
        this.context = (Context) view;
        this.noteRepository = new NoteRepository(context);
    }

    public void refreshNoteList() {
        getView().populateNoteList(NoteUtils.sortNotes(noteRepository.getNotes(), NoteUtils.SortType.DATE_NEW));
    }

    public void handleActionButtonClick() {
        Intent intent = new Intent(context, CreationActivity.class);
        intent.setAction(IntentLibrary.CREATE_NOTE);
        ((Activity) getView()).startActivity(intent);
    }

    public void handleNoteClick(Note note) {
        // Do Nothing
    }

    public boolean handleNoteLongClick(Note note) {
        return true;
    }
}
