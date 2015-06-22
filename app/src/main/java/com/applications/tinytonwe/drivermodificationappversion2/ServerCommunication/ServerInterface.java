package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.os.AsyncTask;

/**
 * Created by admin on 6/19/2015.
 */
public abstract class ServerInterface extends AsyncTask<Request, Void, Response>{


    TaskListener mainActivity;

    public ServerInterface(TaskListener activity)
    {
        this.mainActivity = activity;
    }

    protected void onPostExecute(Response response)
    {
        mainActivity.onTaskFinished(response);
    }

}
