package com.xephorium.crystalnote.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;
import com.xephorium.crystalnote.ui.util.DisplayUtils;

public class ToolbarActivity extends BaseActivity {

    private LinearLayout statusBarMargin;
    private NoteToolbar toolbar;
    private LinearLayout activityContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_activity_layout);

        statusBarMargin = findViewById(R.id.status_bar_margin);
        toolbar = findViewById(R.id.toolbar);
        activityContent = findViewById(R.id.activity_content);

        setupStatusBar();
        setupToolbar();
    }

    public void setActivityContent(int layoutResource) {
        activityContent.addView(getInflatedView(layoutResource));
    }

    public NoteToolbar getToolbar() {
        return toolbar;
    }

    private void setupStatusBar() {
        statusBarMargin.setMinimumHeight(DisplayUtils.getStatusBarHeight(this));
        statusBarMargin.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private View getInflatedView(int layoutResource) {
        LayoutInflater layoutInflater = getLayoutInflater();
        return layoutInflater.inflate(layoutResource, null);
    }
}
