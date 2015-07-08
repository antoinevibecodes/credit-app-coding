package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by admin on 7/8/2015.
 */
public class RealServer extends ServerInterface {

    private Request request;
    private AppData appData;

    private String uploadPictureUrl;
    private String requestDriverUrl;

    private Context context_;

    public RealServer(Activity callingActivity) {
        super(callingActivity);

        context_ = callingActivity.getApplicationContext();

        appData = AppData.getAppDataInstance_();

        request = new Request();
        request.connectionTimeoutDuration = Integer.parseInt(context_.getResources().getText(R.string.connectionTimeoutDuration).toString());
        request.responseTimeoutDuration = Integer.parseInt(context_.getResources().getText(R.string.responseTimeoutDuration).toString());

        uploadPictureUrl = context_.getResources().getString(R.string.serverUploadUrl).toString();
        requestDriverUrl = context_.getResources().getString(R.string.serverRequestDriverUrl).toString();
    }

    public class Upload extends UploadPicture{

        public Upload(){
            request.url = uploadPictureUrl;
            request.DriverId = appData.getDriverId_();
            request.PictureData = appData.getPictureData();
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
                return analyzeServerResponseForPictureUpload(httpResponse);

            }
            catch (Exception e){
                e.printStackTrace();
                Response response = new Response();
                response.responseMessage = "Communication Error";
                response.responseOk = false;
                return response;
            }

        }

        private StringEntity prepareJsonObjects(long driverId, byte[] pictureData){
            try {
                JSONObject pictureInfo = new JSONObject();

                pictureInfo.put("DriverId", 1);
                String base64Encoded = Base64.encodeToString(pictureData, Base64.DEFAULT);
                pictureInfo.put("PictureData", base64Encoded);

                return new StringEntity(pictureInfo.toString());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        private Response analyzeServerResponseForPictureUpload(HttpResponse httpResponse){
            Response response = new Response();
            response.responseOk = false;

            try{
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                switch (statusCode){
                    case 200:
                        response.responseOk = true;
                        break;
                    case 404:
                        response.responseMessage = "404 Not Found Error";
                        break;
                    case 500:
                        response.responseMessage = "500 Internal Server Error";
                        break;
                    default:
                        response.responseMessage = "Unknown Error";
                        break;
                }
            }
            catch (Exception e){
                response.responseMessage = "Communication Error";
            }

            return response;
        }
    }

    public class GetDriverInformation extends RequestDriverInfo{

        public GetDriverInformation(){
            request.url = requestDriverUrl;
            request.rfidUidL = appData.getCardIdReadLongValue_();
            request.rfidUidS = appData.getCardIdReadStringValue_();
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
                StringEntity dataToSend = prepareJsonObjects(request.rfidUidL, request.rfidUidS);

                //send the data
                HttpPost httpPost = new HttpPost(request.url);
                httpPost.setEntity(dataToSend);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                //Getting the response

                HttpResponse httpResponse = httpClient.execute(httpPost);

                return analyzeServerResponseForDriverData(httpResponse);
            }
            catch (Exception e){
                e.printStackTrace();
                Response response = new Response();
                response.responseMessage = "Communication Error";
                response.responseOk = false;
                return response;
            }

        }

        private StringEntity prepareJsonObjects(long rfidUidL, String rfidUidS){
            try {
                JSONObject rfidData = new JSONObject();

                rfidData.put("HasDriverId", false);
                rfidData.put("DriverId", 0);
                rfidData.put("HasRfidUidL", true);
                rfidData.put("RfidUidL", rfidUidL);
                rfidData.put("HasRfidUidS", true);
                rfidData.put("RfidUidS", rfidUidS);

                return new StringEntity(rfidData.toString());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        private Response analyzeServerResponseForDriverData(HttpResponse httpResponse){
            Response response = new Response();
            response.responseOk = false;

            try{
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                switch (statusCode){
                    case 200:
                        response.responseOk = true;
                        //
                        //Get Json object received from server
                        //
                        HttpEntity entity = httpResponse.getEntity();
                        InputStream is = entity.getContent();
                        String receivedJsonString = convertInputStreamToString(is);
                        is.close();
                        //
                        //

                        //Get the information contained within the Json object
                        JSONObject jsonObjectReceived = new JSONObject(receivedJsonString);

                        appData.setDriverId((long)jsonObjectReceived.get("DriverId"));
                        appData.setDriverFirstName(jsonObjectReceived.getString("FirstName"));
                        appData.setDriverLastName(jsonObjectReceived.getString("LastName"));
                        appData.setDriverDob(jsonObjectReceived.getString("DateOfBirth"));


                        //Getting the image of the driver
                        String url = "http://i.ytimg.com/vi/qqon8BQTcK0/maxresdefault.jpg";
                        Bitmap bitmap;
                        Drawable d;
                        try {
                            InputStream imageInputStream = (InputStream) new URL(url).getContent();
                            d = Drawable.createFromStream(imageInputStream, "src name");
                            bitmap = ((BitmapDrawable)d).getBitmap();
                            appData.setDriverImage(bitmap);
                        } catch (Exception e) {
                            d = context_.getResources().getDrawable(R.drawable.notfound);
                            bitmap = ((BitmapDrawable)d).getBitmap();
                            appData.setDriverImage(bitmap);
                        }
                        //
                        //
                        break;
                    case 404:
                        response.responseMessage = "404 Not Found Error";
                        break;
                    case 500:
                        response.responseMessage = "500 Internal Server Error";
                        break;
                    default:
                        response.responseMessage = "Unknown Error";
                        break;
                }
            }
            catch (Exception e){
                response.responseMessage = "Communication Error";
            }

            return response;
        }


    }




    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
