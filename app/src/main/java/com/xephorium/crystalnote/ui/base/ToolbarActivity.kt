package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.databinding.ToolbarActivityLayoutBinding

@SuppressLint("Registered")
open class ToolbarActivity : BaseActivity() {


    /*--- Variable Declarations ---*/

    protected lateinit var toolbarBinding: ToolbarActivityLayoutBinding


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarBinding = ToolbarActivityLayoutBinding.inflate(layoutInflater)
        setContentView(toolbarBinding.root)

        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    /*--- Public Methods ---*/

    fun setBoundViewAsContent(boundView: ViewBinding) {
        toolbarBinding.layoutActivityContent.addView(boundView.root)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
