package com.customeview.wheeltimepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by yashwant.singh on 7/14/2017.
 */

public class WheelTimePickerView extends RelativeLayout {

    private Context mContext = null;
    private boolean isInit = false;
    private float hourRotatedAngle, minuteRotatedAngle = 0;
    private TextView mTextView = null;
    private final int DEFAULT_NUMERAL_TEXT_SIZE = 50 , DEFAULT_TIMER_TEXT_SIZE = 30;
    private int middleTimerTextSize = DEFAULT_TIMER_TEXT_SIZE;
    private int numeralTextSize = DEFAULT_NUMERAL_TEXT_SIZE;
    private OnTimeChangeListener timeChangeListener = null;
    private int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private int backGroundColor = DEFAULT_BACKGROUND_COLOR;
    private int viewHeight , viewWidth = 0;

    public interface OnRotationChange{
        void onRotation(float rotatedAngle);
    }

    public interface OnTimeChangeListener{
        void onTimeChange(String hour, String minutes);
    }

    public WheelTimePickerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public WheelTimePickerView(Context context , int height , int width) {
        super(context);
        this.mContext = context;
        this.viewHeight = height;
        this.viewWidth = width;
    }

    public WheelTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.initializeAttributes(context , attrs);
    }

    public WheelTimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.initializeAttributes(context , attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WheelTimePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        this.initializeAttributes(context , attrs);
    }

    private void initializeAttributes(Context context , AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WheelTimePickerView, 0, 0);
        try{
            middleTimerTextSize = (int) ta.getDimension(R.styleable.WheelTimePickerView_timerTextSize , 0);
            if(middleTimerTextSize <= 0){
                middleTimerTextSize = DEFAULT_TIMER_TEXT_SIZE;
            }

            numeralTextSize = (int) ta.getDimension(R.styleable.WheelTimePickerView_numeralTextSize , 0);
            if(numeralTextSize <= 0){
                numeralTextSize = DEFAULT_NUMERAL_TEXT_SIZE;
            }

            backGroundColor = ta.getColor(R.styleable.WheelTimePickerView_backGroundColor, 0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ta.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.initView(mContext);
        super.onDraw(canvas);
    }

    private void initView(Context mContext){
        if(isInit) {
            return ;
        }
        isInit = true;

        this.mContext = mContext;
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if(this.viewHeight > 0 && this.viewWidth > 0){
            viewWidth = this.viewWidth;
            viewHeight = this.viewHeight;
        }

        this.addWheelTimePicker(viewWidth , viewHeight , CircleTimerView.HOUR_TYPE , 0);
        this.addWheelTimePicker((int) (viewWidth * .60), (int) (viewHeight * .60), CircleTimerView.MINUTE_TYPE , 1);
        this.drawTimeTextInCenter(2);
    }

    private void addWheelTimePicker(int width , final int height , final int wheelViewType , int parentViewPosition){
        CircleTimerView wheelTimePicker = new CircleTimerView(mContext, wheelViewType, new OnRotationChange() {
            @Override
            public void onRotation(float rotatedAngle) {
                if(wheelViewType == CircleTimerView.HOUR_TYPE){
                    hourRotatedAngle = rotatedAngle;
                }else{
                    minuteRotatedAngle = rotatedAngle;
                }
                changeRotationTime(hourRotatedAngle , minuteRotatedAngle);
            }
        }, numeralTextSize , backGroundColor);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width , height);
        if(wheelViewType != CircleTimerView.HOUR_TYPE)
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        wheelTimePicker.setLayoutParams(lp);
        this.addView(wheelTimePicker , parentViewPosition);
    }

    private void drawTimeTextInCenter(int parentViewPosition ){
        mTextView = new TextView(mContext);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mTextView.setLayoutParams(lp);
        mTextView.setText("12:00");
       /* int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, middleTimerTextSize,
                getResources().getDisplayMetrics());*/
        int fontSize = middleTimerTextSize;
        mTextView.setTextSize(fontSize);
        mTextView.setTextColor(Color.BLACK);
        this.addView(mTextView , parentViewPosition);
    }

    private void changeRotationTime(float hourRotatedAngle , float minuteRotatedAngle){
        if(mTextView != null){
            String hour = this.getHourTimeFromRotatedDegree(hourRotatedAngle);
            String minute = this.getMinuteTimeFromRotatedDegree(minuteRotatedAngle);
            mTextView.setText(hour + ":" + minute);
            if(timeChangeListener != null){
                timeChangeListener.onTimeChange(hour , minute);
            }
        }
    }

    private String getHourTimeFromRotatedDegree(float degree){
        if(degree < 0){
            degree = (degree * -1) / 30;
            int devidedValue = (int) (degree / 12);
            degree = degree - devidedValue * 12;
        }else{
            degree = (degree) / 30;
            degree = 12 - degree;
        }
        if(degree < 0){
            degree = degree + 12;
        }
        return formatDoubleDigit((int) degree);
    }

    private String getMinuteTimeFromRotatedDegree(float degree){
        if(degree < 0){
            degree = (degree * -1) / 30;
            int devidedValue = (int) (degree / 12);
            degree = degree - devidedValue * 12;
        }else{
            degree = (degree) / 30;
            degree = 12 - degree;
        }
        degree = (degree * 5) % 60;
        if(degree < 0){
            degree = degree + 60;
        }
        return formatDoubleDigit((int) degree);
    }

    private String formatDoubleDigit(int digit){
        return String.format("%02d", digit);
    }

    public void setTimeChangeListener(OnTimeChangeListener timeChangeListener){
        this.timeChangeListener = timeChangeListener;
    }


    //draw view dynamically
    public void setTimerTextSize(int textSize){
        this.middleTimerTextSize = textSize;
    }

    public void setNumeralTestSize(int texSize){
        this.numeralTextSize = texSize;
    }

    public void setViewBackGroundColor(int color){
        this.backGroundColor = color;
    }

    public void drawView(){
        this.initView(mContext);
    }
}
