package com.xephorium.crystalnote.ui.base.drawer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.utility.DisplayUtils
import com.xephorium.crystalnote.ui.base.drawer.DrawerItem.Companion.DrawerItemType.*
import com.xephorium.crystalnote.ui.home.HomeActivity
import kotlinx.android.synthetic.main.drawer_activity_layout.*
import kotlinx.android.synthetic.main.drawer_layout.*

@SuppressLint("Registered")
open class DrawerActivity : BaseActivity() {


    /*--- Variable Declarations ---*/

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var drawerOpen: Boolean = false
    private var drawerAnimating: Boolean = false


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_activity_layout)

        drawerOpen = false
        drawerAnimating = false

        drawerToggle = getActionBarDrawerToggle()
        drawerToggle.isDrawerIndicatorEnabled = false
        drawer_layout.setDrawerListener(drawerToggle)

        setupStatusBar()
        setupToolbar()
        setupNavDrawerItems()
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
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupNavDrawerItems() {
        list_drawer.layoutManager = LinearLayoutManager(this)
        list_drawer.adapter = DrawerAdapter(this, getItems())
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

    private fun getItems() = listOf(
            DrawerItem(R.drawable.icon_home, "Home",
                    object : DrawerItem.ClickListener {
                        override fun onClick() = navigateToActivity(
                                Intent(this@DrawerActivity, HomeActivity::class.java
                                ))
                    }, ITEM
            ),
            DrawerItem(R.drawable.icon_widget, "Widget",
                    object : DrawerItem.ClickListener {
                        override fun onClick() {
                            // TODO - Build  Widget Page
                            closeDrawerAfterDelay()
                        }
                    }, ITEM
            ),
            DrawerItem(R.drawable.icon_settings, "Settings",
                    object : DrawerItem.ClickListener {
                        override fun onClick() {
                            // TODO - Build  Settings Page
                            closeDrawerAfterDelay()
                        }
                    }, ITEM
            ),
            DrawerItem(0, "",
                    object : DrawerItem.ClickListener {
                        override fun onClick() = Unit
                    }, DIVIDER
            ),
            DrawerItem(R.drawable.icon_about, "About",
                    object : DrawerItem.ClickListener {
                        override fun onClick() {
                            // TODO - Build  About Page
                            closeDrawerAfterDelay()
                        }
                    }, ITEM
            )
    )

    private fun navigateToActivity(intent: Intent) {
        Handler().postDelayed({
            closeDrawer()
            Handler().postDelayed({
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                overridePendingTransition(0, 0);
            }, DRAWER_CLOSE_TIME)
        }, RIPPLE_DELAY)
    }

    private fun closeDrawerAfterDelay() {
        Handler().postDelayed({
            closeDrawer()
        }, RIPPLE_DELAY)
    }


    /*--- Constants ---*/

    companion object {
        private const val DRAWER_CLOSE_TIME: Long = 300
        private const val RIPPLE_DELAY: Long = 100
    }
}
