package com.xephorium.crystalnote.ui.selection;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.data.model.Note;
import com.xephorium.crystalnote.ui.base.BaseActivity;
import com.xephorium.crystalnote.ui.custom.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectionActivity extends BaseActivity implements SelectionView {

    @BindView(R.id.selection_toolbar)
    Toolbar selectionToolbar;

    @BindView(R.id.new_note_header)
    TextView newNoteHeader;

    @BindView(R.id.new_note_list_view)
    ListView newNoteListView;

    @BindView(R.id.old_note_header)
    TextView oldNoteHeader;

    @BindView(R.id.old_note_list_view)
    ListView oldNoteListView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    SelectionPresenter presenter;

    @OnClick(R.id.selection_toolbar_back)
    public void handleToolbarBackClick() {
        presenter.handleToolbarBackClick();
    }

    @OnClick(R.id.selection_toolbar_search)
    public void handleToolbarSearchClick() {
        Toast.makeText(this, "Search Notes", Toast.LENGTH_SHORT).show();
    }

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
        swipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());
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
        return selectionToolbar;
    }

    @Override
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

    @Override
    public void closeSelectionActivity() {
        this.finish();
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshNoteList();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    public NoteListAdapter getNoteListAdapter(final List<Note> notes) {
        return new NoteListAdapter(notes) {
            @Override
            public View.OnClickListener getOnClickListener(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.handleNoteClick(notes.get(position));
                    }
                };
            }

            @Override
            public View.OnLongClickListener getOnLongClickListener(final int position) {
                return new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return presenter.handleNoteLongClick(notes.get(position));
                    }
                };
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
}
