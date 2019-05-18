package com.xephorium.crystalnote.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import kotlinx.android.synthetic.main.settings_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_activity_layout.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.xephorium.crystalnote.data.SharedPreferencesRepository


class SettingsActivity : DrawerActivity(), SettingsContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var presenter: SettingsPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.settings_activity_layout)

        presenter = SettingsPresenter()
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)

        setupToolbar()
        setupThemeSpinner()
        setupNotePreviewLinesSpinner()
        setupSwitches()
        setupSaveButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        if (drawerOpen) {
            super.onBackPressed()
        } else {
            presenter.handleBackClick()
        }
    }


    /*--- View Manipulation Methods ---*/

    override fun populateNoteColorsCheckbox(checked: Boolean) {
        switchSettingsNoteColors.isChecked = checked
    }

    override fun populateTodayHeaderCheckbox(checked: Boolean) {
        switchSettingsToday.isChecked = checked
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showDiscardChangesDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Discard Changes")
        alertDialog.setMessage("Your changes have not been saved. Discard changes?")
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes") { dialog, _ ->
            dialog.dismiss()
            presenter.handleBackConfirm()
        }
        alertDialog.show()
    }

    override fun navigateBack() {
        super.onBackPressed()
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

    private fun setupSwitches() {
        switchSettingsNoteColors.setOnCheckedChangeListener { _, checked ->
            presenter.handleNoteColorsToggle(checked)
        }
        switchSettingsToday.setOnCheckedChangeListener { _, checked ->
            presenter.handleTodayHeaderToggle(checked)
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener { presenter.handleSaveClick() }
    }


    /*--- Constants ---*/

    companion object {
        private val THEMES = listOf("Monochrome", "Blue")
        private val NOTE_PREVIEW_LINES = listOf("1 Line", "2 Lines", "3 Lines", "4 Lines", "5 Lines")
    }

}
