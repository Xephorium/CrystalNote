package com.xephorium.crystalnote.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

import com.xephorium.crystalnote.R;

public class LineEditText extends EditText {

    public static final ColorDrawable TRANSPARENT = new ColorDrawable(0x00FFFFFF);
    public static final int           INPUT_TYPE   = InputType.TYPE_CLASS_TEXT |
                                                     InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                                                     InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;

    private Context context;
    private Paint paint;

    public LineEditText(Context context) {
        super(context);
        initializeLineEditText(context);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeLineEditText(context);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeLineEditText(context);
    }

    private void initializeLineEditText(Context context) {
        this.context = context;
        this.paint = new Paint();
        this.setBackground(TRANSPARENT);
        this.setGravity(Gravity.TOP);
        this.setInputType(INPUT_TYPE);
        this.setScrollContainer(true);
        this.setVerticalScrollBarEnabled(false);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.textUnderline));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewHeight = getHeight() > computeVerticalScrollRange() ? getHeight() : computeVerticalScrollRange();
        int numLines = (viewHeight - getPaddingTop() - getPaddingBottom()) / getLineHeight();

        for (int x = 0; x < numLines + 1; x++) {
            int lineYPos = getLineHeight() * (x + 1) + getPaddingTop();
            canvas.drawLine(getLeft() + getPaddingLeft(), lineYPos, getRight() - getPaddingRight(), lineYPos, paint);
        }
        super.onDraw(canvas);
    }
}