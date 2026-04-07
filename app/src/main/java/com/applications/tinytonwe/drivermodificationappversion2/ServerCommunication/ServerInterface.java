package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.app.Activity;
import android.os.AsyncTask;

public abstract class ServerInterface {

    protected TaskListener callingActivity_;

    public ServerInterface(Activity callingActivity) {
        callingActivity_ = (TaskListener) callingActivity;
    }

    // Existing operations (unchanged)

    public abstract class UploadPicture extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class RequestDriverInfoViaRfid extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ChargeDriverCard extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            callingActivity_.onTaskFinished(response);
        }
    }

    // New operations for extended features

    public abstract class FetchChargeButtons extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_CHARGE_BUTTONS;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ManageChargeButton extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class FetchTransactionHistory extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_TRANSACTION_HISTORY;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class FetchDailyReport extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_DAILY_REPORT;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class FetchKioskOptions extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_KIOSK_OPTIONS;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ProcessKioskPurchase extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.PROCESS_KIOSK_PURCHASE;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ValidatePin extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.VALIDATE_PIN;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class FetchEntitlementTypes extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_ENTITLEMENT_TYPES;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class FetchStandardCards extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            response.operationType = OperationType.FETCH_STANDARD_CARDS;
            callingActivity_.onTaskFinished(response);
        }
    }

    public abstract class ManageStandardCard extends AsyncTask<Request, Void, Response> {
        protected void onPostExecute(Response response) {
            callingActivity_.onTaskFinished(response);
        }
    }
}
