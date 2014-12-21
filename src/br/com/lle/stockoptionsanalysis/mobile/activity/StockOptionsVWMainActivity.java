package br.com.lle.stockoptionsanalysis.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import br.com.lle.stockoptionsanalysis.R;
import br.com.lle.stockoptionsanalysis.mobile.interfaces.MainJavaScriptInterface;
import br.com.lle.stockoptionsanalysis.mobile.webkit.StockOptionsChromeClient;

public class StockOptionsVWMainActivity extends Activity {

private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_stock_options_main);
			this.webView = (WebView) findViewById(R.id.wvMain);
			this.webView.setWebChromeClient(new StockOptionsChromeClient(this));
			this.webView.getSettings().setJavaScriptEnabled(true);
			this.webView.getSettings()
					.setJavaScriptCanOpenWindowsAutomatically(true);
			this.webView.getSettings().setDefaultTextEncodingName("utf-8");
			MainJavaScriptInterface jsMain = new MainJavaScriptInterface(this);
			this.webView.addJavascriptInterface(jsMain, "JSInterface");
//			this.webView.loadUrl("file:///android_asset/line.html");
			if (savedInstanceState == null) {
				this.webView.loadUrl("file:///android_asset/principal.html");
			}
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
