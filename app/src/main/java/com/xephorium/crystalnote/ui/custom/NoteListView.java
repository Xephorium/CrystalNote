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
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListView extends SwipeRefreshLayout {

    @BindView(R.id.new_note_header)
    TextView newNoteHeader;

    @BindView(R.id.new_note_list_view)
    ListView newNoteListView;

    @BindView(R.id.old_note_header)
    TextView oldNoteHeader;

    @BindView(R.id.old_note_list_view)
    ListView oldNoteListView;

    private NoteListViewListener noteListViewListener;

    public NoteListView(Context context) {
        super(context, null);
        buildNoteListView(context);
        ButterKnife.bind(this);
    }

    public NoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        buildNoteListView(context);
        ButterKnife.bind(this);
    }

    private void buildNoteListView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View noteListViewLayout = layoutInflater.inflate(R.layout.note_list_view_layout, null);
        this.addView(noteListViewLayout);

        this.setNoteListViewListener(getDefaultNoteListViewListener());
        this.setOnRefreshListener(getOnRefreshListener());
    }

    public void populateNoteList(List<Note> newNotes, List<Note> oldNotes) {
        if (newNotes.size() > 0 && oldNotes.size() > 0) {
            newNoteHeader.setVisibility(View.VISIBLE);
            newNoteListView.setVisibility(View.VISIBLE);
            newNoteListView.setAdapter(getNoteListAdapter(newNotes));
            oldNoteHeader.setVisibility(View.VISIBLE);
            oldNoteListView.setVisibility(View.VISIBLE);
            oldNoteListView.setAdapter(getNoteListAdapter(oldNotes));
        } else if (newNotes.size() > 0) {
            newNoteHeader.setVisibility(View.GONE);
            newNoteListView.setVisibility(View.VISIBLE);
            newNoteListView.setAdapter(getNoteListAdapter(newNotes));
            oldNoteHeader.setVisibility(View.GONE);
            oldNoteListView.setVisibility(View.GONE);
            oldNoteListView.setAdapter(getNoteListAdapter(new ArrayList<Note>()));
        } else if (oldNotes.size() > 0) {
            newNoteHeader.setVisibility(View.GONE);
            newNoteListView.setVisibility(View.GONE);
            newNoteListView.setAdapter(getNoteListAdapter(new ArrayList<Note>()));
            oldNoteHeader.setVisibility(View.GONE);
            oldNoteListView.setVisibility(View.VISIBLE);
            oldNoteListView.setAdapter(getNoteListAdapter(oldNotes));
        } else {
            newNoteHeader.setVisibility(View.GONE);
            newNoteListView.setVisibility(View.GONE);
            newNoteListView.setAdapter(getNoteListAdapter(new ArrayList<Note>()));
            oldNoteHeader.setVisibility(View.GONE);
            oldNoteListView.setVisibility(View.GONE);
            oldNoteListView.setAdapter(getNoteListAdapter(new ArrayList<Note>()));
        }
        newNoteListView.setLayoutParams(getScrollViewHeightParams(newNoteListView));
        oldNoteListView.setLayoutParams(getScrollViewHeightParams(oldNoteListView));
    }

    public void setNoteListViewListener(NoteListViewListener noteListViewListener) {
        this.noteListViewListener = noteListViewListener;
    }

    public NoteListViewListener getNoteListViewListener() {
        return this.noteListViewListener;
    }

    private NoteListViewListener getDefaultNoteListViewListener() {
        return new NoteListViewListener() {
            @Override
            public void onNoteClick(Note note) {
                // Default Behavior; Do Nothing
            }

            @Override
            public void onNoteLongClick(Note note) {
                // Default Behavior; Do Nothing
            }

            @Override
            public void onNoteListRefresh() {
                // Default Behavior; Do Nothing
            }
        };
    }

    private NoteListAdapter getNoteListAdapter(final List<Note> notes) {
        return new NoteListAdapter(notes) {
            @Override
            public View.OnClickListener getOnClickListener(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteListViewListener.onNoteClick(notes.get(position));
                    }
                };
            }

            @Override
            public View.OnLongClickListener getOnLongClickListener(final int position) {
                return new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        noteListViewListener.onNoteLongClick(notes.get(position));
                        return true;
                    }
                };
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noteListViewListener.onNoteListRefresh();
                setRefreshing(false);
            }
        };
    }

    private ViewGroup.LayoutParams getScrollViewHeightParams(ListView listView) {
        int listViewHeight = 0;
        for (int x = 0; x < listView.getAdapter().getCount(); x++) {
            View listItem = listView.getAdapter().getView(x, null, listView);
            listItem.measure(0, 0);
            listViewHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listViewHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
        return params;
    }

    public interface NoteListViewListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
        void onNoteListRefresh();
    }

}
