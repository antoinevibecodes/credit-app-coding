package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.content.Context;
import android.util.Base64;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

/**
 * Created by admin on 7/6/2015.
 */
public class RealServer extends ServerInterface {

    private Request request;
    private int requestType;
    private AppData appData;

    public RealServer(TaskListener activity, Context context, int requestType){
        super(activity);
        appData = AppData.getAppDataInstance_();

        this.requestType = requestType;

        request = new Request();
        request.url = context.getResources().getText(R.string.serverUploadUrl).toString();
        request.connectionTimeoutDuration = Integer.parseInt(context.getResources().getText(R.string.connectionTimeoutDuration).toString());
        request.responseTimeoutDuration = Integer.parseInt(context.getResources().getText(R.string.responseTimeoutDuration).toString());

    }


    public void start(){
        this.request.DriverId = appData.getDriverId_();
        this.request.PictureData = appData.getPictureData();
        this.execute(request);
    }

    protected Response doInBackground(Request... requests){

        Request request = requests[0];

        try {
            DefaultHttpClient httpClient;
            httpClient = new DefaultHttpClient();
            HttpParams parameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(parameters, request.connectionTimeoutDuration);
            HttpConnectionParams.setSoTimeout(parameters, request.responseTimeoutDuration);

            //get the data
            StringEntity dataToSend = prepareJsonObjects(request.DriverId, request.PictureData);

            //send the data
            HttpPost httpPost = new HttpPost(request.url);
            httpPost.setEntity(dataToSend);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            //Getting the response

            HttpResponse httpResponse = httpClient.execute(httpPost);

            String breakPoint = "break Point";

        }
        catch (Exception e){
                e.printStackTrace();
        }
        Response response = new Response();
        response.serverMessage = "success";
        response.responseType = 2;
        return response;
    }

    private StringEntity prepareJsonObjects(long driverId, byte[] pictureData){
        try {
            JSONObject pictureInfo = new JSONObject();

            pictureInfo.put("DriverId", 1);
            String base64Encoded = Base64.encodeToString(pictureData,Base64.DEFAULT);
            pictureInfo.put("PictureData", base64Encoded);

                return new StringEntity(pictureInfo.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
