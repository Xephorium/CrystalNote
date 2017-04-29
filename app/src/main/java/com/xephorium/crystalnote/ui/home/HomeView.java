package com.xephorium.crystalnote.ui.home;

import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.BaseView;

import java.util.List;

public interface HomeView extends BaseView {
    void populateNoteList(List<Note> note);
}
