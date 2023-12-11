package com.xephorium.crystalnote.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.databinding.ToolbarActivityLayoutBinding

@SuppressLint("Registered")
open class ToolbarActivity : BaseActivity() {


    /*--- Variable Declarations ---*/

    private lateinit var binding: ToolbarActivityLayoutBinding


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ToolbarActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
