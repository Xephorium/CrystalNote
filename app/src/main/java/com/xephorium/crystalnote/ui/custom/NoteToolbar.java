package com.xephorium.crystalnote.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.base.BaseView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteToolbar extends Toolbar {

    private static final int DEFAULT_LEFT_BUTTON_IMAGE = R.drawable.icon_back;
    private static final int DEFAULT_RIGHT_BUTTON_IMAGE = R.drawable.icon_save;
    public static final int NO_IMAGE = -1;

    private boolean editMode;
    private NoteToolbarListener noteToolbarListener;

    public NoteToolbar(Context context) {
        super(context);
        buildNoteToolbar(context);
        ButterKnife.bind(this);
    }

    public NoteToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        buildNoteToolbar(context);
        ButterKnife.bind(this);
    }

    public NoteToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildNoteToolbar(context);
        ButterKnife.bind(this);
    }

    private void buildNoteToolbar(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View noteToolbarLayout = layoutInflater.inflate(R.layout.note_toolbar_layout, null);
        this.addView(noteToolbarLayout);

        this.setEditMode(false);
        this.setLeftButtonImage(DEFAULT_LEFT_BUTTON_IMAGE);
        this.setRightButtonImage(NO_IMAGE);
        this.noteToolbarListener = getDefaultNoteToolbarListener();
    }

    @OnClick(R.id.left_button)
    public void onLeftButtonClick() {
        noteToolbarListener.onLeftButtonClick();
    }

    @OnClick(R.id.right_button)
    public void onRightButtonClick() {
        noteToolbarListener.onRightButtonClick();
    }

    @Override
    public void setTitle(int stringResource) {
        getTitleView().setText(getResources().getText(stringResource));
    }

    @Override
    public CharSequence getTitle() {
        return getTitleView().getText();
    }

    public void setEditMode(boolean editMode) {
        if (editMode) {
            getEditView().setVisibility(VISIBLE);
            getTitleView().setVisibility(GONE);
        } else {
            getEditView().setVisibility(GONE);
            getTitleView().setVisibility(VISIBLE);
        }
    }

    public boolean isEditMode() {
        return this.editMode;
    }

    public void setLeftButtonImage(int drawable) {
        if (drawable == NO_IMAGE) {
            getLeftButtonView().setVisibility(GONE);
        } else {
            getLeftButtonView().setVisibility(VISIBLE);
            getLeftButtonView().setImageDrawable(getResources().getDrawable(drawable, getContext().getTheme()));
        }
    }

    public void setRightButtonImage(int drawable) {
        if (drawable == NO_IMAGE) {
            getRightButtonView().setVisibility(GONE);
        } else {
            getRightButtonView().setVisibility(VISIBLE);
            getRightButtonView().setImageDrawable(getResources().getDrawable(drawable, getContext().getTheme()));
        }
    }

    public void setNoteToolbarListener(NoteToolbarListener noteToolbarListener) {
        this.noteToolbarListener = noteToolbarListener;
    }

    private ImageView getLeftButtonView() {
        return ((ImageView) ((RelativeLayout) getChildAt(0)).getChildAt(0));
    }

    private ImageView getRightButtonView() {
        return ((ImageView) ((RelativeLayout) getChildAt(0)).getChildAt(1));
    }

    private TextView getTitleView() {
        return ((TextView) ((RelativeLayout) getChildAt(0)).getChildAt(3));
    }

    private EditText getEditView() {
        return ((EditText) ((RelativeLayout) getChildAt(0)).getChildAt(2));
    }

    private NoteToolbarListener getDefaultNoteToolbarListener() {
        return new NoteToolbarListener() {
            @Override
            public void onLeftButtonClick() {}

            @Override
            public void onRightButtonClick() {}
        };
    }

    public interface NoteToolbarListener {
        void onLeftButtonClick();
        void onRightButtonClick();
    }
}
