package com.xephorium.crystalnote.ui.selection;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.BaseActivity;
import com.xephorium.crystalnote.ui.custom.NoteListView;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectionActivity extends BaseActivity implements SelectionView {

    @BindView(R.id.selection_toolbar)
    NoteToolbar toolbar;

    @BindView(R.id.note_list_view)
    NoteListView noteListView;

    SelectionPresenter presenter;

    @OnClick(R.id.selection_action_button)
    public void handleActionButtonClick() {
        presenter.handleActionButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_activity_layout);
        ButterKnife.bind(this);

        presenter = new SelectionPresenter(this);
        noteListView.setNoteListViewListener(getSelectionNoteListViewListener());
        toolbar.setNoteToolbarListener(getSelectionNoteToolbarListener());
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
    public Toolbar getToolbar() {
        toolbar.setEditMode(false);
        toolbar.setTitle(R.string.selection_title);
        toolbar.setLeftButtonImage(R.drawable.icon_back);
        toolbar.setRightButtonImage(R.drawable.icon_search);
        return toolbar;
    }

    @Override
    public void populateNoteList(List<Note> newNotes, List<Note> oldNotes) {
        noteListView.populateNoteList(newNotes, oldNotes);
    }

    @Override
    public void closeSelectionActivity() {
        this.finish();
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
            public void onNoteLongClick(Note note) {
                presenter.handleNoteLongClick(note);
            }

            @Override
            public void onNoteListRefresh() {
                presenter.refreshNoteList();
            }
        };
    }
}
