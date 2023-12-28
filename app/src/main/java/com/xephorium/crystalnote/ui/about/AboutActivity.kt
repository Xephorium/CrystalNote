package com.xephorium.crystalnote.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.databinding.AboutActivityLayoutBinding
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity


class AboutActivity : DrawerActivity(), AboutContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var binding: AboutActivityLayoutBinding

    private lateinit var presenter: AboutPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AboutActivityLayoutBinding.inflate(layoutInflater)
        setBoundViewAsContent(binding)

        presenter = AboutPresenter()

        setupToolbar()
        setupClickListeners()
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

    override fun navigateToDeveloperSite() {
        val url = "https://xephorium.github.io/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    override fun navigateToPrivacyPolicy() {
        val url = "https://raw.githubusercontent.com/Xephorium/CrystalNote/master/docs/PrivacyPolicy.txt"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        drawerBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.aboutTitle)
            setLeftButtonImage(R.drawable.icon_menu)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupClickListeners() {
        binding.buttonAboutDeveloper.setOnClickListener {
            presenter.handleDeveloperButtonClick()
        }
        binding.buttonAboutPrivacyPolicy.setOnClickListener {
            presenter.handlePrivacyPolicyButtonClick()
        }
    }

}
