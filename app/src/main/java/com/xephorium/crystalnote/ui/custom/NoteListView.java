package com.xephorium.crystalnote.ui.custom;

/*
  NoteListView.java                                                             04.28.2017
  Christopher Cruzen

    Encapsulating the view behavior of a SwipeRefreshLayout, ScrollView, and two ListViews,
  this class represents a collection of notes organized by reverse chronological order.
  The first ListView is populated by notes modified today and the second is populated by
  notes modified before today.

*/

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.xephorium.crystalnote.R;

public class NoteListView extends SwipeRefreshLayout {

    public NoteListView(Context context) {
        super(context, null);
    }

    public NoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void buildNoteListView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View noteListViewLayout = layoutInflater.inflate(R.layout.note_list_view_layout, null);
        this.addView(noteListViewLayout);
    }

}
