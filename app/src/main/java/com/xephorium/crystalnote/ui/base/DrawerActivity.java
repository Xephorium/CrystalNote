package com.xephorium.crystalnote.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.xephorium.crystalnote.R;
import com.xephorium.crystalnote.ui.custom.NoteToolbar;
import com.xephorium.crystalnote.ui.util.DisplayUtils;

public class DrawerActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout statusBarMargin;
    private NoteToolbar toolbar;
    private LinearLayout activityContent;
    private NavigationView drawerContent;
    private boolean drawerOpen;
    private boolean drawerAnimating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        statusBarMargin = (LinearLayout) findViewById(R.id.status_bar_margin);
        toolbar = (NoteToolbar) findViewById(R.id.toolbar);
        activityContent = (LinearLayout) findViewById(R.id.activity_content);
        drawerContent = (NavigationView) findViewById(R.id.drawer_content);
        drawerOpen = false;
        drawerAnimating = false;

        drawerToggle = getActionBarDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerListener(drawerToggle);

        setupStatusBar();
        setupToolbar();
        setupNavDrawer();
    }

    public void setActivityContent(int layoutResource) {
        activityContent.addView(getInflatedView(layoutResource));
    }

    @Override
    public void onBackPressed() {
        if (drawerAnimating)
            return;

        if (drawerOpen) {
            drawerLayout.closeDrawers();
            drawerAnimating = true;
        } else
            super.onBackPressed();
    }

    private void setDrawerContent() {
        // Do Nothing
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
        drawerAnimating = true;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
        drawerAnimating = true;
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

    private void setupNavDrawer() {
        drawerContent.getLayoutParams().width = DisplayUtils.getDisplayWidth(this) -
                getResources().getDimensionPixelSize(R.dimen.toolbar_height);
    }

    private View getInflatedView(int layoutResource) {
        LayoutInflater layoutInflater = getLayoutInflater();
        return layoutInflater.inflate(layoutResource, null);
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle() {
        return new ActionBarDrawerToggle(
                this,
                this.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_closed) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                drawerOpen = false;
                drawerAnimating = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerOpen = true;
                drawerAnimating = false;
            }
        };
    }
}
