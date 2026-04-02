package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock;

import android.app.Activity;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Request;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MockServer extends ServerInterface {

    private AppData appData;
    private Gson gson;

    public MockServer(Activity callingActivity) {
        super(callingActivity);
        appData = AppData.getAppDataInstance_();
        gson = new Gson();
    }

    private void simulateDelay() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    // ---- Existing operations (mock versions) ----

    public class Upload extends UploadPicture {
        public Upload() {
            this.execute(new Request());
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.responseMessage = "Picture uploaded successfully (mock)";
            return response;
        }
    }

    public class GetDriverInformation extends RequestDriverInfoViaRfid {
        public GetDriverInformation(boolean useRfid) {
            this.execute(new Request());
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;

            // Populate mock driver data
            appData.setDriverId(12345);
            appData.setDriverFirstName("John");
            appData.setDriverLastName("Smith");
            appData.setDriverDob("1995-06-15T00:00:00");
            appData.setTotalCredits("150");
            appData.setGeneralCredits("50");
            appData.setFoodCredits("20");
            appData.setTrafficTrackCredits("15");
            appData.setTinyTrackCredits("25");
            appData.setTrainCredits("20");
            appData.setArcadeCredits("20");

            response.responseMessage = "Driver information retrieved (mock)";
            return response;
        }
    }

    public class ChargeCard extends ChargeDriverCard {
        public ChargeCard(int entitlementType, boolean force, boolean checkCredits, boolean useDriverId) {
            this.execute(new Request());
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.responseMessage = "Card charged successfully (mock)\nRemaining credits: 145";
            return response;
        }
    }

    // ---- New operations ----

    public class GetChargeButtons extends FetchChargeButtons {
        public GetChargeButtons() {
            Request request = new Request();
            request.operationType = OperationType.FETCH_CHARGE_BUTTONS;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.FETCH_CHARGE_BUTTONS;

            // Return mock buttons matching the original 6 + some extras
            String json = "["
                + "{\"id\":1,\"name\":\"Track\",\"entitlementTypeId\":4,\"entitlementTypeName\":\"TinyTrack\",\"creditAmount\":5,\"isForceOption\":false,\"isActive\":true,\"sortOrder\":1,\"colorHex\":\"#e3a21a\"},"
                + "{\"id\":2,\"name\":\"Track Force\",\"entitlementTypeId\":4,\"entitlementTypeName\":\"TinyTrack\",\"creditAmount\":5,\"isForceOption\":true,\"isActive\":true,\"sortOrder\":2,\"colorHex\":\"#00aba9\"},"
                + "{\"id\":3,\"name\":\"Train\",\"entitlementTypeId\":5,\"entitlementTypeName\":\"Train\",\"creditAmount\":3,\"isForceOption\":false,\"isActive\":true,\"sortOrder\":3,\"colorHex\":\"#00aba9\"},"
                + "{\"id\":4,\"name\":\"Train Force\",\"entitlementTypeId\":5,\"entitlementTypeName\":\"Train\",\"creditAmount\":3,\"isForceOption\":true,\"isActive\":true,\"sortOrder\":4,\"colorHex\":\"#e3a21a\"},"
                + "{\"id\":5,\"name\":\"Arcade\",\"entitlementTypeId\":6,\"entitlementTypeName\":\"Arcade\",\"creditAmount\":2,\"isForceOption\":false,\"isActive\":true,\"sortOrder\":5,\"colorHex\":\"#4a90d9\"},"
                + "{\"id\":6,\"name\":\"Bumper Cars\",\"entitlementTypeId\":4,\"entitlementTypeName\":\"TinyTrack\",\"creditAmount\":4,\"isForceOption\":false,\"isActive\":true,\"sortOrder\":6,\"colorHex\":\"#d94a4a\"}"
                + "]";

            response.jsonData = json;
            response.responseMessage = "Buttons loaded (mock)";
            return response;
        }
    }

    public class ManageButton extends ManageChargeButton {
        public ManageButton(int operationType, String jsonPayload) {
            Request request = new Request();
            request.operationType = operationType;
            request.jsonPayload = jsonPayload;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = requests[0].operationType;

            switch (requests[0].operationType) {
                case OperationType.CREATE_CHARGE_BUTTON:
                    response.jsonData = "{\"Id\":99,\"Success\":true,\"Message\":\"Button created (mock)\"}";
                    response.responseMessage = "Button created successfully (mock)";
                    break;
                case OperationType.UPDATE_CHARGE_BUTTON:
                    response.jsonData = "{\"Success\":true,\"Message\":\"Button updated (mock)\"}";
                    response.responseMessage = "Button updated successfully (mock)";
                    break;
                case OperationType.DELETE_CHARGE_BUTTON:
                    response.jsonData = "{\"Success\":true,\"Message\":\"Button deleted (mock)\"}";
                    response.responseMessage = "Button deleted successfully (mock)";
                    break;
            }
            return response;
        }
    }

    public class GetTransactionHistory extends FetchTransactionHistory {
        public GetTransactionHistory(String jsonPayload) {
            Request request = new Request();
            request.operationType = OperationType.FETCH_TRANSACTION_HISTORY;
            request.jsonPayload = jsonPayload;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.FETCH_TRANSACTION_HISTORY;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            String now = sdf.format(new Date());
            Random rand = new Random();

            StringBuilder sb = new StringBuilder();
            sb.append("{\"TotalCount\":5,\"PageNumber\":1,\"PageSize\":50,\"Transactions\":[");

            String[] buttons = {"Track", "Train", "Arcade", "Bumper Cars", "Track Force"};
            String[] types = {"TinyTrack", "Train", "Arcade", "TinyTrack", "TinyTrack"};
            int[] amounts = {5, 3, 2, 4, 5};

            for (int i = 0; i < 5; i++) {
                if (i > 0) sb.append(",");
                sb.append("{")
                    .append("\"TransactionId\":").append(5000 + i).append(",")
                    .append("\"DriverId\":12345,")
                    .append("\"DriverName\":\"John Smith\",")
                    .append("\"IsAnonymous\":").append(i == 4 ? "true" : "false").append(",")
                    .append("\"ButtonName\":\"").append(buttons[i]).append("\",")
                    .append("\"EntitlementTypeName\":\"").append(types[i]).append("\",")
                    .append("\"CreditAmount\":-").append(amounts[i]).append(",")
                    .append("\"Timestamp\":\"").append(now).append("\",")
                    .append("\"TransactionType\":\"Charge\"")
                    .append("}");
            }
            sb.append("]}");

            response.jsonData = sb.toString();
            response.responseMessage = "Transaction history loaded (mock)";
            return response;
        }
    }

    public class GetDailyReport extends FetchDailyReport {
        public GetDailyReport(String date) {
            Request request = new Request();
            request.operationType = OperationType.FETCH_DAILY_REPORT;
            request.jsonPayload = date;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.FETCH_DAILY_REPORT;

            String json = "{"
                + "\"Date\":\"" + requests[0].jsonPayload + "\","
                + "\"TotalTransactions\":312,"
                + "\"TotalCreditsUsed\":1547,"
                + "\"Activities\":["
                + "{\"ButtonName\":\"Track\",\"EntitlementTypeName\":\"TinyTrack\",\"UsageCount\":47,\"TotalCreditsUsed\":235},"
                + "{\"ButtonName\":\"Train\",\"EntitlementTypeName\":\"Train\",\"UsageCount\":33,\"TotalCreditsUsed\":99},"
                + "{\"ButtonName\":\"Arcade\",\"EntitlementTypeName\":\"Arcade\",\"UsageCount\":65,\"TotalCreditsUsed\":130},"
                + "{\"ButtonName\":\"Bumper Cars\",\"EntitlementTypeName\":\"TinyTrack\",\"UsageCount\":28,\"TotalCreditsUsed\":112},"
                + "{\"ButtonName\":\"Track Force\",\"EntitlementTypeName\":\"TinyTrack\",\"UsageCount\":15,\"TotalCreditsUsed\":75}"
                + "]}";

            response.jsonData = json;
            response.responseMessage = "Daily report loaded (mock)";
            return response;
        }
    }

    public class GetKioskOptions extends FetchKioskOptions {
        public GetKioskOptions() {
            Request request = new Request();
            request.operationType = OperationType.FETCH_KIOSK_OPTIONS;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.FETCH_KIOSK_OPTIONS;

            String json = "["
                + "{\"id\":1,\"name\":\"10 Track Rides\",\"description\":\"Good for 10 rides on the traffic track\",\"creditAmount\":50,\"priceDisplay\":\"$25.00\",\"entitlementTypeId\":4},"
                + "{\"id\":2,\"name\":\"5 Train Rides\",\"description\":\"Good for 5 train rides\",\"creditAmount\":15,\"priceDisplay\":\"$10.00\",\"entitlementTypeId\":5},"
                + "{\"id\":3,\"name\":\"20 Arcade Tokens\",\"description\":\"20 tokens for any arcade game\",\"creditAmount\":20,\"priceDisplay\":\"$15.00\",\"entitlementTypeId\":6},"
                + "{\"id\":4,\"name\":\"Unlimited Day Pass\",\"description\":\"Unlimited rides and games for today\",\"creditAmount\":999,\"priceDisplay\":\"$50.00\",\"entitlementTypeId\":1},"
                + "{\"id\":5,\"name\":\"Birthday Package\",\"description\":\"Party package with 100 credits across all activities\",\"creditAmount\":100,\"priceDisplay\":\"$75.00\",\"entitlementTypeId\":1}"
                + "]";

            response.jsonData = json;
            response.responseMessage = "Kiosk options loaded (mock)";
            return response;
        }
    }

    public class PurchaseKiosk extends ProcessKioskPurchase {
        public PurchaseKiosk(String jsonPayload) {
            Request request = new Request();
            request.operationType = OperationType.PROCESS_KIOSK_PURCHASE;
            request.jsonPayload = jsonPayload;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.PROCESS_KIOSK_PURCHASE;
            response.jsonData = "{\"Success\":true,\"Message\":\"50 credits added\",\"NewBalance\":200}";
            response.responseMessage = "Purchase successful! 50 credits added. New balance: 200";
            return response;
        }
    }

    public class CheckPin extends ValidatePin {
        public CheckPin(String pin, String role) {
            Request request = new Request();
            request.operationType = OperationType.VALIDATE_PIN;
            request.jsonPayload = pin;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.operationType = OperationType.VALIDATE_PIN;

            // Mock: accept "1234" as admin PIN
            if ("1234".equals(requests[0].jsonPayload)) {
                response.responseOk = true;
                response.jsonData = "{\"Valid\":true,\"Token\":\"mock-token-abc123\"}";
                response.responseMessage = "PIN validated (mock)";
            } else {
                response.responseOk = false;
                response.responseMessage = "Invalid PIN";
            }
            return response;
        }
    }

    public class GetEntitlementTypesList extends FetchEntitlementTypes {
        public GetEntitlementTypesList() {
            Request request = new Request();
            request.operationType = OperationType.FETCH_ENTITLEMENT_TYPES;
            this.execute(request);
        }

        protected Response doInBackground(Request... requests) {
            simulateDelay();
            Response response = new Response();
            response.responseOk = true;
            response.operationType = OperationType.FETCH_ENTITLEMENT_TYPES;

            String json = "["
                + "{\"Id\":1,\"Name\":\"General\"},"
                + "{\"Id\":2,\"Name\":\"Food\"},"
                + "{\"Id\":3,\"Name\":\"TrafficTrack\"},"
                + "{\"Id\":4,\"Name\":\"TinyTrack\"},"
                + "{\"Id\":5,\"Name\":\"Train\"},"
                + "{\"Id\":6,\"Name\":\"Arcade\"},"
                + "{\"Id\":7,\"Name\":\"MemberAdvantages\"},"
                + "{\"Id\":8,\"Name\":\"TransferCredits\"}"
                + "]";

            response.jsonData = json;
            response.responseMessage = "Entitlement types loaded (mock)";
            return response;
        }
    }
}
