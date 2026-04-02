package com.applications.tinytonwe.drivermodificationappversion2.Common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Build;

public class NfcHelper {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    public NfcHelper(Activity activity) {
        mAdapter = NfcAdapter.getDefaultAdapter(activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPendingIntent = PendingIntent.getActivity(activity, 0,
                    new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE);
        } else {
            mPendingIntent = PendingIntent.getActivity(activity, 0,
                    new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_IMMUTABLE);
        }

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef};
        mTechLists = new String[][]{new String[]{NfcA.class.getName()}};
    }

    public void enableForegroundDispatch(Activity activity) {
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(activity, mPendingIntent, mFilters, mTechLists);
        }
    }

    public void disableForegroundDispatch(Activity activity) {
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(activity);
        }
    }

    /**
     * Reads the hex UID from an NFC intent. Returns null if the intent is not an NFC tech discovery.
     */
    public static String readCardHex(Intent intent) {
        if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            return null;
        }

        try {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tagFromIntent == null) return null;

            byte[] tagUid = tagFromIntent.getId();
            String b0X = String.format("%02x", tagUid[0] & 0xff);
            String b1X = String.format("%02x", tagUid[1] & 0xff);
            String b2X = String.format("%02x", tagUid[2] & 0xff);
            String b3X = String.format("%02x", tagUid[3] & 0xff);

            return b3X + b2X + b1X + b0X;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a hex card ID string to a long value.
     */
    public static long hexToLong(String hexId) {
        return Long.parseLong(hexId, 16);
    }
}
