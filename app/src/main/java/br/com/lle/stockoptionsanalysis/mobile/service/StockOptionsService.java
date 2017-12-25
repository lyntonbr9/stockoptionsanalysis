package br.com.lle.stockoptionsanalysis.mobile.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;

import br.com.lle.stockoptionsanalysis.mobile.Constants;
import br.com.lle.stockoptionsanalysis.mobile.alert.StockAlert;

import static br.com.lle.stockoptionsanalysis.mobile.util.LogUtil.logError;

/**
 * Created by lynton on 11/01/2015.
 */
public class StockOptionsService extends IntentService {

    public StockOptionsService() {
        super("StockOptionsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        Calendar dataAtual = Calendar.getInstance();

        StockAlert sa = new StockAlert(this);

        try {
            int horaAtual = dataAtual.get(Calendar.HOUR_OF_DAY);
            // TODO: Lembrar de descomentar a hora para ir em producao
            //if (horaAtual >= Constants.HORA_COMECO && horaAtual <= Constants.HORA_FIM && !Constants.JSON_ALERTAS_STOP.equalsIgnoreCase("")) {
            if (horaAtual >= Constants.HORA_COMECO && horaAtual <= Constants.HORA_FIM) {
                sa.stockAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logError(e.getMessage());
        }
        */
    }

}
