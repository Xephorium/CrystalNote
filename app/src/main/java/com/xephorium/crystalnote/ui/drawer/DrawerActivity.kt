package com.xephorium.crystalnote.ui.drawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.databinding.DrawerActivityLayoutBinding
import com.xephorium.crystalnote.ui.about.AboutActivity
import com.xephorium.crystalnote.ui.base.BaseActivity
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerButton.*
import com.xephorium.crystalnote.ui.drawer.DrawerItem.Companion.DrawerItemType.*
import com.xephorium.crystalnote.ui.extensions.getThemeColor
import com.xephorium.crystalnote.ui.home.HomeActivity
import com.xephorium.crystalnote.ui.settings.SettingsActivity
import com.xephorium.crystalnote.ui.utility.DisplayUtility
import com.xephorium.crystalnote.ui.widget.WidgetActivity


@SuppressLint("Registered")
open class DrawerActivity : BaseActivity(), DrawerContract.View {


    /*--- Variable Declarations ---*/

    protected lateinit var drawerBinding: DrawerActivityLayoutBinding

    private lateinit var presenter: DrawerPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var drawerAnimating: Boolean = false
    var drawerOpen: Boolean = false
        private set


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerBinding = DrawerActivityLayoutBinding.inflate(layoutInflater)
        setContentView(drawerBinding.root)

        presenter = DrawerPresenter()
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)

        setupViewFields()
        setupToolbar()
        setupNavDrawerItems()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerAnimating)
            return

        if (drawerOpen) {
            drawerBinding.layoutDrawer.closeDrawers()
            drawerAnimating = true
        } else {
            presenter.handleBackClick()
        }
    }


    /*--- Child Activity Methods ---*/

    fun setActivityContent(layoutResource: Int) {
        val view = layoutInflater.inflate(layoutResource, null)
        view.layoutParams = ViewGroup.LayoutParams(
            drawerBinding.layoutActivityContent.width,
            getActivityContentHeight()
        )
        drawerBinding.layoutActivityContent.addView(view)
    }

    fun setBoundViewAsContent(boundView: ViewBinding) {
        boundView.root.layoutParams = ViewGroup.LayoutParams(
            drawerBinding.layoutActivityContent.width,
            getActivityContentHeight()
        )
        drawerBinding.layoutActivityContent.addView(boundView.root)
    }

    fun openDrawer() {
        drawerBinding.layoutDrawer.openDrawer(GravityCompat.START)
        drawerAnimating = true
    }


    /*--- View Manipulation Methods ---*/

    override fun closeDrawer() {
        drawerBinding.layoutDrawer.closeDrawers()
        drawerAnimating = true
    }

    override fun closeDrawerAfterDelay() {
        Handler().postDelayed({
            closeDrawer()
        }, DRAWER_UI_UPDATE_DELAY)
    }

    override fun navigateToHome() {
        navigateToActivity(Intent(this@DrawerActivity, HomeActivity::class.java))
    }

    override fun navigateToHomeImmediately() {
        navigateToActivityImmediately(Intent(this@DrawerActivity, HomeActivity::class.java))
    }

    override fun navigateToWidget() {
        navigateToActivity(Intent(this@DrawerActivity, WidgetActivity::class.java))
    }

    override fun navigateToSettings() {
        navigateToActivity(Intent(this@DrawerActivity, SettingsActivity::class.java))
    }

    override fun navigateToAbout() {
        navigateToActivity(Intent(this@DrawerActivity, AboutActivity::class.java))
    }

    override fun closeCrystalNote() {
        super.onBackPressed()
    }

    override fun setSelectedMenuButton(button: DrawerButton) {
        val listDrawer = drawerBinding.layoutDrawer.findViewById<RecyclerView>(R.id.listDrawer)
        (listDrawer.adapter as? DrawerAdapter)?.items?.forEachIndexed { index, item ->
            if (item.type == BUTTON) {
                val holder = listDrawer.findViewHolderForLayoutPosition(index) as? DrawerAdapter.ViewHolder
                holder?.run {
                    val context = this@DrawerActivity as Context
                    if (item.text == button.displayName) {
                        val itemColor = context.getThemeColor(R.attr.themeDrawerItemSelected)
                        text.setTextColor(itemColor)
                        icon.setColorFilter(itemColor, PorterDuff.Mode.SRC_IN)
                        layout.background = ContextCompat.getDrawable(
                            context,
                            R.drawable.drawer_selection_background
                        )

                    } else {
                        val itemColor = context.getThemeColor(R.attr.themeDrawerItem)
                        text.setTextColor(itemColor)
                        icon.setColorFilter(itemColor, PorterDuff.Mode.SRC_IN)
                        layout.background = null
                    }
                }
            }
        }
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        setSupportActionBar(drawerBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewFields() {
        drawerOpen = false
        drawerAnimating = false

        drawerToggle = getActionBarDrawerToggle()
        drawerToggle.isDrawerIndicatorEnabled = false
        drawerBinding.layoutDrawer.addDrawerListener(drawerToggle)
    }

    private fun setupNavDrawerItems() {
        val listDrawer = drawerBinding.layoutDrawer.findViewById<RecyclerView>(R.id.listDrawer)
        listDrawer.layoutManager = LinearLayoutManager(this)
        listDrawer.adapter = DrawerAdapter(this, getItems())
    }

    private fun getActionBarDrawerToggle() = object : ActionBarDrawerToggle(
            this,
            drawerBinding.layoutDrawer,
            R.string.navigationDrawerOpened,
            R.string.navigationDrawerClosed) {

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
            DrawerItem(R.drawable.icon_note, NOTES.displayName, object : DrawerItem.ClickListener {
                override fun onClick() = presenter.handleHomeClick()
            }, BUTTON
            ),
            DrawerItem(R.drawable.icon_sliders, SETTINGS.displayName, object : DrawerItem.ClickListener {
                override fun onClick() = presenter.handleSettingsClick()
            }, BUTTON
            ),
            DrawerItem(R.drawable.icon_widget, WIDGET.displayName, object : DrawerItem.ClickListener {
                override fun onClick() = presenter.handleWidgetClick()
            }, BUTTON
            ),
            DrawerItem(0, "", object : DrawerItem.ClickListener {
                override fun onClick() = Unit
            }, DIVIDER
            ),
            DrawerItem(R.drawable.icon_about, ABOUT.displayName, object : DrawerItem.ClickListener {
                override fun onClick() = presenter.handleAboutClick()
            }, BUTTON
            )
    )

    private fun navigateToActivity(intent: Intent) {
        Handler().postDelayed({
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }, DRAWER_UI_UPDATE_DELAY + DRAWER_CLOSE_TIME)
    }

    private fun navigateToActivityImmediately(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun getActivityContentHeight(): Int {
        val displayHeight = DisplayUtility.getDisplayHeight(this)
        val statusBarHeight = DisplayUtility.getStatusBarHeight(this)
        val toolbarHeight = resources.getDimensionPixelSize(R.dimen.toolbarHeight)
        return displayHeight - (statusBarHeight + toolbarHeight)
    }


    /*--- Constants ---*/

    companion object {
        private const val DRAWER_CLOSE_TIME: Long = 290
        private const val DRAWER_UI_UPDATE_DELAY: Long = 10
    }
}
