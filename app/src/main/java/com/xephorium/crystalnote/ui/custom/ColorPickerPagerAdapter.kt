package com.xephorium.crystalnote.ui.custom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ColorPickerPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return TAB_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColorPickerDialogPaletteFragment()
            else -> ColorPickerDialogCustomFragment()
        }

//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Palette"
            else -> "Custom"
        }
    }

    companion object {
        const val TAB_COUNT = 2
    }

}