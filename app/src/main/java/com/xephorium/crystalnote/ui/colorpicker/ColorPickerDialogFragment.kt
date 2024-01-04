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
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.repository.SharedPreferencesRepository
import com.xephorium.crystalnote.data.utility.CrystalNoteToast
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerPagerAdapter
import com.xephorium.crystalnote.ui.colorpicker.view.ColorPickerTab


class ColorPickerDialogFragment(
    private val fragmentManager: FragmentManager
): DialogFragment(), ColorPickerDialogContract.View {


    /*--- Variable Declarations ---*/

    private var presenter: ColorPickerDialogPresenter = ColorPickerDialogPresenter()

    private lateinit var alertDialog: AlertDialog
    private lateinit var dialogView: View
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: ColorPickerPagerAdapter


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
        setupDialogTabListener()
        setupPresenter()

        return dialogView
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


    /*--- Public Setup Methods ---*/

    fun setTitle(title: String) {
        presenter.dialogTitle = title
    }

    fun setButtonText(text: String) {
        presenter.dialogButtonText = text
    }

    fun showDialog() {
        this.show(fragmentManager, null)
    }


    /*--- View Manipulation Methods ---*/

    override fun populateDialogViews(title: String, buttonText: String) {
        dialogView.findViewById<TextView>(R.id.textDialogTitle).text = title
        dialogView.findViewById<AppCompatButton>(R.id.buttonDialog).text = buttonText
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
        viewPager = dialogView.findViewById<ViewPager>(R.id.viewPagerColorPicker)
        adapter = ColorPickerPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupDialogTabListener() {
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                presenter.handleTabChange(ColorPickerTab.fromIndex(position))
            }
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPixels: Int) = Unit
        })
    }

    private fun setupPresenter() {
        presenter.sharedPreferencesRepository = SharedPreferencesRepository(requireContext())
    }
}