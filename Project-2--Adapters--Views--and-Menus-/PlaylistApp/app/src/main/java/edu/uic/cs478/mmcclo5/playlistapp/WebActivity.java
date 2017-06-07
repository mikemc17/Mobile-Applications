package edu.uic.cs478.mmcclo5.playlistapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Mike on 2/19/2017.
 *
 * this code taken from a tutorial from https://www.mkyong.com/android/android-webview-example/
 *
 */

public class WebActivity extends Activity
{
    private WebView webView;
    String url;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        url = getIntent().getExtras().getString("url");
        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }


}
