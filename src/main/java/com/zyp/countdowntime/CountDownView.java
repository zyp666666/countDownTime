package com.zyp.countdowntime;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zyp on 2018/6/19 0019 下午 2:52
 */

public class CountDownView extends View {

    private int mInputColor;
    private float mInputSize;
    private int mWidthMeasureSpecSize;
    private int mHeightMeasureSpecSize;
    private long mRealTime;
    private Paint mPaint;
//    private long oldTime;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mRealTime > 0 ){
                mRealTime--;

                invalidate();
//                oldTime = newTime;
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    public CountDownView(Context context) {
        this(context,null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        String inputTime = typedArray.getString(R.styleable.CountDownView_time);
        mInputColor = typedArray.getColor(R.styleable.CountDownView_textcolor, getResources().getColor(R.color.colorPrimary));
        mInputSize = typedArray.getDimension(R.styleable.CountDownView_textsize, 40);

        /**
         * 将输入的数值重新拼接
         */
        mRealTime = Long.parseLong(getTime(inputTime));
        
        typedArray.recycle();
        initTool();
    }

    private void initTool() {
        mPaint = new Paint();
        mPaint.setTextSize(mInputSize);
        mPaint.setColor(mInputColor);
        mPaint.setStrokeWidth(0);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setAntiAlias(true);

//        oldTime = System.currentTimeMillis();
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        /**
         * 测量得到的宽
         */
        mWidthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        
        if (widthMeasureSpecMode == MeasureSpec.AT_MOST && heightMeasureSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200,60);
        }else if (widthMeasureSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200, mHeightMeasureSpecSize);
        }else if (heightMeasureSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidthMeasureSpecSize,60);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerWidth = mWidthMeasureSpecSize / 2;
        int centerheight = mHeightMeasureSpecSize / 2;
        String shortTime = getShortTime(mRealTime);

        canvas.drawText(shortTime, centerWidth - getFontWidth(mPaint, shortTime)/2, centerheight + getFontHeight(mPaint) / 3, mPaint);
    }

    /**
     * 返回指定的文字宽度
     *
     * @param paint
     * @param text
     * @return
     */
    public float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    private String getShortTime(long realTime) {
        long hour = realTime / 3600;
        long minuts = (realTime % 3600) / 60;
        long second = realTime % 60;

        String hour1 = hour<10 ? ("0" + hour) : String.valueOf(hour);
        String minuts1 = minuts<10 ? ("0" + minuts) : String.valueOf(minuts);
        String second1 = second<10 ? ("0" + second) : String.valueOf(second);
//        String hour1 = hour < 10 ? ("0" + hour) : String.valueOf(hour);
        return hour1 +":"+minuts1 +":"+ second1;
    }

    private String getTime(String inputTime) {
        String str = "";
        for (int i = 0; i < inputTime.length(); i++) {
            if (inputTime.charAt(i)>=48 && inputTime.charAt(i)<= 57){
                str += inputTime.charAt(i);
            }
        }
        return str==null ?"0":str;
    }
}
