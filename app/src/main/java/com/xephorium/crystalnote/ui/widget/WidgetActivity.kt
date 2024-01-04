package com.xephorium.crystalnote.ui.widget

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.model.WidgetState.Companion.CornerCurve
import com.xephorium.crystalnote.data.model.WidgetState.Companion.TextSize
import com.xephorium.crystalnote.data.model.WidgetState.Companion.Transparency
import com.xephorium.crystalnote.data.repository.NoteRoomRepository
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.databinding.WidgetActivityLayoutBinding
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialog
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialog.Companion.ColorPickerListener
import com.xephorium.crystalnote.ui.custom.CrystalNoteDialog
import com.xephorium.crystalnote.ui.custom.NoteToolbar
import com.xephorium.crystalnote.ui.drawer.DrawerActivity
import com.xephorium.crystalnote.ui.extensions.getThemeColor


class WidgetActivity : DrawerActivity(), WidgetContract.View {


    /*--- Variable Declarations ---*/

    private lateinit var binding: WidgetActivityLayoutBinding

    private lateinit var presenter: WidgetPresenter


    /*--- Lifecycle Methods ---*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WidgetActivityLayoutBinding.inflate(layoutInflater)
        setBoundViewAsContent(binding)

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
        setupCornerCurveSpinner()
        setupCornerCurveWarning()
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

    override fun setupWidgetSelector(widgetNames: List<String>) {
        val widgetAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            widgetNames
        )
        widgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.run {
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
    }

    override fun populateWidgetSelector(index: Int) {
        binding.selectorWidgetSettingsSelection.setSelection(index)
    }

    override fun populateBackgroundColor(color: Int) {
        binding.colorOrbWidgetSettingsBackgroundColor.setColor(color)
    }

    override fun populateTitleColor(color: Int) {
        binding.colorOrbWidgetSettingsTitleColor.setColor(color)
    }

    override fun populateContentColor(color: Int) {
        binding.colorOrbWidgetSettingsContentColor.setColor(color)
    }

    override fun populateTextSize(size: TextSize) {
        binding.selectorWidgetSettingsTextSize.setSelection(size.ordinal)
    }

    override fun populateBackgroundAlpha(transparency: Transparency) {
        binding.selectorWidgetSettingsBackgroundAlpha.setSelection(transparency.ordinal)
    }

    override fun populateContentAlpha(transparency: Transparency) {
        binding.selectorWidgetSettingsContentAlpha.setSelection(transparency.ordinal)
    }

    override fun populateCornerCurve(cornerCurve: CornerCurve) {
        binding.selectorWidgetSettingsCornerCurve.setSelection(cornerCurve.ordinal)
    }

    override fun setPreviewBackgroundColor(color: Int) {
        binding.widgetSettingsPreview.setBackgroundColor(color)
    }

    override fun setPreviewTitleColor(color: Int) {
        binding.widgetSettingsPreview.setTitleColor(color)
    }

    override fun setPreviewContentColor(color: Int) {
        binding.widgetSettingsPreview.setTextColor(color)
    }

    override fun setPreviewTextSize(size: TextSize) {
        binding.widgetSettingsPreview.setTextSize(size)
    }

    override fun setPreviewBackgroundAlpha(transparency: Transparency) {
        binding.widgetSettingsPreview.setTransparency(transparency)
    }

    override fun setPreviewCornerCurve(cornerCurve: CornerCurve) {
        binding.widgetSettingsPreview.setCornerCurve(cornerCurve)
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
            iconColor = resources.getColor(R.color.darkBackdropDark)
        }

        // Set Background & Icon Colors
        binding.viewWidgetSettingsPreviewBackground.setBackgroundColor(backgroundColor)
        binding.viewWidgetSettingsBackground.setBackgroundColor(backgroundColor)
        val icon = ContextCompat.getDrawable(this, R.drawable.icon_contrast)
        val drawable = DrawableCompat.wrap(icon!!)
        DrawableCompat.setTint(drawable, iconColor)
        binding.iconWidgetSettingsContrast.setImageDrawable(drawable)
    }

    override fun showCornerCurveWarningIcon() {
        binding.imageWidgetSettingsCornerCurveWarning.visibility = View.VISIBLE
    }

    override fun hideCornerCurveWarningIcon() {
        binding.imageWidgetSettingsCornerCurveWarning.visibility = View.GONE
    }

    override fun showCornerCurveWarningDialog() {
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Corner Curve Support")
        dialog.setMessage(
            "The default launcher on Android 12+ sets the corner curve of all widgets to Huge."
            + "\n\n"
            + "This setting will likely have no effect on your device unless you install a different launcher."
        )
        dialog.setPositiveButtonName("Okay")
    }

    override fun showNoWidgetsMessage() {
        binding.run {
            viewWidgetSettings.visibility = View.GONE
            textWidgetSettingsEmpty.visibility = View.VISIBLE
        }
    }

    override fun hideNoWidgetsMessage() {
        binding.run {
            viewWidgetSettings.visibility = View.VISIBLE
            textWidgetSettingsEmpty.visibility = View.GONE
        }
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
        val dialog = CrystalNoteDialog.Builder(this).create()
        dialog.show()
        dialog.setTitle("Discard Changes")
        dialog.setMessage(
            "The following widgets have not been saved. Discard changes?\n\n${widgetNames}"
        )
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

    override fun navigateBack() {
        super.onBackPressed()
    }


    /*--- Private Setup Methods ---*/

