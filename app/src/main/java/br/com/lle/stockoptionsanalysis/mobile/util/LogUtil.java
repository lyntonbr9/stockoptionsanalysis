package br.com.lle.stockoptionsanalysis.mobile.util;

import android.util.Log;
import br.com.lle.stockoptionsanalysis.mobile.Constants;

/**
 * Created by lynton on 25/01/2015.
 */
public class LogUtil {

    public static void log(String msg) {
        Log.d(Constants.LOG_APP_NAME, msg);
    }

    public static void logError(String msg) {
        Log.e(Constants.LOG_APP_NAME, msg);
    }

    public static void logInfo(String msg) {
        Log.i(Constants.LOG_APP_NAME, msg);
    }

}
