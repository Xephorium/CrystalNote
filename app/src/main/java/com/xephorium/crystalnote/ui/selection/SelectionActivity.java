package com.xephorium.crystalnote.ui.selection;

import android.os.Bundle;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.ToolbarActivity;
import com.xephorium.crystalnote.ui.custom.NoteListView;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectionActivity extends ToolbarActivity implements SelectionView {

    @BindView(R.id.selection_note_list)
    NoteListView noteListView;

    SelectionPresenter presenter;

    @OnClick(R.id.selection_action_button)
    public void handleActionButtonClick() {
        presenter.handleActionButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.selection_activity_layout);
        ButterKnife.bind(this);

        presenter = new SelectionPresenter(this);
        noteListView.setNoteListViewListener(getSelectionNoteListViewListener());
        setupSelectionToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.refreshNoteList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void populateNoteList(List<Note> notes) {
        noteListView.populateNoteList(notes);
    }

    @Override
    public void closeSelectionActivity() {
        this.finish();
    }

    private void setupSelectionToolbar() {
        getToolbar().setEditMode(false);
        getToolbar().setTitle(R.string.selection_title);
        getToolbar().setLeftButtonImage(R.drawable.icon_back);
        getToolbar().setRightButtonImage(R.drawable.icon_search);
        getToolbar().setNoteToolbarListener(getSelectionNoteToolbarListener());
    }

    private NoteToolbar.NoteToolbarListener getSelectionNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                presenter.handleToolbarBackClick();
            }

            @Override
            public void onRightButtonClick() {
                Toast.makeText(getApplicationContext(), "Search Notes", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private NoteListView.NoteListViewListener getSelectionNoteListViewListener() {
        return new NoteListView.NoteListViewListener() {
            @Override
            public void onNoteClick(Note note) {
                presenter.handleNoteClick(note);
            }

            @Override
            public boolean onNoteLongClick(Note note) {
                return presenter.handleNoteLongClick(note);
            }

            @Override
            public void onNoteListRefresh() {
                presenter.refreshNoteList();
            }
        };
    }
}
