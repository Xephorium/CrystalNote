package com.xephorium.crystalnote.ui.about

import android.os.Bundle

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.databinding.AboutActivityLayoutBinding
import com.xephorium.crystalnote.databinding.ToolbarActivityLayoutBinding
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity

class AboutActivity : DrawerActivity(), AboutContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var presenter: AboutPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBoundViewAsContent(AboutActivityLayoutBinding.inflate(layoutInflater))

        presenter = AboutPresenter()

        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


    /*--- View Manipulation Methods ---*/

    override fun showNavigationDrawer() {
        openDrawer()
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        drawerBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.aboutTitle)
            setLeftButtonImage(R.drawable.icon_menu)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onButtonClick() = presenter.handleMenuButtonClick()
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

}
