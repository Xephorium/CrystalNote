package com.xephorium.crystalnote.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout.LayoutParams.*
import android.widget.LinearLayout
import com.xephorium.crystalnote.ui.custom.ColorPickerDialog.Companion.ColorPickerListener
import com.xephorium.crystalnote.R
import com.xephorium.crystalnote.data.model.CrystalNoteTheme


class ColorGridView: LinearLayout {


    /*--- Variable Declarations ---*/

    var theme = CrystalNoteTheme.fromCurrentTheme(context)

    val paddingGridTop = getDimensionPx(R.dimen.paddingActivity)
    var paddingGridSide = getAttributePx(android.R.attr.dialogPreferredPadding)
    val paddingGridBottom = getDimensionPx(R.dimen.colorPickerPaddingBottom)

    var colorOrbs = mutableListOf<ColorOrb>()
    var orbSize = ORB_SIZE_INITIAL
    var listener: ColorPickerListener? = null

    val columns = GRID_COLUMNS
    val rows = COLOR_OPTIONS.size / columns


    /*--- Constructors ---*/

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : this(context, null)

    init {
        this.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        this.setPadding(paddingGridSide, paddingGridTop, paddingGridSide, paddingGridBottom)
        this.orientation = VERTICAL
        this.gravity = Gravity.TOP

        populateOrbGrid()
    }


    /*--- Lifecycle Methods ---*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Calculate Orb Sizes & Adjust ColorGridView Side Padding
        if (orbSize == ORB_SIZE_INITIAL) {

            // Get Row Width in Pixels
            val width = findViewById<LinearLayout>(R.id.colorGridRow).width

            // Calculate Orb Sizes at Current Row Width
            orbSize = width / columns

            // Update ColorGridView Side Padding
            val offset = ((orbSize * ORB_PADDING) / 2.0).toInt()
            paddingGridSide -= offset
            this.setPadding(paddingGridSide, paddingGridTop, paddingGridSide, paddingGridBottom)

            // Calculate Orb Sizes at New Row Width
            orbSize = (width + (offset * 2)) / columns

            // Set Final Orb Sizes
            for (orb in colorOrbs) {
                orb.setSizeByPixelValue(orbSize)
            }
        }
    }


    /*--- Public Methods ---*/

    fun setColorPickerListener(listener: ColorPickerListener) {
        this.listener = listener
    }


    /*--- Private Methods ---*/

    private fun populateOrbGrid() {
        var colorIndex = 0

        // For Each Row
        repeat(rows) {

            // Create Row
            val rowLayout = LinearLayout(context)
            rowLayout.id = R.id.colorGridRow
            rowLayout.orientation = HORIZONTAL
            rowLayout.gravity = Gravity.CENTER_HORIZONTAL

            // For Each Column
            repeat(columns) {

                // Create Orb
                val orb = ColorOrb(context)
                orb.setTheme(theme)
                orb.setColor(COLOR_OPTIONS[colorIndex++])
                orb.setPadding(ORB_PADDING)
                orb.showContrastOutline(ORB_OUTLINE)
                orb.setOnClickListener { listener?.onColorSelect(orb.getColor()) }

                // Add Orb to Row
                rowLayout.addView(orb)
                colorOrbs.add(orb)
            }

            this.addView(rowLayout)
        }
    }

    private fun getAttributePx(id: Int): Int {
        val styledAttributes = context.obtainStyledAttributes(TypedValue().data, intArrayOf(id))
        val value = styledAttributes.getDimensionPixelSize(0, -1)
        styledAttributes.recycle()
        return value
    }

    private fun getDimensionPx(id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }


    /*--- Constants ---*/

    companion object {
        private const val ORB_SIZE_INITIAL = 0
        private const val GRID_COLUMNS = 5
        private const val ORB_PADDING = 0.15
        private const val ORB_OUTLINE = true

        private val COLOR_OPTIONS = listOf(
            Color.parseColor("#E6E6FA"),
            Color.parseColor("#FA8072"),
            Color.parseColor("#8B4513"),
            Color.parseColor("#20B2AA"),
            Color.parseColor("#00FFFF"),
            Color.parseColor("#00BFFF"),
            Color.parseColor("#8B008B"),
            Color.parseColor("#F0F8FF"),
            Color.parseColor("#778899"),
            Color.parseColor("#F5DEB3"),
            Color.parseColor("#D3D3D3"),
            Color.parseColor("#FFFACD"),
            Color.parseColor("#48D1CC"),
            Color.parseColor("#00008B"),
            Color.parseColor("#000000"),
            Color.parseColor("#FF4500"),
            Color.parseColor("#FFFFF0"),
            Color.parseColor("#FFFFE0"),
            Color.parseColor("#FF00FF"),
            Color.parseColor("#C71585"),
            Color.parseColor("#008B8B"),
            Color.parseColor("#FF7F50"),
            Color.parseColor("#F5F5F5"),
            Color.parseColor("#DB7093"),
            Color.parseColor("#8FBC8F"),
            Color.parseColor("#FFFAFA"),
            Color.parseColor("#FF00FF"),
            Color.parseColor("#E9967A"),
            Color.parseColor("#BA55D3"),
            Color.parseColor("#0000FF")
        )
    }
}