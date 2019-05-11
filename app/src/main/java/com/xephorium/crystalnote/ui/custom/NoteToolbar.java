package com.xephorium.crystalnote.ui.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.xephorium.crystalnote.R;

public class NoteToolbar extends Toolbar {

    private static final int DEFAULT_LEFT_BUTTON_IMAGE = R.drawable.icon_back;
    private static final int DEFAULT_RIGHT_BUTTON_IMAGE = R.drawable.icon_save;
    public static final int NO_IMAGE = -1;

    private boolean editMode;
    private NoteToolbarListener noteToolbarListener;

    public NoteToolbar(Context context) {
        super(context);
        buildNoteToolbar(context);
    }

    public NoteToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        buildNoteToolbar(context);
    }

    public NoteToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildNoteToolbar(context);
    }

    private void buildNoteToolbar(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View noteToolbarLayout = layoutInflater.inflate(R.layout.note_toolbar_layout, null);
        this.addView(noteToolbarLayout);

        this.setEditMode(false);
        this.setLeftButtonImage(DEFAULT_LEFT_BUTTON_IMAGE);
        this.setRightButtonImage(NO_IMAGE);
        this.noteToolbarListener = getDefaultNoteToolbarListener();

        findViewById(R.id.left_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                noteToolbarListener.onLeftButtonClick();
            }
        });
        findViewById(R.id.right_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                noteToolbarListener.onRightButtonClick();
            }
        });
        ((EditText) findViewById(R.id.edit_text)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                noteToolbarListener.onTextChange(
                        ((EditText) findViewById(R.id.edit_text)).getText().toString());
            }
        });
    }

    @Override
    public void setTitle(int stringResource) {
        getTitleView().setText(getResources().getText(stringResource));
    }

    @Override
    public CharSequence getTitle() {
        return getTitleView().getText();
    }

    public void setTitleContent(String content) {
        getEditView().setText(content);
    }

    public String getTitleContent() {
        return getEditView().getText() != null ? getEditView().getText().toString() : "";
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
            public void onLeftButtonClick() {
            }

            @Override
            public void onRightButtonClick() {
            }

            @Override
            public void onTextChange(String text) {
            }
        };
    }

    public interface NoteToolbarListener {
        void onLeftButtonClick();

        void onRightButtonClick();

        void onTextChange(String text);
    }
}
