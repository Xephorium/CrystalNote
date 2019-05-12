package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.utility.DisplayUtils
import kotlinx.android.synthetic.main.navigation_activity_layout.*

@SuppressLint("Registered")
open class DrawerActivity : BaseActivity() {


    /*--- Variable Declarations ---*/

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var drawerOpen: Boolean = false
    private var drawerAnimating: Boolean = false


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity_layout)

        drawerOpen = false
        drawerAnimating = false

        drawerToggle = getActionBarDrawerToggle()
        drawerToggle.isDrawerIndicatorEnabled = false
        drawer_layout.setDrawerListener(drawerToggle)

        setupStatusBar()
        setupToolbar()
        setupNavDrawer()
    }

    override fun onBackPressed() {
        if (drawerAnimating)
            return

        if (drawerOpen) {
            drawer_layout.closeDrawers()
            drawerAnimating = true
        } else
            super.onBackPressed()
    }


    /*--- Public Methods ---*/

    fun setActivityContent(layoutResource: Int) {
        activity_content.addView(getInflatedView(layoutResource))
    }


    private fun setDrawerContent() {
        // Do Nothing
    }

    fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
        drawerAnimating = true
    }

    fun closeDrawer() {
        drawer_layout.closeDrawers()
        drawerAnimating = true
    }


    /*--- Private Utility Methods ---*/

    private fun setupStatusBar() {
        status_bar_margin.minimumHeight = DisplayUtils.getStatusBarHeight(this)
        status_bar_margin.elevation = resources.getDimension(R.dimen.toolbar_elevation)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setupNavDrawer() {
        drawer_content.layoutParams.width = DisplayUtils.getDisplayWidth(this) - resources.getDimensionPixelSize(R.dimen.toolbar_height)
    }

    private fun getInflatedView(layoutResource: Int): View {
        val layoutInflater = layoutInflater
        return layoutInflater.inflate(layoutResource, null)
    }

    private fun getActionBarDrawerToggle() = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_closed) {

        override fun onDrawerClosed(drawerView: View) {
            super.onDrawerClosed(drawerView)
            drawerOpen = false
            drawerAnimating = false
        }

        override fun onDrawerOpened(drawerView: View) {
            super.onDrawerOpened(drawerView)
            drawerOpen = true
            drawerAnimating = false
        }
    }
}
