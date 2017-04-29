package com.xephorium.crystalnote.ui.creation;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.BaseActivity;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreationActivity extends BaseActivity implements CreationView {

    @BindView(R.id.creation_toolbar)
    NoteToolbar toolbar;

    CreationPresenter presenter;

//    @OnClick(R.id.creation_toolbar_save)
//    public void handleToolbarSaveClick() {
//        presenter.handleSaveClick(toolbarText.getText().toString(), contentText.getText().toString());
//    }

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
        //return !TextUtils.isEmpty(toolbarText.getText()) || !TextUtils.isEmpty(contentText.getText());
        return false;
    }

    private NoteToolbar.NoteToolbarListener getCreationNoteToolbarListener() {
        return new NoteToolbar.NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {
                // presenter.handleBackClick(); // Calling Containing Class Methods From Here Causes UI Skipping?
                //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                //presenter.handleBackClick();
            }

            @Override
            public void onRightButtonClick() {

            }
        };
    }
}
