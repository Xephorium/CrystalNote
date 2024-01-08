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
import com.xephorium.crystalnote.data.model.FavoriteColorQueue
import com.xephorium.crystalnote.data.utility.ColorUtility
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
import com.xephorium.crystalnote.ui.custom.ColorOrb
import com.xephorium.crystalnote.ui.custom.RainbowView
import com.xephorium.crystalnote.ui.custom.RainbowView.Companion.RainbowViewListener
import com.xephorium.crystalnote.ui.extensions.getSelectableItemBackgroundBorderless


class ColorPickerDialogCustomFragment(
    private val listener: ColorPickerCustomListener,
    private var customColor: PreciseColor,
    private var favoriteColors: FavoriteColorQueue
) : Fragment() {


    /*--- Variable Declarations ---*/

    lateinit var theme: CrystalNoteTheme

    private var notUpdatingViews = true


    /*--- Lifecycle Methods ---*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        return inflater.inflate(R.layout.color_picker_dialog_custom_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTheme()

        setupRainbowView()
        setupCustomOrb()
        setupFavoriteOrbs()
        setupHexField()
        setupHsvSliders()
        setupHsvFields()

        setCustomColor(customColor)
        setFavoriteColors(favoriteColors)
    }


    /*--- View Manipulation Methods ---*/

    fun setCustomColor(color: PreciseColor) {
        notUpdatingViews = false
        customColor = color

        updateRainbowView(customColor)
        updateCustomColorOrb(customColor)

        updateHexField(customColor)

        updateHsvSliders(customColor)
        updateHsvFields(customColor)

        notUpdatingViews = true
    }

    fun setFavoriteColors(colors: FavoriteColorQueue) {
        favoriteColors = colors
        for (index in favoriteColors.getAll().indices) {
            val orb = view?.findViewById<ColorOrb>(FAVORITE_ORBS[index])
            orb?.resetOutlineState()
            orb?.setColor(favoriteColors.getAll()[index])
            orb?.let { setupFavoriteOrbClickBehavior(it, index) }
        }
    }

    fun notifyTabChanged(colorPickerTab: ColorPickerTab) {
        clearFocusForTextInputs()
        view?.findViewById<RainbowView>(R.id.rainbowViewCustomColor)?.notifyVisible()
    }


    /*--- Private Setup Methods ---*/

    private fun setupTheme() {
        theme = CrystalNoteTheme.fromCurrentTheme(requireContext())
    }

    private fun setupRainbowView() {
        val rainbowView = view?.findViewById<RainbowView>(R.id.rainbowViewCustomColor)
        rainbowView?.setListener(object : RainbowViewListener {
            override fun onRainbowClick(x: Float, y: Float) {
                listener.onRainbowClick(x, y)
            }
        })
    }

    private fun setupCustomOrb() {
        val customColorOrb = view?.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setPadding(R.dimen.colorPickerCustomOrbPadding)
        customColorOrb?.setBackdropColor(theme.colorBackground)
        customColorOrb?.setOutlineColor(theme.colorTextPrimary)
        customColorOrb?.enableForcedThickOutline()
    }

    private fun setupFavoriteOrbs() {
        for (index in FAVORITE_ORBS.indices) {
            val orb = view?.findViewById<ColorOrb>(FAVORITE_ORBS[index])
            orb?.setPadding(R.dimen.colorPickerCustomOrbFavoritePadding)
            orb?.setBackdropColor(theme.colorBackground)
            orb?.setColor(theme.colorNoteBackground)
            orb?.setOutlineAlpha(0.0)
        }
    }

    private fun setupFavoriteOrbClickBehavior(orb: ColorOrb, index: Int) {
        orb.setBackgroundResource(requireContext().getSelectableItemBackgroundBorderless())
        orb.isClickable = true
        orb.isFocusable = true
        orb.setOnClickListener {
            clearFocusForTextInputs()
            listener.onFavoriteClick(favoriteColors.getAll()[index])
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
            if (notUpdatingViews) {
                clearFocusForTextInputs()
                listener.onHueChange(value.toInt().toString())
            }
        })
        val sliderSat = view?.findViewById<Slider>(R.id.sliderCustomColorSat)
        sliderSat?.addOnChangeListener(OnChangeListener { slider, value, fromUser ->
            if (notUpdatingViews) {
                clearFocusForTextInputs()
                listener.onSatChange(value.toInt().toString())
            }
        })
        val sliderVal = view?.findViewById<Slider>(R.id.sliderCustomColorVal)
        sliderVal?.addOnChangeListener(OnChangeListener { slider, value, fromUser ->
            if (notUpdatingViews) {
                clearFocusForTextInputs()
                listener.onValChange(value.toInt().toString())
            }
        })

    }

    private fun setupHsvFields() {
        val editTextHue = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHue)
        editTextHue?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onHueChange(text.toString())
        }
        editTextHue?.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                notUpdatingViews = false
                editTextHue.setText(customColor.hue.toString())
                notUpdatingViews = true
            }
        }
        val editTextSat = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorSat)
        editTextSat?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onSatChange(text.toString())
        }
        editTextSat?.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                notUpdatingViews = false
                editTextSat.setText(customColor.saturation.toString())
                notUpdatingViews = true
            }
        }
        val editTextVal = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorVal)
        editTextVal?.doOnTextChanged { text, _, _, _ ->
            if (notUpdatingViews) listener.onValChange(text.toString())
        }
        editTextVal?.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                notUpdatingViews = false
                editTextVal.setText(customColor.value.toString())
                notUpdatingViews = true
            }
        }
    }


    /*--- Private Update Methods ---*/

    private fun updateRainbowView(color: PreciseColor) {
        val rainbowView = view?.findViewById<RainbowView>(R.id.rainbowViewCustomColor)
        rainbowView?.updateColor(color)
    }

    private fun updateCustomColorOrb(color: PreciseColor) {
        val customColorOrb = view?.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setColor(color.toIntColor())
    }

    private fun updateHexField(color: PreciseColor) {
        val editTextHex = view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHex)
        val newHex = color.toHexString()
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

    private fun clearFocusForTextInputs() {
        view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHex)?.clearFocus()
        view?.findViewById<TextInputEditText>(R.id.textInputCustomColorHue)?.clearFocus()
        view?.findViewById<TextInputEditText>(R.id.textInputCustomColorSat)?.clearFocus()
        view?.findViewById<TextInputEditText>(R.id.textInputCustomColorVal)?.clearFocus()
    }


    /*--- Constants ---*/

    companion object {
        interface ColorPickerCustomListener {
            fun onHexChange(hex: String)
            fun onHueChange(hue: String)
            fun onSatChange(sat: String)
            fun onValChange(value: String)
            fun onRainbowClick(x: Float, y: Float)
            fun onFavoriteClick(color: Int)
        }

        private val FAVORITE_ORBS = listOf(
            R.id.colorOrbFavorite1,
            R.id.colorOrbFavorite2,
            R.id.colorOrbFavorite3,
            R.id.colorOrbFavorite4,
            R.id.colorOrbFavorite5,
            R.id.colorOrbFavorite6,
            R.id.colorOrbFavorite7,
            R.id.colorOrbFavorite8
        )
    }
}
