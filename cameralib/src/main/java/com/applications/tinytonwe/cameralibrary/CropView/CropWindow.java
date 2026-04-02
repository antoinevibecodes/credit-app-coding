package com.applications.tinytonwe.cameralibrary.CropView;

/**
 * Created by admin on 6/29/2015.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;

import com.applications.tinytonwe.cameralibrary.LibData;

/**
 * This class is where the cropping square gets displayed. It is here that I isSet the color of the cropping square,
 * its thickness, the patterns, and dimensions.
 */
public class CropWindow extends View {

    private Paint paint;
    private RectF cropRect;
    private float cropPercentage_;

    private FrameLayout cameraView_;



    /**
     * This is the constructor through which this class can be initialized
     * @param context represents the context of the calling activity
     */
    public CropWindow(Context context, float cropPercentage, String paintColor, FrameLayout cameraView){

        super(context);
        paint = designPaint(paintColor);

        cropPercentage_ = cropPercentage;
        setCropPercentage(cropPercentage_);
        cameraView_ = cameraView;
    }



    public void displayCropWindow(){
        try {
            cameraView_.addView(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void removeCropWindow(){
        cameraView_.removeView(this);
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
    public Paint designPaint(String paintColor){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        //paint.setColor(Color.parseColor("#ffc000"));
        paint.setColor(Color.parseColor(paintColor));
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

        LibData singletonPictureDimensions = LibData.getUniqueInstance();
        singletonPictureDimensions.setScreenDimensions(parentScreenWidth,parentScreenHeight);

        float cropOriginX = singletonPictureDimensions.getDisplayXOrigin();
        float cropOriginY = singletonPictureDimensions.getDisplayYOrigin();
        float cropEndX    = singletonPictureDimensions.getDisplayXEnd();
        float cropEndY    = singletonPictureDimensions.getDisplayYEnd();

        cropRect = new RectF(cropOriginX,cropOriginY,cropEndX,cropEndY);
    }


    public void setCropPercentage(float cropPercentage){

        cropPercentage_ = cropPercentage;
        LibData.getUniqueInstance().setCropPercentage(cropPercentage_);
    }
}


