package com.xephorium.crystalnote.ui.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.BaseActivity;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeView {

    @BindView(R.id.home_toolbar)
    NoteToolbar toolbar;

    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setEditMode(false);
        toolbar.setTitle(R.string.home_title);
        toolbar.setLeftButtonImage(R.drawable.icon_menu);
        toolbar.setRightButtonImage(R.drawable.icon_search);
        toolbar.setNoteToolbarListener(getCreationNoteToolbarListener());
        return toolbar;
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                // Do Nothing
            }

            @Override
            public void onRightButtonClick() {
                // Do Nothing
            }
        };
    }
}
