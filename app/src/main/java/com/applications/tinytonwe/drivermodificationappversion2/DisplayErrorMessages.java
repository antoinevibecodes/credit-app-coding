package com.applications.tinytonwe.drivermodificationappversion2;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 5/21/2015.
 */
public class DisplayErrorMessages {

    private DisplayErrorMessages(){}

    public static void displayError(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }
}
