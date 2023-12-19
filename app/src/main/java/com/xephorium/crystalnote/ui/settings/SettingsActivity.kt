package com.xephorium.crystalnote.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.DateType
import com.xephorium.crystalnote.databinding.SettingsActivityLayoutBinding
import com.xephorium.crystalnote.ui.custom.CrystalNoteDialog


class SettingsActivity : DrawerActivity(), SettingsContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var settingsBinding: SettingsActivityLayoutBinding

    private lateinit var presenter: SettingsPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = SettingsActivityLayoutBinding.inflate(layoutInflater)
        setBoundViewAsContent(settingsBinding)

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

    @Deprecated("Deprecated in Java")
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
        settingsBinding.selectorSettingsTheme.setSelection(themeIndex)
    }

    override fun populateNotePreviewLines(lines: Int) {
        settingsBinding.selectorSettingsLines.setSelection(lines)
    }

    override fun populateNoteDateType(dateType: DateType) {
        settingsBinding.selectorSettingsDate.setSelection(dateType.ordinal)
    }

    override fun populateNoteColorBarSwitch(checked: Boolean) {
        settingsBinding.switchSettingsNoteColorBar.isChecked = checked
    }

    override fun populateThemedColorBarSwitch(checked: Boolean) {
        settingsBinding.switchSettingsNoteBarThemed.isChecked = checked
    }

    override fun populateTodayHeaderSwitch(checked: Boolean) {
        settingsBinding.switchSettingsToday.isChecked = checked
    }

    override fun populateNoteUnderlineSwitch(checked: Boolean) {
        settingsBinding.switchSettingsUnderline.isChecked = checked
    }

    override fun populateMonospaceSwitch(checked: Boolean) {
        settingsBinding.switchSettingsMonospace.isChecked = checked
    }

    override fun setPreviewTheme(theme: String) {
        settingsBinding.themePreview.setTheme(CrystalNoteTheme.fromThemeName(this, theme))
    }

    override fun setPreviewLines(lines: Int) {
        settingsBinding.themePreview.setPreviewLines(lines)
    }

    override fun setPreviewDateType(type: DateType) {
        settingsBinding.themePreview.setDateType(type)
    }

    override fun setPreviewColorBarVisibility(visible: Boolean) {
        settingsBinding.themePreview.setNoteColorBarVisible(visible)
    }

    override fun setPreviewColorBarThemed(themed: Boolean) {
        settingsBinding.themePreview.setNoteColorBarThemed(themed)
    }

    override fun setPreviewHeaderVisibility(visible: Boolean) {
        settingsBinding.themePreview.setHeadersVisible(visible)
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showDiscardChangesDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Discard Changes")
        dialog.setMessage("Your changes have not been saved. Discard changes?")
        dialog.setPositiveButtonName("Discard")
        dialog.setNegativeButtonName("Cancel")
        dialog.setListener(object : CrystalNoteDialog.Companion.CrystalNoteDialogListener {
            override fun onPositiveClick() {
                presenter.handleBackConfirm()
            }
            override fun onNegativeClick() = Unit
            override fun onBackClick() = Unit
        })
    }

    override fun refreshScreen() {
        recreate()
    }

    override fun navigateBack() {
        super.onBackPressed()
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        drawerBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.settingsTitle)
            setLeftButtonImage(R.drawable.icon_menu)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupThemeSpinner() {
        val themeAdapter = ArrayAdapter(this, R.layout.settings_selector_item, THEMES)
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        settingsBinding.run {
            selectorSettingsTheme.adapter = themeAdapter
            selectorSettingsTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    presenter.handleThemeChange(CrystalNoteTheme.Themes.values()[position].displayName)
                }
            }
            textSettingsThemeLabel.setOnClickListener { selectorSettingsTheme.performClick() }
        }
    }

    private fun setupNotePreviewLinesSpinner() {
        val linesAdapter = ArrayAdapter(this, R.layout.settings_selector_item, NOTE_PREVIEW_LINES)
        linesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        settingsBinding.run {
            selectorSettingsLines.adapter = linesAdapter
            selectorSettingsLines.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    presenter.handleNoteLinesChange(position)
                }
            }
            textSettingsNoteLinesLabel.setOnClickListener { selectorSettingsLines.performClick() }
        }
    }

    private fun setupNoteDateType() {
        val dateAdapter = ArrayAdapter(this, R.layout.settings_selector_item, NOTE_DATE)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        settingsBinding.run {
            selectorSettingsDate.adapter = dateAdapter
            selectorSettingsDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    presenter.handleNoteDateTypeChange(DateType.values()[position])
                }
            }
            textSettingsNoteDateLabel.setOnClickListener { selectorSettingsDate.performClick() }
        }
    }

    private fun setupSwitches() {
        settingsBinding.run {
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
    }

    private fun setupSaveButton() {
        settingsBinding.buttonSave.setOnClickListener { presenter.handleSaveClick() }
    }


    /*--- Constants ---*/

    companion object {
        private val THEMES = CrystalNoteTheme.Themes.values().map { it.displayName }
        private val NOTE_PREVIEW_LINES = listOf("None", "1 Line", "2 Lines", "3 Lines", "4 Lines")
        private val NOTE_DATE = DateType.values().map { it.displayName }
    }

}
