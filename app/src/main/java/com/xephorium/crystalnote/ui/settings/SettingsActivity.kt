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
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.DateType


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
        setupNoteDateType()
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

    override fun populateTheme(theme: String) {
        val themeIndex = CrystalNoteTheme.Themes.values().firstOrNull { it.displayName == theme }?.ordinal ?: 0
        selectorSettingsTheme.setSelection(themeIndex)
    }

    override fun populateNotePreviewLines(lines: Int) {
        selectorSettingsLines.setSelection(lines)
    }

    override fun populateNoteDateType(dateType: DateType) {
        selectorSettingsDate.setSelection(dateType.ordinal)
    }

    override fun populateNoteColorBarSwitch(checked: Boolean) {
        switchSettingsNoteColorBar.isChecked = checked
    }

    override fun populateThemedColorBarSwitch(checked: Boolean) {
        switchSettingsNoteBarThemed.isChecked = checked
    }

    override fun populateTodayHeaderSwitch(checked: Boolean) {
        switchSettingsToday.isChecked = checked
    }

    override fun populateNoteUnderlineSwitch(checked: Boolean) {
        switchSettingsUnderline.isChecked = checked
    }

    override fun populateMonospaceSwitch(checked: Boolean) {
        switchSettingsMonospace.isChecked = checked
    }

    override fun setPreviewTheme(theme: String) {
        themePreview.setTheme(CrystalNoteTheme.fromThemeName(this, theme))
    }

    override fun setPreviewLines(lines: Int) {
        themePreview.setPreviewLines(lines)
    }

    override fun setPreviewDateType(type: DateType) {
        themePreview.setDateType(type)
    }

    override fun setPreviewColorBarVisibility(visible: Boolean) {
        themePreview.setNoteColorBarVisible(visible)
    }

    override fun setPreviewColorBarThemed(themed: Boolean) {
        themePreview.setNoteColorBarThemed(themed)
    }

    override fun setPreviewHeaderVisibility(visible: Boolean) {
        themePreview.setHeadersVisible(visible)
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showDiscardChangesDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
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

    override fun refreshScreen() {
        recreate()
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
            override fun onButtonClick() = presenter.handleMenuButtonClick()
            override fun onColorClick() = Unit
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
                presenter.handleThemeChange(CrystalNoteTheme.Themes.values()[position].displayName)
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
                presenter.handleNoteLinesChange(position)
            }
        }
        textSettingsNoteLinesLabel.setOnClickListener { selectorSettingsLines.performClick() }
    }

    private fun setupNoteDateType() {
        val dateAdapter = ArrayAdapter<String>(this, R.layout.settings_selector_item, NOTE_DATE)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorSettingsDate.adapter = dateAdapter
        selectorSettingsDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.handleNoteDateTypeChange(DateType.values()[position])
            }
        }
        textSettingsNoteDateLabel.setOnClickListener { selectorSettingsDate.performClick() }
    }

    private fun setupSwitches() {
        switchSettingsNoteColorBar.setOnCheckedChangeListener { _, checked ->
            presenter.handleNoteColorBarToggle(checked)
        }
        switchSettingsNoteBarThemed.setOnCheckedChangeListener { _, checked ->
            presenter.handleThemedColorBarToggle(checked)
        }
        switchSettingsToday.setOnCheckedChangeListener { _, checked ->
            presenter.handleTodayHeaderToggle(checked)
        }
        switchSettingsUnderline.setOnCheckedChangeListener { _, checked ->
            presenter.handleNoteUnderlineToggle(checked)
        }
        switchSettingsMonospace.setOnCheckedChangeListener { _, checked ->
            presenter.handleMonospaceToggle(checked)
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener { presenter.handleSaveClick() }
    }


    /*--- Constants ---*/

    companion object {
        private val THEMES = CrystalNoteTheme.Themes.values().map { it.displayName }
        private val NOTE_PREVIEW_LINES = listOf("None", "1 Line", "2 Lines", "3 Lines", "4 Lines")
        private val NOTE_DATE = DateType.values().map { it.displayName }
    }

}
