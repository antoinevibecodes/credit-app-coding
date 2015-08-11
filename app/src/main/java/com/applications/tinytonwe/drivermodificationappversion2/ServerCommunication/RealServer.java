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
    private String chargeCardUrl;

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
        chargeCardUrl = context_.getResources().getString(R.string.serverChargeCardUrl).toString();
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

                pictureInfo.put("DriverId", driverId);
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
                        response.responseMessage = "Technical Error code : 404";
                        break;
                    case 500:
                        response.responseMessage = "Technical Error code : 500";
                        break;
                    default:
                        response.responseMessage = "Technical Error code : Unknown";
                        break;
                }
            }
            catch (Exception e){
                response.responseMessage = "Communication Error";
            }

            return response;
        }
    }


    public class GetDriverInformation extends RequestDriverInfoViaRfid {

        public GetDriverInformation(boolean useRfid){
            request.url = requestDriverUrl;
            request.useRfid = useRfid;

            if(useRfid) {
                request.rfidUidL = appData.getCardIdReadLongValue_();
                request.rfidUidS = appData.getCardIdReadStringValue_();
            }
            else{
                request.DriverId = appData.getDriverId_();
            }

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


                StringEntity dataToSend;
                //get the data
                if(request.useRfid)
                   dataToSend = prepareJsonObjects(request.rfidUidL, request.rfidUidS);
                else
                    dataToSend = prepareJsonObjects(request.DriverId);

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


        private StringEntity prepareJsonObjects(long driverId){
            try {
                JSONObject rfidData = new JSONObject();

                rfidData.put("HasDriverId", true);
                rfidData.put("DriverId", driverId);
                rfidData.put("HasRfidUidL", false);
                rfidData.put("RfidUidL", 0);
                rfidData.put("HasRfidUidS", false);
                rfidData.put("RfidUidS", "");

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
                        retrieveInformation(jsonObjectReceived);


                        //Getting the image of the driver
                        String url = context_.getResources().getString(R.string.serverRequestGetDriverPictureUrl).toString()+appData.getDriverId_();
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
                        response.responseMessage = "Technical error code : 404";
                        break;
                    case 500:
                        response.responseMessage = "Technical error code : 500";
                        break;
                    default:
                        response.responseMessage = "Technical error code : Unknown";
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


    private void retrieveInformation(JSONObject jsonObjectReceived){
        try {
            appData.setDriverId(Long.parseLong(jsonObjectReceived.getString("DriverId")));
            appData.setDriverFirstName(jsonObjectReceived.getString("FirstName"));
            appData.setDriverLastName(jsonObjectReceived.getString("LastName"));
            appData.setDriverDob(jsonObjectReceived.getString("DateOfBirth"));

            appData.setTotalCredits(jsonObjectReceived.getString("Credits"));

            JSONObject general = jsonObjectReceived.getJSONObject("General");
            appData.setGeneralCredits(general.getString("Credits"));

            JSONObject food = jsonObjectReceived.getJSONObject("Food");
            appData.setFoodCredits(food.getString("Credits"));

            JSONObject trafficTrack = jsonObjectReceived.getJSONObject("TrafficTrack");
            appData.setTrafficTrackCredits(trafficTrack.getString("Credits"));

            JSONObject tinyTrack = jsonObjectReceived.getJSONObject("TinyTrack");
            appData.setTinyTrackCredits(tinyTrack.getString("Credits"));

            JSONObject train = jsonObjectReceived.getJSONObject("Train");
            appData.setTrainCredits(train.getString("Credits"));

            JSONObject arcade = jsonObjectReceived.getJSONObject("Arcade");
            appData.setArcadeCredits(arcade.getString("Credits"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public class ChargeCard extends ChargeDriverCard {

        public ChargeCard(int entitlementType, boolean force) {
            request.url = chargeCardUrl;
            request.DriverId = appData.getDriverId_();
            request.force = force;
            request.entitlementType = entitlementType;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {

            try {

                Request request = requests[0];
                //create http client
                DefaultHttpClient httpClient;
                httpClient = new DefaultHttpClient();
                HttpParams parameters = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(parameters, request.connectionTimeoutDuration);
                HttpConnectionParams.setSoTimeout(parameters, request.responseTimeoutDuration);

                //get the data
                StringEntity dataToSend = prepareJsonObjects(request.DriverId, request.entitlementType, request.force);

                //send the data
                HttpPost httpPost = new HttpPost(request.url);
                httpPost.setEntity(dataToSend);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                //Getting the response
                HttpResponse httpResponse = httpClient.execute(httpPost);

                return analyzeServerResponse(httpResponse);
            } catch (Exception e) {
                Response response = new Response();
                response.responseOk = false;
                response.responseMessage = "Communication Error Occurred";
                return response;
            }
        }


        public Response analyzeServerResponse(HttpResponse httpResponse) {
            Response response = new Response();

            try {
                //Verify the status code received if possible i.e no timeout occured
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                response.responseOk = false;

                //In case an OK response was received
                if (statusCode == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    InputStream is = entity.getContent();
                    String receivedJsonString = convertInputStreamToString(is);
                    is.close();

                    //analyze Json
                    JSONObject jsonObjectReceived = new JSONObject(receivedJsonString);
                    response.responseOk = (boolean) jsonObjectReceived.get("CanPlay");
                    response.responseMessage = jsonObjectReceived.getString("Message");

                    //Null is being returned whenever there are not enough credits
                    if (response.responseMessage.equals("null"))
                        response.responseMessage = "Cannot Play. Not enough Credits";

                }
                //In case the status code is 404, you already know there was a negative response/problem, so set status to false
                else if (statusCode == 404) {
                    response.responseMessage = "Technical error code : 404";
                } else if (statusCode == 500) {
                    response.responseMessage = "Technical error code : 500";
                } else {
                    response.responseMessage = "Technical error code : Unknown";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                response.responseOk = false;
                response.responseMessage = "Error processing server response";

            }

            return response;
        }


        public StringEntity prepareJsonObjects(long driverId, int entitlementType, boolean forceUseCredits) {
            try {
                JSONObject jsonEntitlementObject = new JSONObject();
                jsonEntitlementObject.put("HasDriverId", true);
                jsonEntitlementObject.put("DriverId", driverId);
                jsonEntitlementObject.put("HasRfidUidL", false);
                jsonEntitlementObject.put("RfidUidL", "");
                jsonEntitlementObject.put("HasRfidUidS", false);
                jsonEntitlementObject.put("RfidUidS", "");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("DriverIdentification", jsonEntitlementObject);
                jsonObject.put("Update", true);
                jsonObject.put("EntitlementTypeId", entitlementType);
                jsonObject.put("ForceUseOfCredits", forceUseCredits);

                return new StringEntity(jsonObject.toString());

            } catch (Exception ex) {
                return null;
            }
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }

    }
}
