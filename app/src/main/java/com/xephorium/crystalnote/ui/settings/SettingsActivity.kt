package com.xephorium.crystalnote.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import kotlinx.android.synthetic.main.settings_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*
import android.widget.ArrayAdapter


class SettingsActivity : DrawerActivity(), SettingsContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var presenter: SettingsPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.settings_activity_layout)

        presenter = SettingsPresenter()

        setupToolbar()
        setupThemeSpinner()
        setupNotePreviewLinesSpinner()
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
        toolbar.isEditMode = false
        toolbar.setTitle(R.string.settingsTitle)
        toolbar.setLeftButtonImage(R.drawable.icon_menu)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
            override fun onRightButtonClick() = Unit
            override fun onTextChange(text: String) = Unit
        })
    }

    private fun setupThemeSpinner() {
        val themeAdapter = ArrayAdapter<String>(this, R.layout.settings_selector_item, THEMES)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorSettingsTheme.adapter = themeAdapter
        selectorSettingsTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // TODO - Handle Item Select
            }
        }
        textSettingsThemeLabel.setOnClickListener { selectorSettingsTheme.performClick() }
    }

    private fun setupNotePreviewLinesSpinner() {
        val linesAdapter = ArrayAdapter<String>(this, R.layout.settings_selector_item, NOTE_PREVIEW_LINES)
        linesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorSettingsLines.adapter = linesAdapter
        selectorSettingsLines.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // TODO - Handle Item Select
            }
        }
        textSettingsNoteLinesLabel.setOnClickListener { selectorSettingsLines.performClick() }
    }


    /*--- Constants ---*/

    companion object {
        private val THEMES = listOf("Monochrome", "Blue")
        private val NOTE_PREVIEW_LINES = listOf("1 Line", "2 Lines", "3 Lines", "4 Lines", "5 Lines")
    }

}
