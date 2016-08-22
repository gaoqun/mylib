package com.gaige.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class WeatherGlassView extends View {
    private int weatherGlassValue;//温度计的值
    private int indicatesDefaultColor = Color.BLUE;//温度计指示条的默认背景色
    private int indicatesDefaultBackgroundColor = Color.YELLOW;//温度计指示条的默认背景色
    private int indicatesDefaultTextSize = 16;//温度计指示条的默认字体大小
    private int weatherGlassDefaultDeepColor = Color.BLACK;//温度计默认最深颜色
    private int weatherGlassDefaultLightColor = Color.GREEN;//温度计默认最浅颜色
    private int weatherGlassDefaultBackground = Color.WHITE;//温度计默认背景色
    private int weatherGlassBorderColor = Color.RED;//温度计边框颜色
    private float weatherGlassDefaultWidth = 100f;//温度计默认宽度
    private float weatherGlassDefaultHeight = 500f;//温度计默认高度
    private Paint mPaint;
    private TextPaint mTextPaint;
    private String value;
    private int currentValue;
    private LinearGradient mLinearGradient;
    private int mSpeed = 1000;
    private Random mRandom;
    private static final int MaxBubble = 2;
    private static final int MiddleBubble = 1;
    private static final int MinBubble = 0;
    private Bitmap mBitmap;
    private static final int DUp = 0;//向上
    private static final int DDown = 1;//向下
    private static final int DOver = 2;//停止
    private int direction = 0;


    @IntDef({MaxBubble, MiddleBubble, MinBubble})
    @Retention(RetentionPolicy.SOURCE)
    private @interface BubbleType {
    }

    private
    @BubbleType
    int mBubbleType = 0;


    private Runnable loadRunnable = new Runnable() {
        @Override
        public void run() {
            while (currentValue < weatherGlassValue) {
                currentValue++;
                value = currentValue + "%";
                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    };
    private Thread load = new Thread(loadRunnable);

    public WeatherGlassView(Context context) {
        super(context);
        init(null, 0);
    }

    public WeatherGlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WeatherGlassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WeatherGlassView, defStyle, 0);
        weatherGlassValue = a.getInt(R.styleable.WeatherGlassView_indicatesText, weatherGlassValue);
        indicatesDefaultColor = a.getColor(R.styleable.WeatherGlassView_indicatesTextColor, indicatesDefaultColor);
        indicatesDefaultBackgroundColor = a.getColor(R.styleable.WeatherGlassView_indicatesBackgroundColor, indicatesDefaultBackgroundColor);
        weatherGlassDefaultDeepColor = a.getColor(R.styleable.WeatherGlassView_weatherGlassDeepColor, weatherGlassDefaultDeepColor);
        weatherGlassDefaultLightColor = a.getColor(R.styleable.WeatherGlassView_weatherGlassLightColor, weatherGlassDefaultLightColor);
        weatherGlassDefaultBackground = a.getColor(R.styleable.WeatherGlassView_weatherGlassBackgroundColor, weatherGlassDefaultBackground);
        weatherGlassBorderColor = a.getColor(R.styleable.WeatherGlassView_weatherGlassBorderColor, weatherGlassBorderColor);
        indicatesDefaultTextSize = a.getDimensionPixelSize(R.styleable.WeatherGlassView_indicatesTextSize, indicatesDefaultTextSize);
        weatherGlassDefaultWidth = a.getFloat(R.styleable.WeatherGlassView_weatherGlassWidth, weatherGlassDefaultWidth);
        weatherGlassDefaultHeight = a.getFloat(R.styleable.WeatherGlassView_weatherGlassHeight, weatherGlassDefaultHeight);
        mSpeed = a.getInt(R.styleable.WeatherGlassView_speed, mSpeed);

        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint();
        load.start();
        mRandom = new Random();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.indi);
//        caculateBubble();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        mPaint.setColor(weatherGlassBorderColor);
        mPaint.setTextSize(indicatesDefaultTextSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShader(null);
        RectF rectF = new RectF(paddingLeft, paddingTop + weatherGlassDefaultHeight - weatherGlassDefaultWidth, paddingLeft + weatherGlassDefaultWidth, paddingTop + weatherGlassDefaultHeight);
        float arcX = paddingLeft + weatherGlassDefaultWidth / 2 - weatherGlassDefaultWidth / 4;
        float arcY = (float) (paddingTop + weatherGlassDefaultHeight - weatherGlassDefaultWidth / 2 - ((weatherGlassDefaultWidth / 2) * Math.sin(60 * Math.PI / 180)));
        Path path = new Path();//壳子
        path.addArc(rectF, 300, 300);
        path.lineTo(arcX, paddingTop + weatherGlassDefaultWidth / 4);
        RectF topArc = new RectF(arcX, paddingTop - weatherGlassDefaultWidth / 4, arcX + weatherGlassDefaultWidth / 2, paddingTop + weatherGlassDefaultWidth / 4);
        path.arcTo(topArc, 180, 180);
        path.close();
        canvas.drawPath(path, mPaint);
        mPaint.setColor(indicatesDefaultColor);
        //计算温度计数值显示的高度,显示文字
        int valueHeight;
        valueHeight = (int) (paddingTop + weatherGlassDefaultHeight - weatherGlassDefaultHeight * (currentValue / 100f));
        mPaint.setColor(indicatesDefaultColor);

        Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, src, new Rect(paddingLeft + (int) weatherGlassDefaultWidth, valueHeight - getTextHeight() / 2, (int) (paddingLeft + weatherGlassDefaultWidth + getTextSize() * 3 / 2), valueHeight + getTextHeight() / 2), mPaint);
        canvas.drawText(value, paddingLeft + weatherGlassDefaultWidth + getTextSize() / 4, valueHeight + getTextHeight() / 4, mPaint);
        Path path1 = new Path();//填充颜色1,填充圆2，填充矩形
        mPaint.setStyle(Paint.Style.FILL);
        mLinearGradient = new LinearGradient(arcX, arcY, arcX, currentValue, weatherGlassDefaultDeepColor, weatherGlassDefaultLightColor, Shader.TileMode.MIRROR);
        mPaint.setShader(mLinearGradient);
        float radius = weatherGlassDefaultWidth / 2;
        float difference = paddingTop + weatherGlassDefaultHeight - valueHeight;
        if (difference > 0) {
            if (valueHeight > arcY) {
                if (difference < radius) {
                    float angle = (float) Math.toDegrees(Math.acos((radius - difference) / radius));
                    path1.addArc(rectF, 90 - angle, 2 * angle);
                } else if (difference == radius) {
                    //180度
                    path1.addArc(rectF, 0, 180);
                } else {
                    float angle = (float) Math.toDegrees(Math.acos((difference - radius) / radius));
                    path1.addArc(rectF, angle - 90, 360 - 2 * angle);
                }
            } else if (valueHeight >= paddingTop) {
                path1.addArc(rectF, 300, 300);
                path1.lineTo(arcX, valueHeight);
                RectF topArc1 = new RectF(arcX, valueHeight - weatherGlassDefaultWidth / 4, arcX + weatherGlassDefaultWidth / 2, valueHeight + weatherGlassDefaultWidth / 4);
                path1.arcTo(topArc1, 180, 180);
                path1.close();
            }
        }
        canvas.drawPath(path1, mPaint);

       /* if (bubbleCurrentHeight > (paddingTop + weatherGlassDefaultHeight - valueHeight)) {
            if (currentValue>=100){
                direction=DOver;
            }
            direction = DUp;
            speed = 50;
            bubbleCurrentHeight = 0;
        } else {
            canvas.drawCircle(paddingLeft + weatherGlassDefaultWidth / 2, valueHeight - bubbleCurrentHeight, weatherGlassDefaultWidth / 8, mPaint);
        }*/
        //产生随机泡泡
//        int random = mRandom.nextInt(3);
//        switch (random) {
//            case 0:
//                mBubbleType = MinBubble;
//                canvas.drawCircle((int) arcX +radius/8 , valueHeight - radius/8, radius/8, mPaint);
//                break;
//            case 1:
//                mBubbleType = MiddleBubble;
//                canvas.drawCircle((int) arcX +radius/6 , valueHeight - radius/6, radius/6, mPaint);
//                break;
//            case 2:
//                mBubbleType = MaxBubble;
//                canvas.drawCircle((int) arcX +radius/4 , valueHeight - radius/4, radius/4, mPaint);
//                break;
//        }
        path = null;
        path1 = null;
        mLinearGradient = null;
    }


    private int getTextSize() {
        mTextPaint.setTextSize(indicatesDefaultTextSize);
        return (int) mTextPaint.measureText(value);
    }


    private int getTextHeight() {
        return getFontHeight(indicatesDefaultTextSize);
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(value)) {
            int textSize = getTextSize();
            if (widthMode == MeasureSpec.EXACTLY) {
                width = widthSize;
            } else {
                int desire = (int) (getPaddingLeft() + getPaddingRight() + weatherGlassDefaultWidth + textSize);//温度计宽度+文字宽度
                width = desire;
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else {
                int desire = (int) Math.max(textSize, (getPaddingTop() + getPaddingBottom() + weatherGlassDefaultHeight)); //温度计高度
                height = desire;
            }
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void changeValue(int progress) {
        weatherGlassValue = progress;
        value = weatherGlassValue + "%";
        invalidate();
    }

    public void changeSpeed(int speed) {
        this.mSpeed = speed;
    }

    public void reset() {
        if (currentValue == 100) {
            currentValue = 0;
            load = new Thread(loadRunnable);
            load.start();
        }
        currentValue = 0;
        invalidate();
    }

    public void changeWidth(int width) {
        weatherGlassDefaultWidth = width;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
