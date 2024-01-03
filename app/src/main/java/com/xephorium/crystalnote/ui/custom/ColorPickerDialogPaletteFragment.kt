package com.xephorium.crystalnote.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xephorium.crystalnote.R

class ColorPickerDialogPaletteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        return inflater.inflate(R.layout.color_picker_dialog_palette_layout, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
////            val textView: TextView = view.findViewById(R.id.testText)
////            textView.text = getInt(ARG_OBJECT).toString()
////        }
//
//        val textView: TextView = view.findViewById(R.id.testText)
//        textView.text = "Test Text 77"
//    }
//    companion object {
//        const val ARG_OBJECT = "object"
//    }
}
