package com.xephorium.crystalnote.ui.colorpicker.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ColorPickerPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return ColorPickerTab.size()
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColorPickerDialogPaletteFragment()
            else -> ColorPickerDialogCustomFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ColorPickerTab.fromIndex(position).displayName
    }
}