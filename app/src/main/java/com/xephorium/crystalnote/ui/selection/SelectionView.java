package com.xephorium.crystalnote.ui.selection;

import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.BaseView;

import java.util.List;

public interface SelectionView extends BaseView {
    void populateNoteList(List<Note> notes);

    void closeSelectionActivity();
}
