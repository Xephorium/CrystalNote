package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme
import com.xephorium.crystalnote.ui.extensions.getThemeColor

class NoteToolbar : Toolbar {


    /*--- Variable Declarations ---*/

    private var noteToolbarListener: NoteToolbarListener? = null

    var isEditMode: Boolean = false
        set(editMode) = if (editMode) {
            findViewById<EditText>(R.id.textToolbarEdit)?.visibility = View.VISIBLE
            findViewById<TextView>(R.id.textToolbarTitle)?.visibility = View.GONE
        } else {
            findViewById<EditText>(R.id.textToolbarEdit)?.visibility = View.GONE
            findViewById<TextView>(R.id.textToolbarTitle)?.visibility = View.VISIBLE
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
        this.noteToolbarListener = getDefaultNoteToolbarListener()

        findViewById<ColorOrb>(R.id.colorOrbToolbar).run {
            setPadding(R.dimen.toolbarColorOrbPadding)
            setBackdropColor(CrystalNoteTheme.fromCurrentTheme(context).colorToolbar)
            setOutlineColor(CrystalNoteTheme.fromCurrentTheme(context).colorToolbarTextSecondary)
            setOutlineAlpha(COLOR_ORB_ALPHA)
            setOnClickListener { noteToolbarListener?.onColorClick() }
        }

        findViewById<ImageView>(R.id.buttonToolbarLeft).setOnClickListener {
            noteToolbarListener?.onLeftButtonClick()
        }

        findViewById<ImageView>(R.id.buttonToolbarRight).setOnClickListener {
            noteToolbarListener?.onRightButtonClick()
        }

        findViewById<EditText>(R.id.textToolbarEdit).addTextChangedListener(object : TextWatcher {
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
        findViewById<TextView>(R.id.textToolbarTitle).text = resources.getText(stringResource)
    }

    fun setTitle(string: String) {
        findViewById<TextView>(R.id.textToolbarTitle).text = string
    }

    override fun getTitle(): CharSequence {
        return findViewById<TextView>(R.id.textToolbarTitle).text.toString()
    }

    fun setEditTextContent(content: String) {
        findViewById<EditText>(R.id.textToolbarEdit).setText(content)
    }

    fun getEditTextContent(): String {
        return findViewById<EditText>(R.id.textToolbarEdit).text.toString()
    }

    fun setLeftButtonImage(drawable: Int) {
        findViewById<ImageView>(R.id.buttonToolbarLeft).run {
            if (drawable == NO_IMAGE) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                setImageDrawable(resources.getDrawable(drawable, context.theme))
                setColorFilter(
                    context.getThemeColor(R.attr.themeToolbarTextSecondary),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                setLeftButtonScale(TOOLBAR_ICON_SCALE_SMALLER)
            }
        }
    }

    fun setLeftButtonScale(scale: Float) {
        findViewById<ImageView>(R.id.buttonToolbarLeft).run {
            scaleX = scale
            scaleY = scale
        }
    }

    fun showColor() {
        findViewById<ColorOrb>(R.id.colorOrbToolbar).visibility = View.VISIBLE
        findViewById<ColorOrb>(R.id.colorOrbToolbar).forceThickOutline()
    }

    fun hideColor() {
        findViewById<ColorOrb>(R.id.colorOrbToolbar).visibility = View.GONE
    }

    fun setColor(color: Int) {
        findViewById<ColorOrb>(R.id.colorOrbToolbar).setColor(color)
    }

    fun showRightButton() {
        findViewById<ImageView>(R.id.buttonToolbarRight).visibility = View.VISIBLE
    }

    fun hideRightButton() {
        findViewById<ImageView>(R.id.buttonToolbarRight).visibility = View.GONE
    }

    fun setNoteToolbarListener(noteToolbarListener: NoteToolbarListener) {
        this.noteToolbarListener = noteToolbarListener
    }


    /*--- Private Utility Methods ---*/

    private fun getDefaultNoteToolbarListener() = object : NoteToolbarListener {
        override fun onLeftButtonClick() {}
        override fun onRightButtonClick() {}
        override fun onColorClick() {}
        override fun onTextChange(text: String) {}
    }


    /*--- Action Handling Interface ---*/

    interface NoteToolbarListener {
        fun onLeftButtonClick()
        fun onRightButtonClick()
        fun onColorClick()
        fun onTextChange(text: String)
    }


    /*--- Constants ---*/

    companion object {
        private const val DEFAULT_LEFT_BUTTON_IMAGE = R.drawable.icon_back
        private const val COLOR_ORB_ALPHA = 0.68
        const val NO_IMAGE = -1
        const val TOOLBAR_ICON_SCALE_SMALLER = (0.93).toFloat()
    }
}
