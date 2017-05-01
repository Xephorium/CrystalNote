package com.xephorium.crystalnote.ui.home;

import android.os.Bundle;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.NavigationActivity;
import com.xephorium.crystalnote.ui.custom.NoteListView;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends NavigationActivity implements HomeView {

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
        setActivityContent(R.layout.home_activity_layout);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);
        noteListView.setNoteListViewListener(getHomeNoteListViewListener());
        setupHomeToolbar();
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

    private void setupHomeToolbar() {
        getToolbar().setEditMode(false);
        getToolbar().setTitle(R.string.home_title);
        getToolbar().setLeftButtonImage(R.drawable.icon_menu);
        getToolbar().setRightButtonImage(R.drawable.icon_search);
        getToolbar().setNoteToolbarListener(getCreationNoteToolbarListener());
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                openDrawer();
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
                presenter.refreshNoteList();
            }
        };
    }
}