    private fun setupToolbar() {
        drawerBinding.toolbar.run {
            isEditMode = false
            setTitle(R.string.widgetTitle)
            setLeftButtonImage(R.drawable.icon_menu)
            setNoteToolbarListener(object : NoteToolbar.NoteToolbarListener {
                override fun onLeftButtonClick() = presenter.handleMenuButtonClick()
                override fun onRightButtonClick() = Unit
                override fun onColorClick() = Unit
                override fun onTextChange(text: String) = Unit
            })
        }
    }

    private fun setupPreviewIcons() {
        binding.iconWidgetSettingsContrast.setOnClickListener {
            presenter.handlePreviewBackgroundBrightnessToggle()
        }
    }

    private fun setupColorOrbs() {
        binding.run {

            // Retrieve Theme
            val theme = CrystalNoteTheme.fromCurrentTheme(this@WidgetActivity)

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
            colorOrbWidgetSettingsBackgroundColor.setPadding(R.dimen.paddingMicro)

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
            colorOrbWidgetSettingsTitleColor.setPadding(R.dimen.paddingMicro)

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
            colorOrbWidgetSettingsContentColor.setPadding(R.dimen.paddingMicro)
        }
    }

    private fun setupTextSizeSpinner() {
        val textSizesAdapter =
            ArrayAdapter<String>(this, R.layout.settings_selector_item, TEXT_SIZES)
        textSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.run {
            selectorWidgetSettingsTextSize.adapter = textSizesAdapter
            selectorWidgetSettingsTextSize.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                        presenter.handleTextSizeChange(TextSize.entries[position])
                    }
                }
            textWidgetSettingsTextSizeLabel.setOnClickListener {
                selectorWidgetSettingsTextSize.performClick()
            }
        }
    }

    private fun setupBackgroundAlphaSpinner() {
        val transparencyAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            TRANSPARENCY_VALUES
        )
        transparencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.run {
            selectorWidgetSettingsBackgroundAlpha.adapter = transparencyAdapter
            selectorWidgetSettingsBackgroundAlpha.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                        presenter.handleBackgroundAlphaChange(Transparency.entries[position])
                    }
                }
            textWidgetSettingsBackgroundAlphaLabel.setOnClickListener {
                selectorWidgetSettingsBackgroundAlpha.performClick()
            }
        }
    }

    private fun setupContentAlphaSpinner() {
        val textTransparencyAdapter = ArrayAdapter<String>(
            this,
            R.layout.settings_selector_item,
            TRANSPARENCY_VALUES
        )
        textTransparencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.run {
            selectorWidgetSettingsContentAlpha.adapter = textTransparencyAdapter
            selectorWidgetSettingsContentAlpha.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                        presenter.handleContentAlphaChange(Transparency.entries[position])
                    }
                }
            textWidgetSettingsContentAlphaLabel.setOnClickListener {
                selectorWidgetSettingsContentAlpha.performClick()
            }
        }
    }

    private fun setupCornerCurveSpinner() {
        val cornerCurveAdapter = ArrayAdapter(
            this,
            R.layout.settings_selector_item,
            CORNER_CURVE_VALUES
        )
        cornerCurveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.run {
            selectorWidgetSettingsCornerCurve.adapter = cornerCurveAdapter
            selectorWidgetSettingsCornerCurve.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(a: AdapterView<*>?, v: View?, position: Int, id: Long) {
                        presenter.handleCornerCurveChange(CornerCurve.entries[position])
                    }
                }
            textWidgetSettingsCornerCurveLabel.setOnClickListener {
                selectorWidgetSettingsCornerCurve.performClick()
            }
        }
    }

    private fun setupCornerCurveWarning() {
        binding.imageWidgetSettingsCornerCurveWarning.setOnClickListener {
            presenter.handleCornerCurveWarningClick()
        }
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener { presenter.handleSaveClick() }
    }

    override fun refreshWidgets() {
        NotesWidgetProvider.refreshWidgets(this, application)
    }


    /*--- Constants ---*/

    companion object {
        private val TEXT_SIZES = TextSize.entries.map { it.displayName }
        private val TRANSPARENCY_VALUES = Transparency.entries.map { it.displayName }
        private val CORNER_CURVE_VALUES = CornerCurve.entries.map { it.displayName }
    }
}
