package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.utility.DisplayUtils
import kotlinx.android.synthetic.main.toolbar_activity_layout.*

@SuppressLint("Registered")
open class ToolbarActivity : BaseActivity() {


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_activity_layout)

        setupStatusBar()
        setupToolbar()
    }


    /*--- Public Methods ---*/

    fun setActivityContent(layoutResource: Int) {
        layoutActivityContent.addView(getInflatedView(layoutResource))
    }


    /*--- Private Setup Methods ---*/

    private fun setupStatusBar() {
        layoutToolbarActivityStatusBar.minimumHeight = DisplayUtils.getStatusBarHeight(this)
        layoutToolbarActivityStatusBar.elevation = resources.getDimension(R.dimen.toolbarElevation)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun getInflatedView(layoutResource: Int): View {
        val layoutInflater = layoutInflater
        return layoutInflater.inflate(layoutResource, null)
    }
}
