package br.com.lle.stockoptionsanalysis.mobile.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import br.com.lle.stockoptionsanalysis.R;
import br.com.lle.stockoptionsanalysis.mobile.interfaces.js.MainJavaScriptInterface;
import br.com.lle.stockoptionsanalysis.mobile.service.StockOptionsService;
import br.com.lle.stockoptionsanalysis.mobile.util.GCMUtil;
import br.com.lle.stockoptionsanalysis.mobile.webkit.StockOptionsChromeClient;

import static br.com.lle.stockoptionsanalysis.mobile.Constants.QUINZE_MIN_MILIS;

public class StockOptionsVWMainActivity extends Activity {

	private WebView webView;
	String regid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_options_main);
        this.webView = (WebView) findViewById(R.id.wvMain);
        this.webView.setWebChromeClient(new StockOptionsChromeClient(this));
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.webView.getSettings().setDefaultTextEncodingName("utf-8");
        this.webView.getSettings().setDomStorageEnabled(true);
        MainJavaScriptInterface jsMain = new MainJavaScriptInterface(this);
        this.webView.addJavascriptInterface(jsMain, "JSInterface");
        iniciarServico();
        //Intent serviceIntent = new Intent(getApplicationContext(), StockOptionsService.class);
        //startService(serviceIntent);
        if (savedInstanceState == null) {
            //this.webView.loadUrl("file:///android_asset/teste-js.html");
            this.webView.loadUrl("file:///android_asset/principal-professional.html");
            //this.webView.loadUrl("file:///android_asset/principal.html");
        }
			
	}

    private void iniciarServico() {
        // cria o servico de alerta
        PendingIntent serviceIntent = PendingIntent.getService(getApplicationContext(),
                0, new Intent(getApplicationContext(), StockOptionsService.class), 0);
        long firstTime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, firstTime, QUINZE_MIN_MILIS, serviceIntent);
    }

    @Deprecated
	public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // recupera o registro do dispositivo
                regid = GCMUtil.getGCMRegID(getApplicationContext());
                // retorna o registro do dispositivo
                return regid;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i("GCM onPostExecute", msg);
            }
        }.execute(null, null, null);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_options_main, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		this.webView.saveState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		this.webView.restoreState(savedInstanceState);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case R.id.sair:
		    	this.finish();
		    	System.exit(0);
		    	return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}

}
