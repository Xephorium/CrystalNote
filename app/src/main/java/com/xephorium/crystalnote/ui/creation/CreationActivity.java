package com.xephorium.crystalnote.ui.creation;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.BaseActivity;
import com.xephorium.crystalnote.ui.custom.LineEditText;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreationActivity extends BaseActivity implements CreationView {

    @BindView(R.id.creation_toolbar)
    NoteToolbar toolbar;

    @BindView(R.id.creation_content)
    LineEditText content;

    CreationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity_layout);
        ButterKnife.bind(this);

        presenter = new CreationPresenter(this);
        toolbar.setEditMode(true);
        toolbar.setLeftButtonImage(R.drawable.icon_back);
        toolbar.setRightButtonImage(R.drawable.icon_save);
        toolbar.setNoteToolbarListener(getCreationNoteToolbarListener());
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
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        presenter.handleBackClick();
    }

    @Override
    public void closeCreationActivity() {
        this.finish();
    }

    @Override
    public boolean isNewNotePopulated() {
        return !TextUtils.isEmpty(toolbar.getTitleContent()) || !TextUtils.isEmpty(content.getText());
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                presenter.handleBackClick();
            }

            @Override
            public void onRightButtonClick() {
                presenter.handleSaveClick(toolbar.getTitleContent(), content.getText().toString());
            }
        };
    }
}
