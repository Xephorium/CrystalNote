package com.xephorium.crystalnote.ui.colorpicker.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.xephorium.crystalnote.ui.colorpicker.model.PreciseColor
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerDialogCustomFragment.Companion.ColorPickerCustomListener
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerDialogPaletteFragment.Companion.ColorPickerPaletteListener

class ColorPickerPagerAdapter(
    fragmentManager: FragmentManager,
    private val paletteListener: ColorPickerPaletteListener,
    private val customListener: ColorPickerCustomListener
) : FragmentPagerAdapter(fragmentManager) {


    /*--- Variable Declarations ---*/

    private var paletteFragment: ColorPickerDialogPaletteFragment? = null
    private var customFragment: ColorPickerDialogCustomFragment? = null


    /*--- PagerAdapter Overrides ---*/

    override fun getCount(): Int {
        return ColorPickerTab.size()
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                paletteFragment = ColorPickerDialogPaletteFragment(paletteListener)
                paletteFragment!!
            }
            else -> {
                customFragment = ColorPickerDialogCustomFragment(customListener)
                customFragment!!
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ColorPickerTab.fromIndex(position).displayName
    }


    /*--- View Manipulation Methods ---*/

    fun setCustomColor(color: PreciseColor) {
        customFragment?.setCustomColor(color)
    }

    fun notifyTabChange(tab: ColorPickerTab) {
        if (tab == ColorPickerTab.CUSTOM) customFragment?.notifyVisible()
    }
}