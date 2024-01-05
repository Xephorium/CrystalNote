package com.xephorium.crystalnote.ui.colorpicker.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerDialogPaletteFragment.Companion.ColorPickerPaletteListener

class ColorPickerPagerAdapter(
    fragmentManager: FragmentManager,
    private val paletteListener: ColorPickerPaletteListener
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return ColorPickerTab.size()
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColorPickerDialogPaletteFragment(paletteListener)
            else -> ColorPickerDialogCustomFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ColorPickerTab.fromIndex(position).displayName
    }
}