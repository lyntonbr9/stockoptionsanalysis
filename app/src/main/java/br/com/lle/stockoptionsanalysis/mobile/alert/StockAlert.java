package br.com.lle.stockoptionsanalysis.mobile.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.lle.sata.mobile.alert.AlertaStop;
import br.com.lle.sata.mobile.alert.Notificacao;
import br.com.lle.stockoptionsanalysis.R;
import br.com.lle.stockoptionsanalysis.mobile.Constants;
import br.com.lle.stockoptionsanalysis.mobile.util.RedeUtil;

import static br.com.lle.sata.util.StringUtil.concat;
import static br.com.lle.stockoptionsanalysis.mobile.Constants.JSON_ALERTAS_STOP;

/**
 * Created by lynton on 29/08/2015.
 */
public class StockAlert {

    Context context;

    public StockAlert (Context context) {
        this.context = context;
    }

    public void stockAlert() {
        if (!RedeUtil.isNetworkAvailable(getContext())) {
            // TODO: ver melhor maneira de verificar a falta de conexão com a internet
            //emitAlert("Alerta: ", "NO INTERNET CONNECTION");
            return;
        }
        List<AlertaStop> ass = Constants.gson.fromJson(JSON_ALERTAS_STOP, new TypeToken<List<AlertaStop>>(){}.getType());
        if (ass != null) {
            List<String> msgs = Notificacao.getAlertMessages(ass);
            for (String msg : msgs) {
                if (msg != null && !msg.equals(""))
                    emitAlert("Alerta: ", msg);
            }
        }
    }

    private void emitAlert(String tagMsg, String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext().getApplicationContext())
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
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



}
