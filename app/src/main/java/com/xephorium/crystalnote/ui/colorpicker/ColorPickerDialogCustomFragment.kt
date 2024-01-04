package com.xephorium.crystalnote.ui.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.ui.custom.ColorOrb

class ColorPickerDialogCustomFragment : Fragment() {


    /*--- Variable Declarations ---*/

    lateinit var theme: CrystalNoteTheme


    /*--- Lifecycle Methods ---*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        return inflater.inflate(R.layout.color_picker_dialog_custom_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theme = CrystalNoteTheme.fromCurrentTheme(requireContext())

        val customColorOrb = view.findViewById<ColorOrb>(R.id.colorOrbCustom)
        customColorOrb?.setPadding(R.dimen.colorCustomOrbCustomPadding)
        customColorOrb?.setBackdropColor(theme.colorBackground)
        customColorOrb?.setOutlineColor(theme.colorTextPrimary)
        customColorOrb?.enableForcedThickOutline()
        customColorOrb?.setColor(theme.colorTextTertiary)

        for (index in HISTORY_ORBS.indices) {
            val orb = view.findViewById<ColorOrb>(HISTORY_ORBS[index])
            orb?.setPadding(R.dimen.colorCustomOrbHistoryPadding)
            orb?.setBackdropColor(theme.colorBackground)
            orb?.setColor(theme.colorNoteBackground)
            orb?.setOutlineAlpha(0.0)
        }
    }


    /*--- Constants ---*/

    companion object {
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
