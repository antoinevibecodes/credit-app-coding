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
import org.apache.http.client.methods.HttpGet;
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
    private String checkCreditUrl;

    // New endpoint URLs
    private String getChargeButtonsUrl;
    private String createChargeButtonUrl;
    private String updateChargeButtonUrl;
    private String deleteChargeButtonUrl;
    private String getEntitlementTypesUrl;
    private String getTransactionHistoryUrl;
    private String getDailyReportUrl;
    private String getKioskOptionsUrl;
    private String processKioskPurchaseUrl;
    private String validatePinUrl;
    private String getStandardCardsUrl;
    private String createStandardCardUrl;
    private String updateStandardCardUrl;
    private String deleteStandardCardUrl;

    private Context context_;
    private int connTimeout;
    private int respTimeout;

    public RealServer(Activity callingActivity) {
        super(callingActivity);

        context_ = callingActivity.getApplicationContext();

        appData = AppData.getAppDataInstance_();

        request = new Request();
        connTimeout = Integer.parseInt(context_.getResources().getText(R.string.connectionTimeoutDuration).toString());
        respTimeout = Integer.parseInt(context_.getResources().getText(R.string.responseTimeoutDuration).toString());
        request.connectionTimeoutDuration = connTimeout;
        request.responseTimeoutDuration = respTimeout;

        uploadPictureUrl = context_.getResources().getString(R.string.serverUploadUrl).toString();
        requestDriverUrl = context_.getResources().getString(R.string.serverRequestDriverUrl).toString();
        chargeCardUrl = context_.getResources().getString(R.string.serverChargeCardUrl).toString();
        checkCreditUrl = context_.getResources().getString(R.string.serverCheckCreditUrl).toString();

        // New endpoints
        getChargeButtonsUrl = context_.getResources().getString(R.string.serverGetChargeButtonsUrl);
        createChargeButtonUrl = context_.getResources().getString(R.string.serverCreateChargeButtonUrl);
        updateChargeButtonUrl = context_.getResources().getString(R.string.serverUpdateChargeButtonUrl);
        deleteChargeButtonUrl = context_.getResources().getString(R.string.serverDeleteChargeButtonUrl);
        getEntitlementTypesUrl = context_.getResources().getString(R.string.serverGetEntitlementTypesUrl);
        getTransactionHistoryUrl = context_.getResources().getString(R.string.serverGetTransactionHistoryUrl);
        getDailyReportUrl = context_.getResources().getString(R.string.serverGetDailyReportUrl);
        getKioskOptionsUrl = context_.getResources().getString(R.string.serverGetKioskOptionsUrl);
        processKioskPurchaseUrl = context_.getResources().getString(R.string.serverProcessKioskPurchaseUrl);
        validatePinUrl = context_.getResources().getString(R.string.serverValidatePinUrl);
        getStandardCardsUrl = context_.getResources().getString(R.string.serverGetStandardCardsUrl);
        createStandardCardUrl = context_.getResources().getString(R.string.serverCreateStandardCardUrl);
        updateStandardCardUrl = context_.getResources().getString(R.string.serverUpdateStandardCardUrl);
        deleteStandardCardUrl = context_.getResources().getString(R.string.serverDeleteStandardCardUrl);
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

        public ChargeCard(int entitlementType, boolean force, boolean checkCredits, boolean useDriverId) {

            if(checkCredits) {
                request.url = checkCreditUrl;
                request.checkCredits = true;
            }
            else
                request.url = chargeCardUrl;

            if(useDriverId) {
                request.DriverId = appData.getDriverId_();
                request.useDriverId = true;
            }
            else
                request.rfidUidL = appData.getCardIdReadLongValue_();

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

                StringEntity dataToSend = prepareJsonObjects(request.DriverId, request.rfidUidL, request.entitlementType, request.force, request.useDriverId, request.checkCredits);


                //send the data
                HttpPost httpPost = new HttpPost(request.url);
                httpPost.setEntity(dataToSend);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                //Getting the response
                HttpResponse httpResponse = httpClient.execute(httpPost);

                return analyzeServerResponse(httpResponse, request.checkCredits);
            } catch (Exception e) {
                Response response = new Response();
                response.responseOk = false;
                response.responseMessage = "Communication Error Occurred";
                return response;
            }
        }


        public Response analyzeServerResponse(HttpResponse httpResponse, boolean checkCredits) {
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

                    if(!(checkCredits)) {
                        //analyze Json
                        JSONObject jsonObjectReceived = new JSONObject(receivedJsonString);
                        response.responseOk = (boolean) jsonObjectReceived.get("CanPlay");
                        response.responseMessage = jsonObjectReceived.getString("Message");
                    }
                    else{
                        response.responseOk = true;
                        response.responseMessage = formatResponseForCredit(receivedJsonString);
                    }


                    //Null is being returned whenever there are not enough credits
                    if (response.responseMessage.equals("null")) {
                        if(response.responseOk)
                            response.responseMessage = "Can Play. TT Member";
                        else
                        response.responseMessage = "Cannot Play. Not enough Credits";
                    }

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


        public StringEntity prepareJsonObjects(long driverId, long rfidUidL, int entitlementType, boolean forceUseCredits, boolean useDriverId, boolean checkCredits) {
            try {
                JSONObject jsonEntitlementObject = new JSONObject();

                if(useDriverId){
                    jsonEntitlementObject.put("HasDriverId", true);
                    jsonEntitlementObject.put("DriverId", driverId);
                    jsonEntitlementObject.put("HasRfidUidL", false);
                    jsonEntitlementObject.put("RfidUidL", "");
                }
                else{
                    jsonEntitlementObject.put("HasDriverId", false);
                    jsonEntitlementObject.put("DriverId", 0);
                    jsonEntitlementObject.put("HasRfidUidL", true);
                    jsonEntitlementObject.put("RfidUidL", rfidUidL);
                }

                jsonEntitlementObject.put("HasRfidUidS", false);
                jsonEntitlementObject.put("RfidUidS", "");

                if(!(checkCredits)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("DriverIdentification", jsonEntitlementObject);
                    jsonObject.put("Update", true);
                    jsonObject.put("EntitlementTypeId", entitlementType);
                    jsonObject.put("ForceUseOfCredits", forceUseCredits);

                    return new StringEntity(jsonObject.toString());
                }
                else
                    return new StringEntity(jsonEntitlementObject.toString());

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

        public String formatResponseForCredit(String creditResponse){

            String formattedString = "";

            creditResponse = creditResponse.substring(1,creditResponse.length()-2);

            int indexOfNewLine = creditResponse.indexOf("\\");

            while(indexOfNewLine != -1) {
                formattedString += creditResponse.substring(0, indexOfNewLine) + "\n";
                creditResponse = creditResponse.substring(indexOfNewLine+2,creditResponse.length()-1);
                indexOfNewLine = creditResponse.indexOf("\\");
            }

            return formattedString;
        }

    }

    // ==================== Helper: generic HTTP calls ====================

    private Response doHttpPost(String url, String jsonBody) {
        Response response = new Response();
        response.responseOk = false;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams parameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(parameters, connTimeout);
            HttpConnectionParams.setSoTimeout(parameters, respTimeout);

            HttpPost httpPost = new HttpPost(url);
            if (jsonBody != null) {
                httpPost.setEntity(new StringEntity(jsonBody));
            }
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                String jsonString = convertInputStreamToString(is);
                is.close();
                response.responseOk = true;
                response.jsonData = jsonString;
                response.responseMessage = "Success";
            } else {
                response.responseMessage = "Server error code: " + statusCode;
            }
        } catch (Exception e) {
            response.responseMessage = "Communication Error: " + e.getMessage();
        }
        return response;
    }

    private Response doHttpGet(String url) {
        Response response = new Response();
        response.responseOk = false;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams parameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(parameters, connTimeout);
            HttpConnectionParams.setSoTimeout(parameters, respTimeout);

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                String jsonString = convertInputStreamToString(is);
                is.close();
                response.responseOk = true;
                response.jsonData = jsonString;
                response.responseMessage = "Success";
            } else {
                response.responseMessage = "Server error code: " + statusCode;
            }
        } catch (Exception e) {
            response.responseMessage = "Communication Error: " + e.getMessage();
        }
        return response;
    }

    // ==================== New API operations ====================

    public class GetChargeButtons extends FetchChargeButtons {
        public GetChargeButtons() {
            Request req = new Request();
            req.operationType = OperationType.FETCH_CHARGE_BUTTONS;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpGet(getChargeButtonsUrl + "?includeInactive=true");
            response.operationType = OperationType.FETCH_CHARGE_BUTTONS;
            return response;
        }
    }

    public class ManageButton extends ManageChargeButton {
        private int opType;
        private String jsonPayload;

        public ManageButton(int operationType, String jsonPayload) {
            this.opType = operationType;
            this.jsonPayload = jsonPayload;
            Request req = new Request();
            req.operationType = operationType;
            req.jsonPayload = jsonPayload;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            String url;
            switch (requests[0].operationType) {
                case OperationType.CREATE_CHARGE_BUTTON:
                    url = createChargeButtonUrl;
                    break;
                case OperationType.UPDATE_CHARGE_BUTTON:
                    url = updateChargeButtonUrl;
                    break;
                case OperationType.DELETE_CHARGE_BUTTON:
                    url = deleteChargeButtonUrl;
                    break;
                default:
                    Response err = new Response();
                    err.responseOk = false;
                    err.responseMessage = "Unknown operation";
                    return err;
            }
            Response response = doHttpPost(url, requests[0].jsonPayload);
            response.operationType = requests[0].operationType;
            return response;
        }
    }

    public class GetTransactionHistory extends FetchTransactionHistory {
        public GetTransactionHistory(String jsonPayload) {
            Request req = new Request();
            req.operationType = OperationType.FETCH_TRANSACTION_HISTORY;
            req.jsonPayload = jsonPayload;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpPost(getTransactionHistoryUrl, requests[0].jsonPayload);
            response.operationType = OperationType.FETCH_TRANSACTION_HISTORY;
            return response;
        }
    }

    public class GetDailyReport extends FetchDailyReport {
        public GetDailyReport(String date) {
            Request req = new Request();
            req.operationType = OperationType.FETCH_DAILY_REPORT;
            req.jsonPayload = "{\"Date\":\"" + date + "\"}";
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpPost(getDailyReportUrl, requests[0].jsonPayload);
            response.operationType = OperationType.FETCH_DAILY_REPORT;
            return response;
        }
    }

    public class GetKioskOptions extends FetchKioskOptions {
        public GetKioskOptions() {
            Request req = new Request();
            req.operationType = OperationType.FETCH_KIOSK_OPTIONS;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpPost(getKioskOptionsUrl, "{}");
            response.operationType = OperationType.FETCH_KIOSK_OPTIONS;
            return response;
        }
    }

    public class PurchaseKiosk extends ProcessKioskPurchase {
        public PurchaseKiosk(String jsonPayload) {
            Request req = new Request();
            req.operationType = OperationType.PROCESS_KIOSK_PURCHASE;
            req.jsonPayload = jsonPayload;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpPost(processKioskPurchaseUrl, requests[0].jsonPayload);
            response.operationType = OperationType.PROCESS_KIOSK_PURCHASE;
            return response;
        }
    }

    public class CheckPin extends ValidatePin {
        public CheckPin(String pin, String role) {
            Request req = new Request();
            req.operationType = OperationType.VALIDATE_PIN;
            req.jsonPayload = "{\"Pin\":\"" + pin + "\",\"Role\":\"" + role + "\"}";
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpPost(validatePinUrl, requests[0].jsonPayload);
            response.operationType = OperationType.VALIDATE_PIN;
            return response;
        }
    }

    public class GetEntitlementTypesList extends FetchEntitlementTypes {
        public GetEntitlementTypesList() {
            Request req = new Request();
            req.operationType = OperationType.FETCH_ENTITLEMENT_TYPES;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpGet(getEntitlementTypesUrl);
            response.operationType = OperationType.FETCH_ENTITLEMENT_TYPES;
            return response;
        }
    }

    public class GetStandardCards extends FetchStandardCards {
        public GetStandardCards() {
            Request req = new Request();
            req.operationType = OperationType.FETCH_STANDARD_CARDS;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            Response response = doHttpGet(getStandardCardsUrl);
            response.operationType = OperationType.FETCH_STANDARD_CARDS;
            return response;
        }
    }

    public class ManageStdCard extends ManageStandardCard {
        public ManageStdCard(int operationType, String jsonPayload) {
            Request req = new Request();
            req.operationType = operationType;
            req.jsonPayload = jsonPayload;
            this.execute(req);
        }

        protected Response doInBackground(Request... requests) {
            String url;
            switch (requests[0].operationType) {
                case OperationType.CREATE_STANDARD_CARD:
                    url = createStandardCardUrl;
                    break;
                case OperationType.UPDATE_STANDARD_CARD:
                    url = updateStandardCardUrl;
                    break;
                case OperationType.DELETE_STANDARD_CARD:
                    url = deleteStandardCardUrl;
                    break;
                default:
                    Response err = new Response();
                    err.responseOk = false;
                    err.responseMessage = "Unknown operation";
                    return err;
            }
            Response response = doHttpPost(url, requests[0].jsonPayload);
            response.operationType = requests[0].operationType;
            return response;
        }
    }
}
