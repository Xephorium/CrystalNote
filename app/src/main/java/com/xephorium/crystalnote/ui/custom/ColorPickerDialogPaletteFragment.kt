package com.xephorium.crystalnote.ui.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme

class ColorPickerDialogPaletteFragment : Fragment() {


    /*--- Variable Declarations ---*/

    lateinit var theme: CrystalNoteTheme

    private var selectedOrb: ColorOrb? = null


    /*--- Lifecycle Methods ---*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        return inflater.inflate(R.layout.color_picker_dialog_palette_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theme = CrystalNoteTheme.fromCurrentTheme(requireContext())

        for (index in ORB_RESOURCES.indices) {
            val orb = view.findViewById<ColorOrb>(ORB_RESOURCES[index])
            orb?.setPadding(R.dimen.colorPickerPaddingOrb)
            orb?.setBackdropColor(theme.colorBackground)
            orb?.setColor(ORB_COLORS[index])
            orb?.setOnClickListener {
                selectedOrb?.disableForcedThickOutline()
                selectedOrb = orb
                orb.enableForcedThickOutline()
            }
        }
    }


    /*--- Constants ---*/

    companion object {
        private val ORB_RESOURCES = listOf(
            R.id.colorOrb1,
            R.id.colorOrb2,
            R.id.colorOrb3,
            R.id.colorOrb4,
            R.id.colorOrb5,
            R.id.colorOrb6,
            R.id.colorOrb7,
            R.id.colorOrb8,
            R.id.colorOrb9,
            R.id.colorOrb10,
            R.id.colorOrb11,
            R.id.colorOrb12,
            R.id.colorOrb13,
            R.id.colorOrb14,
            R.id.colorOrb15,
            R.id.colorOrb16,
            R.id.colorOrb17,
            R.id.colorOrb18,
            R.id.colorOrb19,
            R.id.colorOrb20,
            R.id.colorOrb21,
            R.id.colorOrb22,
            R.id.colorOrb23,
            R.id.colorOrb24,
            R.id.colorOrb25,
            R.id.colorOrb26,
            R.id.colorOrb27,
            R.id.colorOrb28,
            R.id.colorOrb29,
            R.id.colorOrb30,
            R.id.colorOrb31,
            R.id.colorOrb32,
            R.id.colorOrb33,
            R.id.colorOrb34,
            R.id.colorOrb35,
            R.id.colorOrb36,
            R.id.colorOrb37
        )
        private val ORB_COLORS = listOf(
            Color.parseColor("#1a918b"), // R1 ---
            Color.parseColor("#129fb4"), //
            Color.parseColor("#09b0e8"), //
            Color.parseColor("#0774e8"), //
            Color.parseColor("#1545f6"), //
            Color.parseColor("#333ae3"), //
            Color.parseColor("#4e3faf"), //
            Color.parseColor("#10c358"), // R2 ---
            Color.parseColor("#14f499"), //
            Color.parseColor("#49fce1"), //
            Color.parseColor("#62ddfe"), //
            Color.parseColor("#78b6ff"), //
            Color.parseColor("#8a8bff"), //
            Color.parseColor("#8740fb"), //
            Color.parseColor("#9013ee"), //
            Color.parseColor("#3aee38"), // R3 ---
            Color.parseColor("#83ff90"), //
            Color.parseColor("#c2ffcd"), //
            Color.parseColor("#e8e8e8"), //
            Color.parseColor("#ebadf6"), //
            Color.parseColor("#eb6df6"), //
            Color.parseColor("#e81ef2"), //
            Color.parseColor("#539d0f"), // R4 ---
            Color.parseColor("#9cf83c"), //
            Color.parseColor("#d9fe79"), //
            Color.parseColor("#fde6a1"), //
            Color.parseColor("#ffa9a1"), //
            Color.parseColor("#ff68a0"), //
            Color.parseColor("#f23ca7"), //
            Color.parseColor("#bd1c97"), //
            Color.parseColor("#a7ad25"), // R5 ---
            Color.parseColor("#e1e013"), //
            Color.parseColor("#fac823"), //
            Color.parseColor("#fa862f"), //
            Color.parseColor("#fd5332"), //
            Color.parseColor("#e11950"), //
            Color.parseColor("#951a53")  //
        )
    }
}
