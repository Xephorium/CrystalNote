package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

import com.xephorium.crystalnote.R
import kotlinx.android.synthetic.main.note_toolbar_layout.view.*

class NoteToolbar : Toolbar {


    /*--- Variable Declarations ---*/

    private var noteToolbarListener: NoteToolbarListener? = null

    var isEditMode: Boolean = false
        set(editMode) = if (editMode) {
            textToolbarEdit.visibility = View.VISIBLE
            textToolbarTitle.visibility = View.GONE
        } else {
            textToolbarEdit.visibility = View.GONE
            textToolbarTitle.visibility = View.VISIBLE
        }


    /*--- Toolbar Setup ---*/

    constructor(context: Context) : super(context) {
        buildNoteToolbar(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        buildNoteToolbar(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        buildNoteToolbar(context)
    }

    private fun buildNoteToolbar(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        val noteToolbarLayout = layoutInflater.inflate(R.layout.note_toolbar_layout, null)
        this.addView(noteToolbarLayout)

        this.isEditMode = false
        this.setLeftButtonImage(DEFAULT_LEFT_BUTTON_IMAGE)
        this.setRightButtonImage(NO_IMAGE)
        this.noteToolbarListener = getDefaultNoteToolbarListener()

        findViewById<View>(R.id.buttonToolbarLeft).setOnClickListener {
            noteToolbarListener?.onLeftButtonClick()
        }
        findViewById<View>(R.id.buttonToolbarRight).setOnClickListener {
            noteToolbarListener?.onRightButtonClick()
        }
        (findViewById<View>(R.id.textToolbarEdit) as EditText).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                noteToolbarListener?.onTextChange(
                        (findViewById<View>(R.id.textToolbarEdit) as EditText).text.toString())
            }
        })
    }


    /*--- Public Methods ---*/

    override fun setTitle(stringResource: Int) {
        textToolbarTitle.text = resources.getText(stringResource)
    }

    override fun getTitle(): CharSequence {
        return textToolbarTitle.text
    }

    fun setEditTextContent(content: String) {
        textToolbarEdit.setText(content)
    }

    fun getEditTextContent(): String {
        return textToolbarEdit.toString()
    }

    fun setLeftButtonImage(drawable: Int) {
        if (drawable == NO_IMAGE) {
            buttonToolbarLeft.visibility = View.GONE
        } else {
            buttonToolbarLeft.visibility = View.VISIBLE
            buttonToolbarLeft.setImageDrawable(resources.getDrawable(drawable, context.theme))
        }
    }

    fun setRightButtonImage(drawable: Int) {
        if (drawable == NO_IMAGE) {
            buttonToolbarRight.visibility = View.GONE
        } else {
            buttonToolbarRight.visibility = View.VISIBLE
            buttonToolbarRight.setImageDrawable(resources.getDrawable(drawable, context.theme))
        }
    }

    fun setNoteToolbarListener(noteToolbarListener: NoteToolbarListener) {
        this.noteToolbarListener = noteToolbarListener
    }


    /*--- Private Utility Methods ---*/

    private fun getDefaultNoteToolbarListener() = object : NoteToolbarListener {
        override fun onLeftButtonClick() {}
        override fun onRightButtonClick() {}
        override fun onTextChange(text: String) {}
    }


    /*--- Action Handling Interface ---*/

    interface NoteToolbarListener {
        fun onLeftButtonClick()
        fun onRightButtonClick()
        fun onTextChange(text: String)
    }


    /*--- Constants ---*/

    companion object {
        private const val DEFAULT_LEFT_BUTTON_IMAGE = R.drawable.icon_back
        private const val DEFAULT_RIGHT_BUTTON_IMAGE = R.drawable.icon_save
        private const val NO_IMAGE = -1
    }
}
