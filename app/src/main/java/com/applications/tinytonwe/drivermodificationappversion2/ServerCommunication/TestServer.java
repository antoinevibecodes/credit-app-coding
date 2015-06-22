package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

/**
 * Created by admin on 6/19/2015.
 */
public class TestServer extends ServerInterface {


    private Request request;

    public TestServer(TaskListener activity){
        super(activity);
        request = new Request();
    }


    public void setDriverRfId(long rfidUidL){
        this.request.rfidUidL = rfidUidL;
    }

    public void start(){

        this.execute(request);
    }

    protected Response doInBackground(Request... requests){

        Response response = new Response();

        response.dateOfBirth = "01/01/1991";
        response.driverId = 00000001;
        response.driverImage = null;
        response.firstName = "Tiny";
        response.lastName = "Towne";
        response.hasPicture = false;
        try {
            Thread.sleep(5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
