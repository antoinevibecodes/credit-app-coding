package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by admin on 7/8/2015.
 */
public abstract class ServerInterface {

    protected TaskListener callingActivity_;

    public ServerInterface(Activity callingActivity){
        callingActivity_ = (TaskListener)callingActivity;
    }

    public abstract class UploadPicture extends AsyncTask<Request,Void,Response>{

        protected void onPostExecute(Response response)
        {
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class RequestDriverInfoViaRfid extends AsyncTask<Request,Void, Response>{

        protected void onPostExecute(Response response)
        {
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ChargeDriverCard extends AsyncTask<Request,Void, Response>{

        protected void onPostExecute(Response response)
        {
            callingActivity_.onTaskFinished(response);
        }
    }

}
