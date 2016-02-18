package com.huyn.repo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Andy.fang on 2015/12/18.
 */
public class AnimTextView extends TextView {
    private int mSpaceH = 0;
    private int mSpaceV = 0;
    private int mSpeed = 0;
    public AnimTextView(Context context) {
        this(context, null);
    }
    public AnimTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimTextView);

        if (a.hasValue(R.styleable.AnimTextView_horizontal_space)) {
            this.mSpaceH = a.getDimensionPixelSize(R.styleable.AnimTextView_horizontal_space, 10);
        } else {
            this.mSpaceH = getResources().getDimensionPixelSize(R.dimen.animtext_horizontal_space);
        }

        if (a.hasValue(R.styleable.AnimTextView_vertical_space)) {
            this.mSpaceV = a.getDimensionPixelSize(R.styleable.AnimTextView_vertical_space, 10);
        } else {
            this.mSpaceV = getResources().getDimensionPixelSize(R.dimen.animtext_horizontal_space);
        }

        if (a.hasValue(R.styleable.AnimTextView_speed)) {
            this.mSpeed = a.getInt(R.styleable.AnimTextView_speed, 5);
        } else {
            this.mSpeed = 5;
        }

        String text = getText().toString();
        if(!TextUtils.isEmpty(text))
            initText();
        a.recycle();
    }

    private TextPaint paint;

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(paint == null)
            paint = getPaint();
        String txt = getText().toString();
        int size = txt.length();
        float width = paint.measureText("8")*size + mSpaceH*(size+1);
        float height = getMeasuredHeight() + 2*mSpaceV;

        int widthdSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);

        setMeasuredDimension(widthdSpec, heightSpec);
    }

    private AtomicBoolean mAnimating = new AtomicBoolean(false);
    private void initText() {
        String text = getText().toString();
        deltaY = 0;
        mAnimating.set(true);
        size = text.length();
        states = new boolean[size];

        paint = getPaint();

        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();

        topY = fontMetrics.top;

        fontWidth = paint.measureText("8");

        fontBase = Math.abs(topY) + mSpaceV;

        mText = new String[size];
        for(int i=0; i<size; i++) {
            mText[i] = text.substring(i, i + 1);
        }
    }

    private int size = 0;
    private String[] mText;
    private float topY;
    private float fontWidth;
    private float fontBase;
    private float deltaY = 0;
    private boolean[] states;
    public void onDraw(Canvas canvas) {
        float LEFT = getHeight()-fontBase;

        boolean mState = true;

        for(int i=0; i<size; i++) {
            mState &= states[i];
            String s = mText[i];
            int value = Integer.parseInt(s);
            int[] values = new int[value > 0 ? value+1 : 10];
            for(int j=0; j<values.length; j++)
                values[j] = j;
            if(value == 0) {
                values[values.length-1] = 0;
            }

            for(int j = 0; j<values.length; j++) {
                float y = fontBase + LEFT * j + deltaY;
                if(j == values.length-1 && y < fontBase) {
                    y = fontBase;
                }
                if(j == values.length-2 && y < 0) {
                    //stop it
                    states[i] = true;
                }
                canvas.drawText(values[j]+"", (i + 1) * mSpaceH + fontWidth * i, y, paint);
            }
        }

        deltaY -= mSpeed;
        if(mState) {
            mAnimating.set(false);
            if(mListener != null)
                mListener.onEnd();
        } else {
            invalidate();
        }
    }

    private IAnimEndListener mListener;
    public void startAnim(String text, IAnimEndListener listener) {
        if(mAnimating.get())
            return;
        if(TextUtils.isEmpty(text))
            return;
        this.mListener = listener;
        setText(text);
        initText();
        invalidate();
    }

    public interface IAnimEndListener {
        public void onEnd();
    }

}
