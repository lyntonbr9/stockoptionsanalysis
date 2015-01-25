package br.com.lle.stockoptionsanalysis.mobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.lle.sata.mobile.core.interfaces.IBuscaCotacao;
import br.com.lle.sata.mobile.core.robo.BVMFBuscaCotacao;
import br.com.lle.stockoptionsanalysis.R;

/**
 * Created by lynton on 11/01/2015.
 */
public class StockOptionsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int horaComeco = 10; // as 10 horas comeca
        int horaFim = 17; // termina as 17 hs
        int QUINZE_MIN_MILIS = 15*60*1000;
        BigDecimal precoMinDisparo = new BigDecimal("18.60");
        BigDecimal precoMaxDisparo = new BigDecimal("21.20");
        // TODO Auto-generated method stub
        Calendar dataAtual = Calendar.getInstance();
        int i = 1;
        // no loop
        while (i > 0) {
            try {
                int horaAtual = dataAtual.get(Calendar.HOUR_OF_DAY);
                if (horaAtual >= horaComeco && horaAtual <= horaFim) {
                    IBuscaCotacao bs = new BVMFBuscaCotacao();
                    String strCotacao = bs.getCotacao("VALE5").replaceAll(",",".");
                    BigDecimal cotacao = new BigDecimal(strCotacao);
                    // se a cotacao estiver menor do que o preco de disparo
                    // menos a
                    // variacao
                    if (cotacao.compareTo(precoMinDisparo) == -1) {
                        // faz o alerta
                        System.out.println("Alerta VALE5 STOP LOSS: " + cotacao);
                    }
                    if (cotacao.compareTo(precoMaxDisparo) >= 0) {
                        // faz o alerta
                        System.out.println("Alerta VALE5 STOP GAIN: " + cotacao);
                    }
                    emitAlert("VALE5");
                }
                i--; // desconta do contador
                Thread.sleep(2000); // a thread descansa por 2 segundos
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return Service.START_NOT_STICKY;
    }

    private void emitAlert(String mes) {
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
