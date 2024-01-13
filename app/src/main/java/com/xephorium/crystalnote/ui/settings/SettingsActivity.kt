package com.xephorium.crystalnote.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import android.widget.ArrayAdapter
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
        setupApplyThemeButton()
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

    override fun populateHomePreviewLines(lines: Int) {
        settingsBinding.selectorSettingsLines.setSelection(lines)
    }

    override fun populateHomeDateType(dateType: DateType) {
        settingsBinding.selectorSettingsDate.setSelection(dateType.ordinal)
    }

    override fun populateHomeColorBarSwitch(checked: Boolean) {
        settingsBinding.switchSettingsNoteColorBar.isChecked = checked
    }

    override fun populateHomeThemedColorBarSwitch(checked: Boolean) {
        settingsBinding.switchSettingsNoteBarThemed.isChecked = checked
    }

    override fun populateHomeTodayHeaderSwitch(checked: Boolean) {
        settingsBinding.switchSettingsToday.isChecked = checked
    }

    override fun populateHomeOptionsSwitch(checked: Boolean) {
        settingsBinding.switchSettingsHomeOptions.isChecked = checked
    }

    override fun populateNoteUnderlineSwitch(checked: Boolean) {
        settingsBinding.switchSettingsUnderline.isChecked = checked
    }

    override fun populateNoteScrollButtonSwitch(checked: Boolean) {
        settingsBinding.switchSettingsScrollButton.isChecked = checked
    }

    override fun populateNoteColorOrbSwitch(checked: Boolean) {
        settingsBinding.switchSettingsColorOrb.isChecked = checked
    }

    override fun populateNoteMonospaceSwitch(checked: Boolean) {
        settingsBinding.switchSettingsMonospace.isChecked = checked
    }

    override fun setPreviewTheme(theme: String) {
        settingsBinding.viewHomePreview.setTheme(CrystalNoteTheme.fromThemeName(this, theme))
        settingsBinding.viewNotePreview.setTheme(CrystalNoteTheme.fromThemeName(this, theme))
    }

    override fun setHomePreviewLines(lines: Int) {
        settingsBinding.viewHomePreview.setPreviewLines(lines)
    }

    override fun setHomePreviewDateType(type: DateType) {
        settingsBinding.viewHomePreview.setDateType(type)
    }

    override fun setHomePreviewColorBarVisibility(visible: Boolean) {
        settingsBinding.viewHomePreview.setNoteColorBarVisible(visible)
    }

    override fun setHomePreviewColorBarThemed(themed: Boolean) {
        settingsBinding.viewHomePreview.setNoteColorBarThemed(themed)
    }

    override fun setHomePreviewHeaderVisibility(visible: Boolean) {
        settingsBinding.viewHomePreview.setHeadersVisible(visible)
    }

    override fun setHomePreviewOptionsVisibility(visible: Boolean) {
        settingsBinding.viewHomePreview.setOptionsVisible(visible)
    }

    override fun setNotePreviewUnderlineVisibility(visible: Boolean) {
        settingsBinding.viewNotePreview.setUnderlineVisible(visible)
    }

    override fun setNoteScrollButtonVisibility(visible: Boolean) {
        settingsBinding.viewNotePreview.setScrollButtonVisible(visible)
    }

    override fun setNoteColorOrbVisibility(visible: Boolean) {
        settingsBinding.viewNotePreview.setColorOrbVisible(visible)
    }

    override fun setNoteMonospaceFontVisibility(visible: Boolean) {
        settingsBinding.viewNotePreview.setMonospaceFontVisible(visible)
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showDiscardThemeChangeDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Discard Theme Change")
        dialog.setMessage("Theme change has not been applied. Discard change?")
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
                    presenter.handleHomeLinesChange(position)
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
                    presenter.handleHomeDateTypeChange(DateType.values()[position])
                }
            }
            textSettingsNoteDateLabel.setOnClickListener { selectorSettingsDate.performClick() }
        }
    }

    private fun setupSwitches() {
        settingsBinding.run {
            switchSettingsNoteColorBar.setOnCheckedChangeListener { _, checked ->
                presenter.handleHomeColorBarToggle(checked)
            }
            switchSettingsNoteBarThemed.setOnCheckedChangeListener { _, checked ->
                presenter.handleHomeThemedColorBarToggle(checked)
            }
            switchSettingsToday.setOnCheckedChangeListener { _, checked ->
                presenter.handleHomeTodayHeaderToggle(checked)
            }
            switchSettingsHomeOptions.setOnCheckedChangeListener { _, checked ->
                presenter.handleHomeOptionsToggle(checked)
            }

            switchSettingsUnderline.setOnCheckedChangeListener { _, checked ->
                presenter.handleNoteUnderlineToggle(checked)
            }
            switchSettingsScrollButton.setOnCheckedChangeListener { _, checked ->
                presenter.handleNoteScrollButtonToggle(checked)
            }
            switchSettingsColorOrb.setOnCheckedChangeListener { _, checked ->
                presenter.handleNoteColorOrbToggle(checked)
            }
            switchSettingsMonospace.setOnCheckedChangeListener { _, checked ->
                presenter.handleNoteMonospaceToggle(checked)
            }
        }
    }

    private fun setupApplyThemeButton() {
        settingsBinding.buttonApplyTheme.setOnClickListener { presenter.handleApplyThemeClick() }
    }


    /*--- Constants ---*/

    companion object {
        private val THEMES = CrystalNoteTheme.Themes.values().map { it.displayName }
        private val NOTE_PREVIEW_LINES = listOf("None", "1 Line", "2 Lines", "3 Lines", "4 Lines")
        private val NOTE_DATE = DateType.values().map { it.displayName }
    }

}
