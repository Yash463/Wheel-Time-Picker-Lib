package com.customeview.wheeltimepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by yashwant.singh on 7/13/2017.
 */

public class CircleTimerView extends View {

    private Paint mCirclePaint, mMinHourSticksPaint, mCenterTextPaint, mNumberTextPaint, mBackgroundPaint = null;
    private boolean isFirstTimeDraw = false;
    private int cx, cy, radius, innerCircleRadius, usableWidth, usableHeight = 0;
    private float startAngle , rotateAngle , savedLastRotatedAngle = 0;//by default
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();
    public static final int HOUR_TYPE = 1, MINUTE_TYPE = 2, SECOND_TYPE = 3;
    int selectedViewType = HOUR_TYPE;
    private WheelTimePickerView.OnRotationChange rotationChangeListener = null;
    private int numeralTextSize = 15;
    private int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
    private int backGroundColor = DEFAULT_BACKGROUND_COLOR;

    public CircleTimerView(Context context , int viewType,
                           WheelTimePickerView.OnRotationChange rotationChangeListener,
                           int numeralTextSize , int backGroundColor) {
        super(context);
        this.selectedViewType = viewType;
        this.rotationChangeListener = rotationChangeListener;
        this.numeralTextSize = numeralTextSize;
        this.backGroundColor = backGroundColor;
        this.init();
    }

    public CircleTimerView(Context context) {
        super(context);
        this.init();
    }

    public CircleTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public CircleTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init(){
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backGroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mMinHourSticksPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinHourSticksPaint.setColor(Color.BLACK);
        mMinHourSticksPaint.setStyle(Paint.Style.STROKE);
        mMinHourSticksPaint.setStrokeWidth(2);

        /*int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, numeralTextSize,
                getResources().getDisplayMetrics());*/
        int fontSize = numeralTextSize;
        mNumberTextPaint = new Paint();
        mNumberTextPaint.setColor(Color.BLACK);
        mNumberTextPaint.setTextSize(fontSize);

        mCenterTextPaint = new Paint();
        mCenterTextPaint.setColor(Color.BLACK);
        mCenterTextPaint.setTextAlign(Paint.Align.CENTER);
        mCenterTextPaint.setTextSize(fontSize);
    }

    private void initVariablesOnDraw(){
        int mHeight = this.getHeight();
        int mWidth = this.getWidth();

        int pl = this.getPaddingLeft();
        int pr = this.getPaddingRight();
        int pt = this.getPaddingTop();
        int pb = this.getPaddingBottom();

        usableWidth = mWidth - (pl + pr);
        usableHeight = mHeight - (pt + pb);

        radius = Math.min(usableWidth, usableHeight) / 2;
        //innerCircleRadius = (int) (radius * .60);
        cx = pl + (usableWidth / 2);
        cy = pt + (usableHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        if(!isFirstTimeDraw) {
            isFirstTimeDraw = true;
            this.initVariablesOnDraw();
        }

        //canvas.drawText(this.getTime(rotateAngle), canvas.getWidth() / 2, canvas.getHeight() / 2, mCenterTextPaint);
        //canvas.drawColor(backGroundColor);
        if(selectedViewType == HOUR_TYPE) {
            canvas.drawCircle(cx, cy, radius-5, mBackgroundPaint);
            canvas.drawLine(cx, cy, cx, cy - radius, mMinHourSticksPaint);
        }
        canvas.drawCircle(cx, cy, radius-5, mCirclePaint);
        rotateView(canvas);
        if(selectedViewType == HOUR_TYPE) {
            drawHourNumeral(canvas);
        }else {
            drawMinuteNumeral(canvas);
        }
        canvas.restore();
        if(rotationChangeListener != null){
            rotationChangeListener.onRotation(rotateAngle);
        }
        super.onDraw(canvas);
    }

    private void rotateView(Canvas canvas){
        canvas.rotate(rotateAngle , radius, radius);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAngle = getCalculatedAngle(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                rotateAngle = getCalculatedAngle(event.getX(), event.getY());
                rotateAngle = rotateAngle - startAngle + savedLastRotatedAngle;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(selectedViewType == HOUR_TYPE){
                    float modulusDegreeFromRatedAngle = rotateAngle % 30;
                    rotateAngle = rotateAngle + modulusDegreeFromRatedAngle * -1;
                    if(Math.abs(modulusDegreeFromRatedAngle) >= 15){
                        if(rotateAngle < 0){
                            rotateAngle = rotateAngle - 30;
                        }else{
                            rotateAngle = rotateAngle + 30;
                        }
                    }
                    this.invalidate();
                }
                savedLastRotatedAngle = rotateAngle;
                break;
        }
        return true;
    }

    private int getCalculatedAngle(float getX, float getY) {
        double tx;
        double ty;
        int x = (int) getX;
        int y = (int) getY;
        tx = x - (usableWidth / 2);
        ty = y - (usableHeight / 2);
        double angleInDegrees = Math.atan2(ty, tx) * 180 / Math.PI;
        int ACTUAL_ANGLE = 270;
        if (angleInDegrees < 0 && angleInDegrees < -90) {// Need to add
            // 270+angle degrees)
            ACTUAL_ANGLE += (int) (180 + angleInDegrees);
        } else if (angleInDegrees < 0 && angleInDegrees > -90) {// Need to add
            // 90+angle
            // degrees)
            ACTUAL_ANGLE = (int) (90 + angleInDegrees);
        } else if (angleInDegrees > 0)
            ACTUAL_ANGLE = 90 + (int) angleInDegrees;
        return ACTUAL_ANGLE;
    }

    private void drawHourNumeral(Canvas canvas) {
        for (int number : numbers) {
            String tmp = String.valueOf(number);
            mNumberTextPaint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (usableWidth / 2 + Math.cos(angle) * (radius - 40) - rect.width() / 2);
            int y = (int) (usableHeight / 2 + Math.sin(angle) * (radius - 40) + rect.height() / 2);
            canvas.drawText(tmp, x, y, mNumberTextPaint);
        }
    }

    private void drawMinuteNumeral(Canvas canvas) {
        for (int number : numbers) {
            String tmp = String.valueOf(((number * 5)%60));
            mNumberTextPaint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (usableWidth / 2 + Math.cos(angle) * (radius - 40) - rect.width() / 2);
            int y = (int) (usableHeight / 2 + Math.sin(angle) * (radius - 40) + rect.height() / 2);
            canvas.drawText(tmp, x, y, mNumberTextPaint);
        }
    }

    /*private boolean isInnerCircleTouch(float dx,float dy){
        return (((dx - cx) *  (dx - cx)) + ((dy - cy) * (dy - cy))) <= (innerCircleRadius * innerCircleRadius);
    }*/
}