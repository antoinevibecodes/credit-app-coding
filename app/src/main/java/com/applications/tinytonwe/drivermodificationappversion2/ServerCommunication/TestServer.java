package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;

/**
 * Created by admin on 6/19/2015.
 */
public class TestServer extends ServerInterface {


    private Request request;
    private int requestType;
    private AppData appData;

    public TestServer(TaskListener activity, int requestType){
        super(activity);
        appData = AppData.getAppDataInstance_();
        request = new Request();
        this.requestType = requestType;
    }


    public void start(){

        this.request.rfidUidL = appData.getCardIdReadLongValue_();
        this.execute(request);
    }

    protected Response doInBackground(Request... requests){

        Response response = new Response();

        if(this.requestType == 1) {
            response.dateOfBirth = "01/01/1991";
            response.driverId = 00000001;
            response.driverImage = null;
            response.firstName = "Tiny";
            response.lastName = "Towne";
            response.hasPicture = false;
            response.responseType = 1;
        }
        else {
            response.serverMessage = "success";
            response.responseType = 2;
        }
        try {
            Thread.sleep(5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
