package edu.uic.cs478.mmcclo5.app_a2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewFragment extends Fragment
{
    private WebView web = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.webview, container, false);
        String url = getArguments().getString("site");

        if(url != null)
        {
            web = (WebView)v.findViewById(R.id.web);
            web.loadUrl(url);
            web.setWebViewClient(new WebViewClient());
        }
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


}
