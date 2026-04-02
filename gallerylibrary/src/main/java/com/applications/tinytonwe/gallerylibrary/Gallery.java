package com.applications.tinytonwe.gallerylibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by admin on 8/3/2015.
 */
public class Gallery {

    private RecyclerView.Adapter adapter_;
    private RecyclerView.LayoutManager layoutManager_;
    private Bitmap[] bitmaps_;

    private RecyclerView recyclerView_;
    private Context context_;
    private RecyclerViewClickListener recyclerViewClickListener_;

    public Gallery(Activity activity,RecyclerView recyclerView){
        context_ = activity.getApplicationContext();
        recyclerViewClickListener_ = (RecyclerViewClickListener)activity;
        recyclerView_ = recyclerView;
    }

    public void setBitmaps(Bitmap [] bitmaps){
        int numberOfImages = bitmaps.length;
        bitmaps_ = new Bitmap[numberOfImages];

        for(int loop=0; loop<numberOfImages; loop++){
            bitmaps_[loop] = Bitmap.createScaledBitmap(bitmaps[loop],350,350,false);
        }


        initialize();
    }

    private void initialize(){
        layoutManager_ = new LinearLayoutManager(context_,LinearLayoutManager.HORIZONTAL, false);
        recyclerView_.setLayoutManager(layoutManager_);

        adapter_ = new Adapter(bitmaps_,recyclerViewClickListener_);
        recyclerView_.setAdapter(adapter_);

    }
}
