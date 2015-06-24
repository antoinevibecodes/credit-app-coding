package com.applications.tinytonwe.cameralibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * This class is where the cropping square gets displayed. It is here that I isSet the color of the cropping square,
 * its thickness, the patterns, and dimensions.
 */
public class CropPreview extends View {

    private Paint paint;
    private RectF cropRect;

    /**
     * This is the constructor through which this class can be initialized
     * @param context represents the context of the calling activity
     */
    public CropPreview(Context context){

        super(context);
        paint = designPaint();
    }

    /**
     * This method is called each time the size of the parent view changes. This can occur
     * when the device orientation is changed, in which case the height and the width are swapped.
     * It provides the new values of the layout/view as well as the old ones
     * @param w new width of the view
     * @param h new height of the view
     * @param oldw old width of the view
     * @param oldh old height of the view
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initializeCropArea(w,h);
        invalidate();
    }

    /**
     * This is the draw method which gets called each time "invalidate()" method is called.
     * It draws on the screen whatever is defined within it through the canvas
     * @param canvas
     */
    protected void onDraw(Canvas canvas){
        canvas.drawRect(cropRect,paint);
    }

    /**
     * Within this method, I isSet the attributes of the paint I will use to paint my cropping square
     * It is here that I isSet the color, pattern as well as the thickness/stroke and appearance (round,square etc.)
     * @return this method returns the paint which was designed
     */
    public Paint designPaint(){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#ffc000"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(15);
        paint.setPathEffect(new DashPathEffect(new float[]{5, 20}, 0));

        return paint;
    }

    /**
     * This method sets the dimensions of the cropping square such as where it should begin from on the
     * screen on which it is going to be displayed
     * @param parentScreenWidth the parent screen width on which it will be displayed
     * @param parentScreenHeight the parent screen height on which it will be displayed
     */
    public void initializeCropArea(int parentScreenWidth, int parentScreenHeight){

        SingletonCameraData cameraData = SingletonCameraData.getUniqueInstance();
        cameraData.setScreenDimensions(parentScreenWidth, parentScreenHeight);

        float cropOriginX = cameraData.getDisplayXOrigin();
        float cropOriginY = cameraData.getDisplayYOrigin();
        float cropEndX    = cameraData.getDisplayXEnd();
        float cropEndY    = cameraData.getDisplayYEnd();

        cropRect = new RectF(cropOriginX,cropOriginY,cropEndX,cropEndY);
    }
}

