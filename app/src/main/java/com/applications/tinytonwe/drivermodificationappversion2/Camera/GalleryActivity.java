package com.applications.tinytonwe.drivermodificationappversion2.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.SendPicture.SendPictureActivity;
import com.applications.tinytonwe.gallerylibrary.Gallery;
import com.applications.tinytonwe.gallerylibrary.RecyclerViewClickListener;

import java.util.ArrayList;

public class GalleryActivity extends ActionBarActivity implements RecyclerViewClickListener {


    private final int NOT_INITIALIZED = -9999;
    private int currentPictureIndex_ = NOT_INITIALIZED;

    private RecyclerView recyclerView_;
    private ImageView mainImageView_;

    private ArrayList<Bitmap> bitmaps_;
    private Bitmap[] driverImages_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        checkIfReturning(savedInstanceState);
        registerListeners();
        initialize();
    }




    private void registerListeners(){
        mainImageView_ = (ImageView)findViewById(R.id.currentPictureDisplayed);
        recyclerView_ = (RecyclerView) findViewById(R.id.my_recycler_view);

        ImageButton retakePicture = (ImageButton)findViewById(R.id.retakePicture);
        ImageButton selectPicture = (ImageButton)findViewById(R.id.selectPicture);

        retakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CActions.NEW_PICTURE);
            }
        });

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CActions.SELECT_PICTURE);
            }
        });
    }


    private void initialize(){

        Gallery gallery = new Gallery(this, recyclerView_);

        bitmaps_ = CameraData.getInstance().getImages();
        driverImages_ = new Bitmap[bitmaps_.size()];
        driverImages_ = bitmaps_.toArray(driverImages_);

        gallery.setBitmaps(driverImages_);

        if(currentPictureIndex_ != NOT_INITIALIZED)
            mainImageView_.setImageBitmap(driverImages_[currentPictureIndex_]);
        else {
            currentPictureIndex_ = 0;
            mainImageView_.setImageBitmap(driverImages_[currentPictureIndex_]);
        }
    }


    public void buttonHandler(CActions cActions){

        switch (cActions){
            case NEW_PICTURE:
                startActivity(new Intent(this, CameraActivity.class));
                this.finish();
                break;
            case SELECT_PICTURE:
                Bitmap bitmap = driverImages_[currentPictureIndex_];
                CameraData.getInstance().setBitmapChosen(bitmap);
                AppData appData = AppData.getAppDataInstance_();
                appData.setCroppedImage_(bitmap);
                startActivity(new Intent(this, SendPictureActivity.class));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void recyclerViewListClicked(int position) {
        mainImageView_.setImageBitmap(driverImages_[position]);
        currentPictureIndex_ = position;
    }


    public void onSaveInstanceState(Bundle savedBundle){
        savedBundle.putInt("currentPictureIndex", currentPictureIndex_);
        super.onSaveInstanceState(savedBundle);
    }

    public void checkIfReturning(Bundle savedBundle){
        if(savedBundle != null)
            currentPictureIndex_ = savedBundle.getInt("currentPictureIndex");
    }
}
