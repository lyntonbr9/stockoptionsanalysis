package br.com.lle.stockoptionsanalysis.mobile.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.lle.sata.alert.domain.AlertaStop;
import br.com.lle.sata.mobile.alert.Notificacao;
import br.com.lle.sata.util.http.HTTPSata;
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

    public static String TAG = StockAlert.class.getSimpleName();

    public StockAlert (Context context) {
        this.context = context;
    }

    public void stockAlert() {
        if (!RedeUtil.isNetworkAvailable(getContext())) {
            // TODO: ver melhor maneira de verificar a falta de conexão com a internet
            //emitAlert("Alerta: ", "NO INTERNET CONNECTION");
            return;
        }

        //PETR4 - https://cotacoes.economia.uol.com.br/ws/asset/484/intraday
        //VALE5 - https://cotacoes.economia.uol.com.br/ws/asset/687/intraday
        /*
        String conteudo = HTTPSata.GET("https://cotacoes.economia.uol.com.br/ws/asset/484/intraday", null);
        Long tempoEmMillis = Long.parseLong(conteudo.substring(17, 30));
        BigDecimal preco = new BigDecimal(conteudo.substring(39, 44).replaceAll(",", ""));
        Date timestamp = new Date(tempoEmMillis);
        Log.d(TAG, tempoEmMillis.toString());
        Log.d(TAG, timestamp.toString());
        Log.d(TAG, preco.toString());
        String msg = "";
        if (preco.compareTo(new BigDecimal("14")) > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            msg = "PETR4 - " + dateFormat.format(timestamp) + " - " + preco.toString();
            emitAlert("Alerta: ", msg);
        }
        */
        /*
        List<AlertaStop> ass = Constants.gson.fromJson(JSON_ALERTAS_STOP, new TypeToken<List<AlertaStop>>(){}.getType());
        if (ass != null) {
            List<String> msgs = Notificacao.getAlertMessages(ass);
            for (String msg : msgs) {
                if (msg != null && !msg.equals(""))
                    emitAlert("Alerta: ", msg);
            }
        }*/
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
