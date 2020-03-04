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
        private const val GRID_COLUMNS = 7
        private const val ORB_PADDING = 0.15
        private const val ORB_OUTLINE = true

        private val COLOR_OPTIONS = listOf(
            Color.parseColor("#ffffff"), // R1 ---                (White)
            Color.parseColor("#576775"), //    Blue Smoke Dark
            Color.parseColor("#899aa9"), //    Blue Smoke
            Color.parseColor("#d3d9df"), //    Blue Smoke Light
            Color.parseColor("#caa8f0"), //    Barney Light
            Color.parseColor("#a882d3"), //    Barney
            Color.parseColor("#856aa4"), //    Barney Dark
            Color.parseColor("#cccccc"), // R2 ---
            Color.parseColor("#426080"), //    Azure Dark
            Color.parseColor("#3F7AB9"), //    Azure
            Color.parseColor("#7aa4d1"), //    Azure Light
            Color.parseColor("#d6a9c0"), //    Thanos Light
            Color.parseColor("#a7728d"), //    Thanos
            Color.parseColor("#895d74"), //    Thanos Dark
            Color.parseColor("#a3a3a3"), // R3 ---
            Color.parseColor("#316c72"), //    Sage Dark
            Color.parseColor("#008999"), //    Sage
            Color.parseColor("#12B4B8"), //    Sage Light
            Color.parseColor("#ce8d88"), //    Tomato Light
            Color.parseColor("#b5615a"), //    Tomato
            Color.parseColor("#9d5148"), //    Tomato Dark
            Color.parseColor("#7a7a7a"), // R4 ---
            Color.parseColor("#53795e"), //    Green Dark
            Color.parseColor("#4b9b63"), //    Green
            Color.parseColor("#98cdaa"), //    Green Light
            Color.parseColor("#ec9393"), //    Red Light
            Color.parseColor("#d35050"), //    Red
            Color.parseColor("#b63535"), //    Red Dark
            Color.parseColor("#555555"), // R5 ---                (Dark Theme Background)
            Color.parseColor("#627934"), //    Olive Dark
            Color.parseColor("#94b649"), //    Olive
            Color.parseColor("#cce29c"), //    Olive Light
            Color.parseColor("#fbb065"), //    Orange Light
            Color.parseColor("#ef9439"), //    Orange
            Color.parseColor("#da8c3e"), //    Orange Dark
            Color.parseColor("#343434"), // R6 ---
            Color.parseColor("#bebe5b"), //    Yellow Dark
            Color.parseColor("#dede63"), //    Yellow
            Color.parseColor("#eeee8c"), //    Yellow Light
            Color.parseColor("#f9d36c"), //    Gold Light
            Color.parseColor("#f7c53b"), //    Gold
            Color.parseColor("#dfac20"), //    Gold Dark
            Color.parseColor("#000000"), // R7 ---                (Black)
            Color.parseColor("#adad85"), //    Parchment Dark
            Color.parseColor("#d7d7b7"), //    Parchment
            Color.parseColor("#e9e9d8"), //    Parchment Light
            Color.parseColor("#c6b9a9"), //    Coffee Light
            Color.parseColor("#ad9b85"), //    Coffee
            Color.parseColor("#5d554c")  //    Coffee Dark
        )

        private val COLOR_OPTIONS_OLD = listOf(
            Color.parseColor("#ffffff"), // R1 - White
            Color.parseColor("#7aa4d1"), //      Azure Light
            Color.parseColor("#66b3ff"), //      Azure Saturated
            Color.parseColor("#12B4B8"), //      Sage Light
            Color.parseColor("#8cd9bf"), //      Green Light
            Color.parseColor("#64b47f"), //      Desaturated Green Light
            Color.parseColor("#00555555"),
            Color.parseColor("#c6c6c6"), // R2
            Color.parseColor("#345679"), //      Azure Dark
            Color.parseColor("#3F7AB9"), //      Azure
            Color.parseColor("#008a99"), //      Sage
            Color.parseColor("#40bf95"), //      Green
            Color.parseColor("#4b9b63"), //      Desaturated Green
            Color.parseColor("#00555555"),
            Color.parseColor("#777777"), // R3
            Color.parseColor("#f18f8d"), //      Lightish Red
            Color.parseColor("#f47762"), //      Tomato Light
            Color.parseColor("#f6a655"), //      Orange Light
            Color.parseColor("#f9d36c"), //      Gold Light
            Color.parseColor("#e6e689"), //      Yellow Light
            Color.parseColor("#00555555"),
            Color.parseColor("#555555"), // R4 - Dark Theme Background
            Color.parseColor("#B3564D"), //      Tomato
            Color.parseColor("#d6514b"), //      Red
            Color.parseColor("#d2802d"), //      Orange Dark
            Color.parseColor("#dfac20"), //      Gold Dark
            Color.parseColor("#bfbf40"), //      Yellow Dark
            Color.parseColor("#00555555"),
            Color.parseColor("#333333"), // R5
            Color.parseColor("#d3d9df"), //      Blue Smoke Light
            Color.parseColor("#c6b9a9"), //      Coffee Light
            Color.parseColor("#c0add2"), //      Barney Light
            Color.parseColor("#d6a9c0"), //      Thanos Light
            Color.parseColor("#dbdbbd"), //      Parchment Light
            Color.parseColor("#00555555"),
            Color.parseColor("#000000"), // R6 - Black
            Color.parseColor("#576775"), //      Blue Smoke Dark
            Color.parseColor("#5d554c"), //      Coffee Dark
            Color.parseColor("#8c70a9"), //      Barney Dark
            Color.parseColor("#996680"), //      Thanos Dark
            Color.parseColor("#adad85"), //      Parchment Dark
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555"),
            Color.parseColor("#00555555")
        )
    }
}