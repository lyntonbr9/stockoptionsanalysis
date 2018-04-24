package br.com.lle.stockoptionsanalysis.mobile.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import br.com.lle.stockoptionsanalysis.R;

public class SOAFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = SOAFirebaseMessagingService.class.getName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String titulo = remoteMessage.getData().get("titulo");
        String mensagem = "Alerta: " + remoteMessage.getData().get("mensagem");
        // ticker para uso da configuracoes de acessibilidade
        String ticker = titulo + " " + mensagem;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setTicker(ticker)
                        .setAutoCancel(false)
                        //.setOngoing(true)
                        .setContentTitle(titulo)
                        .setContentText(mensagem);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        // Sets an ID for the notification
        int mNotificationId = (int) System.currentTimeMillis();
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
