package br.com.lle.stockoptionsanalysis.mobile.util;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import static br.com.lle.stockoptionsanalysis.mobile.util.LogUtil.log;
import static br.com.lle.stockoptionsanalysis.mobile.util.LogUtil.logError;

/**
 * Created by lynton on 25/01/2015.
 */
public class GCMUtil {

    public static final String PROJECT_NUMBER = "508117163167";

    public static String getGCMRegID(Context context) {
        GoogleCloudMessaging gcm = null;
        String regID = "";
        String msg = "";
        try {
            gcm = GoogleCloudMessaging.getInstance(context);
            regID = gcm.register(PROJECT_NUMBER);
            msg = "Device registered, registration ID=" + regID;
            log("GCM: " + msg);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            logError("GCM: " + msg);
        }
        return regID;
    }
}
