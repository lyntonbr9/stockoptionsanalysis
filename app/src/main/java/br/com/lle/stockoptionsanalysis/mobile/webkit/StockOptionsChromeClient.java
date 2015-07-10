package br.com.lle.stockoptionsanalysis.mobile.webkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class StockOptionsChromeClient extends WebChromeClient {

	private Context ctx;
	private final String APP_NAME = "Stock Options Analysis";
	
	public StockOptionsChromeClient(Context context) {
		super();
		this.ctx = context;
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
    {
		new AlertDialog.Builder(ctx)
            .setTitle(APP_NAME)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false)
            .create()
            .show();
		return true;
    }
}
