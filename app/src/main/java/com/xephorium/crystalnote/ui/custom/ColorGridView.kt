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
            Color.parseColor("#ffffff"), // R1 - White
            Color.parseColor("#66b3ff"), //      Sky Blue
            Color.parseColor("#b3d9ff"), //      Sky Blue Bright
            Color.parseColor("#28bd4d"), //      Green
            Color.parseColor("#63e984"), //      Green Light
            Color.parseColor("#c6c6c6"), // R2 - White on Black Secondary
            Color.parseColor("#417dbe"), //      Azure
            Color.parseColor("#7aa4d1"), //      Azure Light
            Color.parseColor("#3d8f57"), //      Desaturated Green
            Color.parseColor("#4db36f"), //      Desaturated Green Light
            Color.parseColor("#666666"), // R3 - Black on White Secondary
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#555555"), // R4 - Dark Theme Background
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#353535"), // R5 - Black on White Primary
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#000000"), // R6 - Black
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#008a99"), //      Sage
            Color.parseColor("#12B4B8")  //      Sage Light
        )
    }
}