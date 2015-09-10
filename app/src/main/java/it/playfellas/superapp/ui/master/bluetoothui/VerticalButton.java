package it.playfellas.superapp.ui.master.bluetoothui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

/**
 * Created by Stefano Cappa on 10/09/15.
 */
public class VerticalButton extends Button {
    public static final int UP = 0;
    public static final int RIGHT = 0;
    public static final int DOWN = 0;
    public static final int LEFT = 0;

    final boolean topDown;

    public VerticalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM)) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = true;
        } else
            topDown = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (topDown) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        getLayout().draw(canvas);
        canvas.restore();
    }
}