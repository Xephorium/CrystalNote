package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager

import com.xephorium.crystalnote.R
import kotlinx.android.synthetic.main.toolbar_activity_layout.*

@SuppressLint("Registered")
open class ToolbarActivity : BaseActivity() {


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar_activity_layout)

        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    /*--- Public Methods ---*/

    fun setActivityContent(layoutResource: Int) {
        layoutInflater.inflate(layoutResource, findViewById(R.id.layoutActivityContent))
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
