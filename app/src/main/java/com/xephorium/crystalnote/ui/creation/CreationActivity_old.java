package com.xephorium.crystalnote.ui.creation;

import android.os.Bundle;
import android.text.TextUtils;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.ToolbarActivity;
import com.xephorium.crystalnote.ui.custom.LineEditText;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

public class CreationActivity_old extends ToolbarActivity implements CreationView {

    //@BindView(R.id.creation_content)
    LineEditText content;

    CreationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.creation_activity_layout);
        //ButterKnife.bind(this);

        presenter = new CreationPresenter(this);
        setupCreationToolbar();
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
    public void onBackPressed() {
        presenter.handleBackClick();
    }

    @Override
    public void closeCreationActivity() {
        this.finish();
    }

    @Override
    public boolean isNewNotePopulated() {
        return !TextUtils.isEmpty(getToolbar().getTitleContent()) || !TextUtils.isEmpty(content.getText());
    }

    private void setupCreationToolbar() {
        getToolbar().setEditMode(true);
        getToolbar().setLeftButtonImage(R.drawable.icon_back);
        getToolbar().setRightButtonImage(R.drawable.icon_save);
        getToolbar().setNoteToolbarListener(getCreationNoteToolbarListener());
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                presenter.handleBackClick();
            }

            @Override
            public void onRightButtonClick() {
                presenter.handleSaveClick(getToolbar().getTitleContent(), content.getText().toString());
            }
        };
    }
}
