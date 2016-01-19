package com.tomych.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dimon_GDA on 8/26/15.
 */
public class ProgressView extends View {


    final static int PROGRESS_COLOR = 0xFF0ACCEF;
    final static int TITLE_COLOR = 0xFF000000;


    final static int PROGRESS_START_ANGLE = -90;
    final static String TEXT_HOLDER = "99:99";
    final static String TIME_FORMATTER = "%02d:%02d";
    private Bitmap background;
    private Bitmap circle;
    private Bitmap srcBackground;
    private Bitmap srcCircle;
    private Bitmap circleProgress;
    private Bitmap srcCircleProgress;

    private Bitmap arrow;
    private Bitmap srcArrow;


    private String firstTitle = "";
    private String secondTitle = "";

    private Paint paint = null;
    private RectF mBounds = new RectF();
    private float max = 600;
    private float progress = 0;
    private float minTextSize = 2;
    private Rect textRect;
    private Typeface fontr;
    private Typeface fontb;

    public void setFirstTitle(String firstTitle) {
        this.firstTitle = firstTitle;
    }

    public void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }

    public ProgressView(Context context) {
        super(context);
        init(context);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {

        if (progress > max) {
            progress = max;
        }
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(1, Paint.ANTI_ALIAS_FLAG));
        super.dispatchDraw(canvas);
    }


    private void init(Context context) {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        textRect = new Rect();

        srcBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.background);
        srcCircle = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        srcCircleProgress = BitmapFactory.decodeResource(getResources(), R.mipmap.progress);
        srcArrow = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);

        fontb = Typeface.createFromAsset(context.getAssets(), "fonts/gothicb.ttf");
        fontr = Typeface.createFromAsset(context.getAssets(), "fonts/gothicr.ttf");

    }


    private float calcProgressAngle(float progress) {

        return 360 * progress;

    }


    private Bitmap punchArcInABitmap(Bitmap foreground, float angle) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawArc(new RectF(0, 0, foreground.getWidth(), foreground.getHeight()), PROGRESS_START_ANGLE, angle - 360, true, paint);
        return bitmap;
    }

    private Bitmap rotateBitmap(Bitmap foreground, float angle) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.setRotate(angle, foreground.getWidth() / 2, foreground.getHeight() / 2);
        canvas.drawBitmap(foreground, matrix, new Paint());
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);


        c.drawBitmap(background, 0, 0, paint);

        float angle = calcProgressAngle(progress / max);

        c.drawBitmap(punchArcInABitmap(circleProgress, angle), 0, 0, paint);


        float xAngle = (float) ((angle + PROGRESS_START_ANGLE) * (Math.PI / 180));


        float x1 = mBounds.centerX() + (float) ((mBounds.width() * 0.84 / 2) * Math.cos(xAngle)) - circle.getWidth() / 2;
        float y1 = mBounds.centerY() + (float) ((mBounds.height() * 0.84 / 2) * Math.sin(xAngle)) - circle.getHeight() / 2;







        float x;
        float y;

        paint.setColor(TITLE_COLOR);
        paint.setTextSize(minTextSize);

        paint.setTypeface(Typeface.create(fontb, Typeface.NORMAL));


        if (firstTitle != null && firstTitle.length() > 1) {
            paint.getTextBounds(firstTitle, 0, firstTitle.length(), textRect);

            while (textRect.width() < mBounds.width() / 2) {
                paint.setTextSize(paint.getTextSize() + 1);
                paint.getTextBounds(firstTitle, 0, firstTitle.length(), textRect);
            }

            x = mBounds.width() / 2f - textRect.width() / 2f;
            y = mBounds.height() / 4f + textRect.height() / 2f;
            c.drawText(firstTitle, x, y, paint);
        }

        int leftMinutes = 0;
        int leftSeconds = 0;

        if (max >= progress) {
            leftMinutes = (int) ((max - progress) / 60);
            leftSeconds = (int) (max - leftMinutes * 60 - progress);
        }


        String left = String.format(TIME_FORMATTER, leftMinutes, leftSeconds);

        paint.setColor(PROGRESS_COLOR);
        paint.setTextSize(minTextSize);
        paint.setTypeface(Typeface.create(fontr, Typeface.NORMAL));
        paint.getTextBounds(TEXT_HOLDER, 0, TEXT_HOLDER.length(), textRect);

        while (textRect.width() < mBounds.width() * 0.5) {
            paint.setTextSize(paint.getTextSize() + 1);
            paint.getTextBounds(TEXT_HOLDER, 0, TEXT_HOLDER.length(), textRect);
        }

        x = mBounds.width() / 2f - textRect.width() / 2f;
        y = mBounds.height() / 2.25f + textRect.height() / 2f;
        c.drawText(left, x, y, paint);


        paint.setColor(TITLE_COLOR);
        paint.setTextSize(minTextSize);

        paint.setTypeface(Typeface.create(fontb, Typeface.BOLD));


        if (secondTitle != null && secondTitle.length() > 1) {
            paint.getTextBounds(secondTitle, 0, secondTitle.length(), textRect);

            while (textRect.width() < mBounds.width() / 2) {
                paint.setTextSize(paint.getTextSize() + 1);
                paint.getTextBounds(secondTitle, 0, secondTitle.length(), textRect);
            }

            x = mBounds.width() / 2f - textRect.width() / 2f;
            y = mBounds.height() / 2f + mBounds.height() / 8f + textRect.height() / 2f;
            c.drawText(secondTitle, x, y, paint);
        }

        int totalMinutes = (int) (progress / 60);
        int totalSeconds = (int) (progress - totalMinutes * 60);


        String total = String.format(TIME_FORMATTER, totalMinutes, totalSeconds);

        paint.setTextSize(minTextSize);
        paint.setTypeface(Typeface.create(fontr, Typeface.NORMAL));
        paint.getTextBounds(TEXT_HOLDER, 0, TEXT_HOLDER.length(), textRect);

        while (textRect.width() < mBounds.width() / 3) {
            paint.setTextSize(paint.getTextSize() + 1);
            paint.getTextBounds(TEXT_HOLDER, 0, TEXT_HOLDER.length(), textRect);
        }

        x = mBounds.width() / 2f - textRect.width() / 2f;
        y = mBounds.height() / 2f + mBounds.height() / 4f + textRect.height() / 2f;
        c.drawText(total, x, y, paint);

        c.drawBitmap(circle, x1, y1, paint);

        c.drawBitmap(rotateBitmap(arrow, angle), x1, y1, paint);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(0, 0, w, h);

        float xScale = srcBackground.getWidth() / mBounds.width();
        float yScale = srcBackground.getHeight() / mBounds.height();

        background = Bitmap.createScaledBitmap(srcBackground, (int) (srcBackground.getWidth() / xScale), (int) (srcBackground.getHeight() / yScale), true);
        circle = Bitmap.createScaledBitmap(srcCircle, (int) (srcCircle.getWidth() / xScale), (int) (srcCircle.getHeight() / yScale), true);
        circleProgress = Bitmap.createScaledBitmap(srcCircleProgress, (int) (srcCircleProgress.getWidth() / xScale), (int) (srcCircleProgress.getHeight() / yScale), true);
        arrow = Bitmap.createScaledBitmap(srcArrow, (int) (srcArrow.getWidth() / xScale), (int) (srcArrow.getHeight() / yScale), true);
    }

}
