package com.xephorium.crystalnote.ui.colorpicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnChangeListener
import com.google.android.material.textfield.TextInputEditText
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.data.utility.ColorUtility
import com.xephorium.crystalnote.ui.colorpicker.ColorPickerDialogContract.Presenter.Companion.DEFAULT_CUSTOM_COLOR
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
import com.xephorium.crystalnote.ui.custom.ColorOrb
import com.xephorium.crystalnote.ui.custom.RainbowView

class ColorPickerDialogCustomFragment(
    private val listener: ColorPickerCustomListener
) : Fragment() {


    /*--- Variable Declarations ---*/

    lateinit var theme: CrystalNoteTheme

    var notUpdatingViews = true


    /*--- Lifecycle Methods ---*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        return inflater.inflate(R.layout.color_picker_dialog_custom_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTheme()
        setupCustomOrb()
        setupHistoryOrbs()

        setupHexField()
        setupHsvSliders()
        setupHsvFields()

        setCustomColor(DEFAULT_CUSTOM_COLOR)
    }


    /*--- View Manipulation Methods ---*/

    fun setCustomColor(color: PreciseColor) {
        notUpdatingViews = false

        updateRainbowView(color)
        updateCustomColorOrb(color)

        updateHexField(color)

        updateHsvSliders(color)
        updateHsvFields(color)

        notUpdatingViews = true
    }

    fun notifyVisible() {
        view?.findViewById<RainbowView>(R.id.rainbowViewCustomColor)?.notifyVisible()
    }


    /*--- Private Setup Methods ---*/

    private fun setupTheme() {
        theme = CrystalNoteTheme.fromCurrentTheme(requireContext())
    }

    private fun setupCustomOrb() {
        val customColorOrb = view?.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setPadding(R.dimen.colorCustomOrbCustomPadding)
        customColorOrb?.setBackdropColor(theme.colorBackground)
        customColorOrb?.setOutlineColor(theme.colorTextPrimary)
        customColorOrb?.enableForcedThickOutline()
    }

    private fun setupHistoryOrbs() {
        for (index in HISTORY_ORBS.indices) {
            val orb = view?.findViewById<ColorOrb>(HISTORY_ORBS[index])
            orb?.setPadding(R.dimen.colorCustomOrbHistoryPadding)
            orb?.setBackdropColor(theme.colorBackground)
            orb?.setColor(theme.colorNoteBackground)
            orb?.setOutlineAlpha(0.0)
        }
    }

    private fun setupHexField() {
        val editTextHex = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHex)
        editTextHex?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onHexChange(text.toString())
        }
    }

    private fun setupHsvSliders() {
        val sliderHue = view?.findViewById<Slider>(R.id.sliderCustomColorHue)
        sliderHue?.addOnChangeListener(OnChangeListener { slider, value, fromUser ->
            listener.onHueChange(value.toInt().toString())
        })
        val sliderSat = view?.findViewById<Slider>(R.id.sliderCustomColorSat)
        sliderSat?.addOnChangeListener(OnChangeListener { slider, value, fromUser ->
            listener.onSatChange(value.toInt().toString())
        })
        val sliderVal = view?.findViewById<Slider>(R.id.sliderCustomColorVal)
        sliderVal?.addOnChangeListener(OnChangeListener { slider, value, fromUser ->
            listener.onValChange(value.toInt().toString())
        })

    }

    private fun setupHsvFields() {
        val editTextHue = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHue)
        editTextHue?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onHueChange(text.toString())
        }
        val editTextSat = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorSat)
        editTextSat?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onSatChange(text.toString())
        }
        val editTextVal = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorVal)
        editTextVal?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onValChange(text.toString())
        }
    }


    /*--- Private Update Methods ---*/

    private fun updateRainbowView(color: PreciseColor) {
        val rainbowView = view?.findViewById<RainbowView>(R.id.rainbowViewCustomColor)
        rainbowView?.updateColor(color)
    }

    private fun updateCustomColorOrb(color: PreciseColor) {
        val customColorOrb = view?.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setColor(color.getIntColor())
    }

    private fun updateHexField(color: PreciseColor) {
        val editTextHex = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHex)
        val newHex = color.getHexCode()
        editTextHex?.let {
            if (!ColorUtility.areEqualHexColors(it.text.toString(), newHex)) {
                if (!editTextHex.isFocused) it.setText(newHex)
            }
        }
    }

    private fun updateHsvSliders(color: PreciseColor) {
        val sliderHue = view?.findViewById<Slider>(R.id.sliderCustomColorHue)
        if (sliderHue?.hasFocus() != true) sliderHue?.value = color.hue.toFloat()
        val sliderSat = view?.findViewById<Slider>(R.id.sliderCustomColorSat)
        if (sliderSat?.hasFocus() != true) sliderSat?.value = color.saturation.toFloat()
        val sliderVal = view?.findViewById<Slider>(R.id.sliderCustomColorVal)
        if (sliderVal?.hasFocus() != true) sliderVal?.value = color.value.toFloat()
    }

    private fun updateHsvFields(color: PreciseColor) {
        updateInputEditTextIfNotActive(R.id.textInputCustomColorHue, color.hue)
        updateInputEditTextIfNotActive(R.id.textInputCustomColorSat, color.saturation)
        updateInputEditTextIfNotActive(R.id.textInputCustomColorVal, color.value)
    }

    private fun updateInputEditTextIfNotActive(res: Int, newValue: Int) {
        val editText = view?.findViewById<TextInputEditText>(res)
        if (editText?.text.toString() != newValue.toString()) {
            if (editText?.isFocused == false) editText.setText(newValue.toString())
        }
    }


    /*--- Constants ---*/

    companion object {
        interface ColorPickerCustomListener {
            fun onHexChange(hex: String)
            fun onHueChange(hue: String)
            fun onSatChange(sat: String)
            fun onValChange(value: String)
        }

        private val HISTORY_ORBS = listOf(
            R.id.colorOrbHistory1,
            R.id.colorOrbHistory2,
            R.id.colorOrbHistory3,
            R.id.colorOrbHistory4,
            R.id.colorOrbHistory5,
            R.id.colorOrbHistory6,
            R.id.colorOrbHistory7,
            R.id.colorOrbHistory8
        )
    }
}
