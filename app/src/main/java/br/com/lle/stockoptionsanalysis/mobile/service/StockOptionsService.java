package br.com.lle.stockoptionsanalysis.mobile.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

import br.com.lle.sata.mobile.alert.AlertaStop;
import br.com.lle.sata.mobile.alert.Notificacao;
import br.com.lle.stockoptionsanalysis.R;
import br.com.lle.stockoptionsanalysis.mobile.Constants;
import br.com.lle.stockoptionsanalysis.mobile.util.LogUtil;

import static br.com.lle.sata.mobile.core.util.StringUtil.concat;
import static br.com.lle.stockoptionsanalysis.mobile.Constants.HORA_COMECO;
import static br.com.lle.stockoptionsanalysis.mobile.Constants.HORA_FIM;
import static br.com.lle.stockoptionsanalysis.mobile.Constants.JSON_ALERTAS_STOP;

/**
 * Created by lynton on 11/01/2015.
 */
public class StockOptionsService extends IntentService {

    public StockOptionsService() {
        super("StockOptionsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Calendar dataAtual = Calendar.getInstance();
        if (!isNetworkAvailable()) {
            emitAlert("Alerta: ", "NO INTERNET CONNECTION");
            return;
        }

        try {
            int horaAtual = dataAtual.get(Calendar.HOUR_OF_DAY);
            if (horaAtual >= HORA_COMECO && horaAtual <= HORA_FIM && !JSON_ALERTAS_STOP.equalsIgnoreCase("")) {
                List<AlertaStop> ass = Constants.gson.fromJson(JSON_ALERTAS_STOP, new TypeToken<List<AlertaStop>>(){}.getType());
                List<String> msgs = Notificacao.getAlertMessages(ass);
                for (String msg : msgs) {
                    emitAlert("Alerta: ", msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(e.getMessage());
        }
    }

    private void emitAlert(String tagMsg, String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setTicker(msg)
                        .setAutoCancel(false)
                                //.setOngoing(true)
                        .setContentTitle(msg)
                        .setContentText(concat(tagMsg, msg));
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        // Sets an ID for the notification
        int mNotificationId = (int) System.currentTimeMillis();
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
