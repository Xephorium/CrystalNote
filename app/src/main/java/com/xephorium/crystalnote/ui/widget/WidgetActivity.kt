package com.xephorium.crystalnote.ui.widget

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.ui.custom.ColorPickerDialog
import com.xephorium.crystalnote.ui.custom.ColorPickerDialog.Companion.ColorPickerListener
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import com.xephorium.crystalnote.ui.extensions.getThemeColor
import kotlinx.android.synthetic.main.toolbar_activity_layout.*
import kotlinx.android.synthetic.main.widget_activity_layout.*
import kotlinx.android.synthetic.main.widget_activity_layout.buttonSave


class WidgetActivity : DrawerActivity(), WidgetContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var presenter: WidgetPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContent(R.layout.widget_activity_layout)

        presenter = WidgetPresenter()
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(this)
        presenter.noteRoomRepository = NoteRoomRepository(this)
        presenter.previewBackgroundBright = getThemeColor(R.attr.themeNoteBackground) ==
                resources.getColor(R.color.lightNoteBackground)

        setupToolbar()
        setupPreviewIcons()
        setupColorOrbs()
        setupTextSizeSpinner()
        setupBackgroundAlphaSpinner()
        setupContentAlphaSpinner()
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

    override fun setupWidgetSelector(widgetNames: List<String>) {
        val widgetAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            widgetNames
        )
        widgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorWidgetSettingsSelection.adapter = widgetAdapter
        selectorWidgetSettingsSelection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                    presenter.handleWidgetChange(position)
                }
        }
        textWidgetSettingsSelectionLabel.setOnClickListener {
            selectorWidgetSettingsSelection.performClick()
        }
    }

    override fun populateWidgetSelector(index: Int) {
        selectorWidgetSettingsSelection.setSelection(index)
    }

    override fun populateBackgroundColor(color: Int) {
        colorOrbWidgetSettingsBackgroundColor.setColor(color)
    }

    override fun populateTitleColor(color: Int) {
        colorOrbWidgetSettingsTitleColor.setColor(color)
    }

    override fun populateContentColor(color: Int) {
        colorOrbWidgetSettingsContentColor.setColor(color)
    }

    override fun populateTextSize(size: TextSize) {
        selectorWidgetSettingsTextSize.setSelection(size.ordinal)
    }

    override fun populateBackgroundAlpha(transparency: Transparency) {
        selectorWidgetSettingsBackgroundAlpha.setSelection(transparency.ordinal)
    }

    override fun populateContentAlpha(transparency: Transparency) {
        selectorWidgetSettingsContentAlpha.setSelection(transparency.ordinal)
    }

    override fun setPreviewBackgroundColor(color: Int) {
        widgetSettingsPreview.setBackgroundColor(color)
    }

    override fun setPreviewTitleColor(color: Int) {
        widgetSettingsPreview.setTitleColor(color)
    }

    override fun setPreviewContentColor(color: Int) {
        widgetSettingsPreview.setTextColor(color)
    }

    override fun setPreviewTextSize(size: TextSize) {
        widgetSettingsPreview.setTextSize(size)
    }

    override fun setPreviewBackgroundAlpha(transparency: Transparency) {
        widgetSettingsPreview.setTransparency(transparency)
    }

    override fun setPreviewBackgroundBrightness(light: Boolean) {

        // Determine Background & Icon Colors
        val backgroundColor: Int
        val iconColor: Int
        if (light) {
            backgroundColor = getThemeColor(R.attr.themePreviewBackgroundLight)
            iconColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.white), 110)
        } else {
            backgroundColor = getThemeColor(R.attr.themePreviewBackgroundDark)
            iconColor = resources.getColor(R.color.darkNoteBackground)
        }

        // Set Background & Icon Colors
        viewWidgetSettingsPreviewBackground.setBackgroundColor(backgroundColor)
        val icon = ContextCompat.getDrawable(this, R.drawable.icon_contrast)
        val drawable = DrawableCompat.wrap(icon!!)
        DrawableCompat.setTint(drawable, iconColor)
        iconWidgetSettingsContrast.setImageDrawable(drawable)
    }

    override fun showNoWidgetsMessage() {
        scrollViewWidgetSettings.visibility = View.GONE
        buttonSave.visibility = View.GONE
        textWidgetSettingsEmpty.visibility = View.VISIBLE
    }

    override fun hideNoWidgetsMessage() {
        scrollViewWidgetSettings.visibility = View.VISIBLE
        buttonSave.visibility = View.VISIBLE
        textWidgetSettingsEmpty.visibility = View.GONE
    }

    override fun showBackgroundColorPickerDialog() {
        val colorPickerDialog = ColorPickerDialog.Builder(this).create()
        colorPickerDialog.setTitle("Choose Background Color")
        colorPickerDialog.setColorPickerListener(object : ColorPickerListener {
            override fun onColorSelect(color: Int) {
                colorPickerDialog.dismiss()
                presenter.handleBackgroundColorChange(color)
            }
        })
        colorPickerDialog.show()
    }

    override fun showTitleColorPickerDialog() {
        val colorPickerDialog = ColorPickerDialog.Builder(this).create()
        colorPickerDialog.setTitle("Choose Title Color")
        colorPickerDialog.setColorPickerListener(object : ColorPickerListener {
            override fun onColorSelect(color: Int) {
                colorPickerDialog.dismiss()
                presenter.handleTitleColorChange(color)
            }
        })
        colorPickerDialog.show()
    }

    override fun showContentColorPickerDialog() {
        val colorPickerDialog = ColorPickerDialog.Builder(this).create()
        colorPickerDialog.setTitle("Choose Text Color")
        colorPickerDialog.setColorPickerListener(object : ColorPickerListener {
            override fun onColorSelect(color: Int) {
                colorPickerDialog.dismiss()
                presenter.handleContentColorChange(color)
            }
        })
        colorPickerDialog.show()
    }

    override fun showNavigationDrawer() {
        openDrawer()
    }

    override fun showDiscardChangesDialog(widgetNames: String) {
        val alertDialog = AlertDialog.Builder(this, R.style.DialogTheme).create()
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Discard Changes")
        alertDialog.setMessage(
            "The following widgets have not been saved. Discard changes?\n\n${widgetNames}"
        )
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
        toolbar.setTitle(R.string.widgetTitle)
        toolbar.setLeftButtonImage(R.drawable.icon_menu)
        toolbar.setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
            override fun onButtonClick() = presenter.handleMenuButtonClick()
            override fun onColorClick() = Unit
            override fun onTextChange(text: String) = Unit
        })
    }

    private fun setupPreviewIcons() {
        iconWidgetSettingsContrast.setOnClickListener {
            presenter.handlePreviewBackgroundBrightnessToggle()
        }
    }

    private fun setupColorOrbs() {

        // Retrieve Theme
        val theme = CrystalNoteTheme.fromCurrentTheme(this)

        // Background Color Orb
        layoutWidgetSettingsBackgroundColor.setOnClickListener {
            colorOrbWidgetSettingsBackgroundColor.performClick()
        }
        textWidgetSettingsBackgroundColorLabel.setOnClickListener {
            colorOrbWidgetSettingsBackgroundColor.performClick()
        }
        colorOrbWidgetSettingsBackgroundColor.setTheme(theme)
        colorOrbWidgetSettingsBackgroundColor.setOnClickListener {
            presenter.handleBackgroundColorClick()
        }

        // Title Color Orb
        layoutWidgetSettingsTitleColor.setOnClickListener {
            colorOrbWidgetSettingsTitleColor.performClick()
        }
        textWidgetSettingsTitleColorLabel.setOnClickListener {
            colorOrbWidgetSettingsTitleColor.performClick()
        }
        colorOrbWidgetSettingsTitleColor.setTheme(theme)
        colorOrbWidgetSettingsTitleColor.setOnClickListener {
            presenter.handleTitleColorClick()
        }

        // Text Color Orb
        layoutWidgetSettingsContentColor.setOnClickListener {
            colorOrbWidgetSettingsContentColor.performClick()
        }
        textWidgetSettingsContentColorLabel.setOnClickListener {
            colorOrbWidgetSettingsContentColor.performClick()
        }
        colorOrbWidgetSettingsContentColor.setTheme(theme)
        colorOrbWidgetSettingsContentColor.setOnClickListener {
            presenter.handleContentColorClick()
        }
    }

    private fun setupTextSizeSpinner() {
        val textSizesAdapter =
            ArrayAdapter<String>(this, R.layout.settings_selector_item, TEXT_SIZES)
        textSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorWidgetSettingsTextSize.adapter = textSizesAdapter
        selectorWidgetSettingsTextSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                    presenter.handleTextSizeChange(TextSize.values()[position])
                }
            }
        textWidgetSettingsTextSizeLabel.setOnClickListener {
            selectorWidgetSettingsTextSize.performClick()
        }
    }

    private fun setupBackgroundAlphaSpinner() {
        val transparencyAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            TRANSPARENCY_VALUES
        )
        transparencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorWidgetSettingsBackgroundAlpha.adapter = transparencyAdapter
        selectorWidgetSettingsBackgroundAlpha.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                    presenter.handleBackgroundAlphaChange(Transparency.values()[position])
                }
            }
        textWidgetSettingsBackgroundAlphaLabel.setOnClickListener {
            selectorWidgetSettingsBackgroundAlpha.performClick()
        }
    }

    private fun setupContentAlphaSpinner() {
        val textTransparencyAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            TRANSPARENCY_VALUES
        )
        textTransparencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorWidgetSettingsContentAlpha.adapter = textTransparencyAdapter
        selectorWidgetSettingsContentAlpha.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                    presenter.handleContentAlphaChange(Transparency.values()[position])
                }
            }
        textWidgetSettingsContentAlphaLabel.setOnClickListener {
            selectorWidgetSettingsContentAlpha.performClick()
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener { presenter.handleSaveClick() }
    }

    override fun refreshWidgets() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }


    /*--- Constants ---*/

    companion object {
        private val TEXT_SIZES = TextSize.values().map { it.displayName }
        private val TRANSPARENCY_VALUES = Transparency.values().map { it.displayName }
    }
}
