package com.xephorium.crystalnote.ui.colorpicker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xephorium.crystalnote.R


class ColorPickerDialogFragment(private val fragmentManager: FragmentManager): DialogFragment() {


    /*--- Variable Declarations ---*/

    private lateinit var alertDialog: AlertDialog
    private lateinit var dialogView: View

    private var title: String = ""
    private var buttonText: String = ""


    /*--- Lifecycle Methods ---*/

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        initializeDialogView()
        initializeAlertDialog()
        setupDialogBackground()
        enableSoftKeyboard()

        return alertDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View {
        setupDialogTabs()

        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateViews()
    }


    /*--- Public Methods ---*/

    fun showDialog() {
        this.show(fragmentManager, null)
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setButtonText(text: String) {
        buttonText = text
    }

    /*--- View Initialization Methods ---*/

    private fun initializeDialogView() {
        dialogView = requireActivity().layoutInflater.inflate(
            R.layout.color_picker_dialog_layout,
            null
        )
    }

    private fun initializeAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        alertDialog = builder.create()
    }

    private fun setupDialogBackground() {
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun enableSoftKeyboard() {
        dialogView.post {
            alertDialog.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            )
            alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
    }

    private fun setupDialogTabs() {
        val tabLayout = dialogView.findViewById<TabLayout>(R.id.tabLayoutColorPicker)
        val viewPager = dialogView.findViewById<ViewPager>(R.id.viewPagerColorPicker)
        val adapter = ColorPickerPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }


    /*--- Dialog Population Methods ---*/

    private fun populateViews() {
        dialogView.findViewById<TextView>(R.id.textDialogTitle).text = title
        dialogView.findViewById<AppCompatButton>(R.id.buttonDialog).text = buttonText
    }
}