package com.xephorium.crystalnote.ui.home;

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

public class HomeActivity extends BaseActivity implements HomeView {

    @BindView(R.id.home_toolbar)
    NoteToolbar toolbar;

    @BindView(R.id.home_note_list)
    NoteListView noteListView;

    HomePresenter presenter;

    @OnClick(R.id.home_action_button)
    public void handleActionButtonClick() {
        presenter.handleActionButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);
        noteListView.setNoteListViewListener(getHomeNoteListViewListener());
        toolbar.setNoteToolbarListener(getCreationNoteToolbarListener());
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
    public Toolbar getToolbar() {
        toolbar.setEditMode(false);
        toolbar.setTitle(R.string.home_title);
        toolbar.setLeftButtonImage(R.drawable.icon_menu);
        toolbar.setRightButtonImage(R.drawable.icon_search);
        return toolbar;
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                // Show Navigation Drawer
            }

            @Override
            public void onRightButtonClick() {
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private NoteListView.NoteListViewListener getHomeNoteListViewListener() {
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
                //presenter.refreshNoteList();
            }
        };
    }
}
