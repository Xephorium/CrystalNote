package com.xephorium.crystalnote.ui.custom;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.util.NoteUtils;
import com.xephorium.crystalnote.data.model.Note;

import java.util.List;

/*
  NoteListAdapter.java                                                      04.11.2017
  Christopher Cruzen

    Provides view behavior for each element of the NoteList and a default onClickListener.
  Per implementation, getOnClickListener should be overridden for desired behavior.

*/

public class NoteListAdapter extends BaseAdapter {

    private List<Note> notes;

    public NoteListAdapter(List<Note> noteList) {
        super();
        this.notes = noteList;
    }

    public NoteListAdapter() {
        super();
        this.notes = null;
    }

    @Override
    public View getView(final int position, View noteLayout, ViewGroup parent) {
        if (noteLayout == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            noteLayout = inflater.inflate(R.layout.note_layout, parent, false);

            TextView noteTitle = (TextView) noteLayout.findViewById(R.id.note_title);
            TextView notePreview = (TextView) noteLayout.findViewById(R.id.note_preview);
            TextView noteDate = (TextView) noteLayout.findViewById(R.id.note_date);
            ImageView noteIcon = (ImageView) noteLayout.findViewById(R.id.note_icon);

            noteTitle.setText(notes.get(position).getName());
            notePreview.setText(notes.get(position).getPreview());
            noteDate.setText(NoteUtils.getFormattedDate(notes.get(position)));
            noteIcon.setColorFilter(notes.get(position).getColor(), PorterDuff.Mode.SRC_ATOP);

            noteLayout.setOnClickListener(getOnClickListener(position));
            noteLayout.setOnLongClickListener(getOnLongClickListener(position));
        }

        return noteLayout;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public int getCount() {
        return notes != null ? notes.size() : 0;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public View.OnClickListener getOnClickListener(int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Default Behavior; Do Nothing
            }
        };
    }

    public View.OnLongClickListener getOnLongClickListener(int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Default Behavior; Do Nothing
                return true;
            }
        };
    }
}