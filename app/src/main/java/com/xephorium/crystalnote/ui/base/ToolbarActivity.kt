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
        activity_content.addView(getInflatedView(layoutResource))
    }


    /*--- Private Setup Methods ---*/

    private fun setupStatusBar() {
        status_bar_margin.minimumHeight = DisplayUtils.getStatusBarHeight(this)
        status_bar_margin.elevation = resources.getDimension(R.dimen.toolbar_elevation)
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
