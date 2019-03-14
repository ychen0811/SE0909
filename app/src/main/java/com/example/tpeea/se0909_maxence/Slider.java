package com.example.tpeea.se0909_maxence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.icu.util.Measure;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class Slider extends View {

    // Constructeurs
    public Slider (Context context){
        super(context);
        init(context, null);
    }

    public Slider (Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    // Attributs
    private boolean mEnabled = true;

    // Constantes
    final static float DEFAULT_BAR_WIDTH = 10;
    final static float DEFAULT_BAR_LENGTH = 100;
    final static float DEFAULT_CURSOR_DIAMETER = 20;

    // Minimums et maximums
    private float mValue = 0;
    private float mMin = 0;
    private float mMax = 100;

    // Dimension
    private float mBarLength;
    private float mBarWidth;
    private float mCursorDiameter;
    private Paint mCursorPaint = null;
    private Paint mValueBarPaint = null;
    private Paint mBarPaint = null;

    // Couleur des différents éléments graphiques
    private int mDisabledColor;
    private int mCursorColor;
    private int mBarColor;
    private int mValueBarColor;

    // Méthodes
    // Privées

    /**
     * Transforme la valeur du slider en un ratio entre 0 et 1
     * @param value : valeur du slider
     * @return ratio entre 0 et 1
     */
    private float valueToRatio (float value){
        return (value - mMin) / (mMax - mMin);
    }

    /**
     * Transforme le ratio du slider (compris entre 0 et 1) en sa valeur de taille exacte
     * @param ratio : ratio entre 0 et 1 du slider
     * @return valeur de la taille du slider
     */
    private float ratioToValue (float ratio){
        return ratio * (mMax - mMin) + mMin;
    }

    /**
     * Transforme la valeur du slider en une position à l'écran
     * @param value : valeur du slider
     * @return position à l'écran
     */
    private Point toPos (float value){
        int x, y;
        x = (int) Math.max(mCursorDiameter, mBarWidth)/2 + getPaddingLeft();
        y = (int) ( (1 - valueToRatio(value)) * mBarLength + mCursorDiameter / 2 + getPaddingTop() );
        return new Point(x,y);
    }

    /**
     *
     * @param position
     * @return
     */
    private float toValue (Point position){
        float ratio;
        ratio = 1 - (position.y - getPaddingTop() - mCursorDiameter / 2) / mBarLength;
        return ratioToValue(ratio);
    }

    /**
     * Initialise l'application
     * @param context
     * @param attrs
     */
    private void init (Context context, AttributeSet attrs){
        mBarLength = dpToPixel(DEFAULT_BAR_LENGTH);
        mCursorDiameter = dpToPixel(DEFAULT_CURSOR_DIAMETER);
        mBarWidth = dpToPixel(DEFAULT_BAR_WIDTH);

        mCursorPaint = new Paint();
        mBarPaint = new Paint();
        mValueBarPaint = new Paint();

        // Suppression de l'aliasing (càd le repliement)
        mCursorPaint.setAntiAlias(true);
        mBarPaint.setAntiAlias(true);
        mValueBarPaint.setAntiAlias(true);

        // Dessine les éléments graphiques
        mCursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mValueBarPaint.setStyle(Paint.Style.STROKE);

        mBarPaint.setStrokeCap(Paint.Cap.ROUND);

        mDisabledColor = ContextCompat.getColor(context, R.color.colorDisabled); // référence de la couleur à "app/res/values/color.xml"
        mCursorColor = ContextCompat.getColor(context, R.color.colorAccent);
        mBarColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mValueBarColor = ContextCompat.getColor(context, R.color.colorSecondary);

        if (mEnabled) {
            mCursorPaint.setColor(mCursorColor);
            mBarPaint.setColor(mBarColor);
            mValueBarPaint.setColor(mValueBarColor);
        } else {
            mCursorPaint.setColor(mDisabledColor);
            mBarPaint.setColor(mDisabledColor);
            mValueBarPaint.setColor(mDisabledColor);
        }

        mBarPaint.setStrokeWidth(mBarWidth);
        mValueBarPaint.setStrokeWidth(mBarWidth);

        setMinimumWidth((int) dpToPixel(DEFAULT_BAR_WIDTH+getPaddingLeft()+getPaddingRight()+DEFAULT_CURSOR_DIAMETER));
        setMinimumHeight((int) dpToPixel(DEFAULT_BAR_LENGTH+getPaddingTop()+getPaddingBottom()+DEFAULT_CURSOR_DIAMETER));
    }

    /**
     *
     * @param valueInDp
     * @return
     */
    private float dpToPixel(float valueInDp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, getResources().getDisplayMetrics());
    }
    // Publiques


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int suggestedWidth, suggestedHeigth;
        int width, height;

        suggestedWidth = Math.max( getSuggestedMinimumWidth(), (int) Math.max(mBarWidth,mCursorDiameter) + getPaddingLeft() );
        suggestedHeigth = Math.max( getSuggestedMinimumHeight(), (int) Math.max(mBarLength,mCursorDiameter) + getPaddingTop() );

        width= resolveSize(suggestedWidth,suggestedHeigth);
        height=resolveSize(suggestedHeigth,suggestedWidth);

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Point p1, p2;

        p1 = toPos(mMin);
        p2 = toPos(mMax);

        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mBarPaint);

        /*canvas.drawLine(( getWidth() - dpToPixel(DEFAULT_BAR_WIDTH) ) / 2, getHeight() - dpToPixel(mMin) - getPaddingBottom(),
                ( getWidth() - dpToPixel(DEFAULT_BAR_WIDTH) ) / 2, getHeight() - dpToPixel(DEFAULT_BAR_LENGTH) - getPaddingTop(),
                mBarPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() - dpToPixel(mValue) + getPaddingBottom(),
                dpToPixel(DEFAULT_CURSOR_DIAMETER), mCursorPaint);
        canvas.drawLine(( getWidth() - dpToPixel(DEFAULT_BAR_WIDTH) ) / 2, getHeight() - dpToPixel(mMin) + getPaddingBottom(),
                ( getWidth() - dpToPixel(DEFAULT_BAR_WIDTH) ) / 2, getHeight() - dpToPixel(mValue)  - getPaddingTop(),
                mValueBarPaint);*/
    }

}
