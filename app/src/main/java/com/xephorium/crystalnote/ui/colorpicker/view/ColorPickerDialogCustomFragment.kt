package com.xephorium.crystalnote.ui.colorpicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.ui.custom.ColorOrb

class ColorPickerDialogCustomFragment(
    private val listener: ColorPickerCustomListener
) : Fragment() {


    /*--- Variable Declarations ---*/

    lateinit var theme: CrystalNoteTheme


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
    }


    /*--- View Manipulation Methods ---*/

    fun setCustomColor(color: Int) {
        val customColorOrb = view?.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setColor(color)
    }


    /*--- Private Methods ---*/

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
            listener.onHexChange(text.toString())
        }
    }


    /*--- Constants ---*/

    companion object {
        interface ColorPickerCustomListener {
            fun onHexChange(hex: String)
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
