package br.com.lle.stockoptionsanalysis.mobile.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

//import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.com.lle.stockoptionsanalysis.R;

import static br.com.lle.stockoptionsanalysis.mobile.util.LogUtil.log;

@Deprecated
public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
    public GcmMessageHandler() {

        //super("GcmMessageHandler");
        super("");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showNotification();
        log("GCM: Received : (" + messageType + ")  " + extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);
        */

    }

    public void showNotification(){

        handler.post(new Runnable() {

            public void run() {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setTicker(mes)
                                .setAutoCancel(false)
                                //.setOngoing(true)
                                .setContentTitle(mes)
                                .setContentText("Alerta: " + mes);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                // Sets an ID for the notification
                int mNotificationId = (int) System.currentTimeMillis();
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        });

    }


}
