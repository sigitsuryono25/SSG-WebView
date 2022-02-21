package com.surelabsid.lauwba.ssg;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import am.appwise.components.ni.NoInternetDialog;

public class MainActivity extends AppCompatActivity {
    ConnectivityManager cm;
    WebView webView;
    SwipeRefreshLayout swipe;
    String link = "https://green.server4111.com/daftar/index.php/admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        swipe = findViewById(R.id.swipe);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }


        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipe.setRefreshing(false);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                link = url;
                return super.shouldOverrideUrlLoading(view, url);
            }

        });


        NoInternetDialog.Builder mBuilder = new NoInternetDialog.Builder(this);
        mBuilder.setBgGradientCenter(getResources().getColor(R.color.blueicon));
        mBuilder.setBgGradientEnd(getResources().getColor(R.color.blueicon));
        mBuilder.setBgGradientStart(getResources().getColor(R.color.blueicon));
        mBuilder.setButtonColor(getResources().getColor(R.color.purple_500));
        mBuilder.setButtonTextColor(getResources().getColor(R.color.black));
        NoInternetDialog mNoIntenetDialog = mBuilder.build();

        mNoIntenetDialog.showDialog();

        if (checkPermission())
            checkInternet();


        swipe.setOnRefreshListener(() -> {
            webView.loadUrl(link);
            Log.d("LINK", link);
            swipe.setRefreshing(true);
        });
    }

    private boolean checkPermission() {
        String permission = Manifest.permission.ACCESS_WIFI_STATE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void checkInternet() {
        cm = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            webView.loadUrl(link);
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}