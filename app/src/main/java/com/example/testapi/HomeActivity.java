package com.example.testapi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private WebView mywebView;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    String apiUrl = "https://babui.com.bd/";
    String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressBar = findViewById(R.id.progressBar1);
        LoadUrl(apiUrl);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        RefreshUrl();
                    }
                }, 3000);
            }
        });
    }

    public void LoadUrl(String url) {
        String webUrl = url;
        mywebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebView.getSettings().setLoadWithOverviewMode(true);
        mywebView.getSettings().setUseWideViewPort(true);
        mywebView.getSettings().setSupportZoom(true);
        mywebView.getSettings().setDisplayZoomControls(false);
        mywebView.getSettings().setBuiltInZoomControls(true);
        //webview performance
        mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mywebView.getSettings().setAppCacheEnabled(true);
//        mywebView.getSettings().setCacheMode(hasConnection(ContactOnlineActivity.this) ?WebSettings.LOAD_NO_CACHE: WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        // WebView.setWebContentsDebuggingEnabled(true);

        mywebView.setWebChromeClient(new MyChrome());
        mywebView.loadUrl(webUrl);
        mywebView.setWebViewClient(new MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(view.VISIBLE);
            currentUrl = url;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            refreshLayout.setRefreshing(false);
            if (url.contains("android_asset")) {
                super.onPageFinished(view, url);
            }else{
                currentUrl = url;
                super.onPageFinished(view, url);
                progressBar.setVisibility(view.GONE);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Toast.makeText(getApplicationContext(), "Error Occured, Please check network Connectivity", Toast.LENGTH_LONG).show();
            mywebView.loadUrl("file:///android_asset/error.html");
            progressBar.setVisibility(view.GONE);
//            Toast.makeText(getApplicationContext(),"Error Occured, Please check network Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private class MyChrome extends WebChromeClient {

        //        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
//        protected FrameLayout mFullscreenContainer;
//        private int mOriginalOrientation;
//        private int mOriginalSystemUiVisibility;

        MyChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
//            if (mCustomView == null) {
//                return null;
//            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }
    }

    //refresh webview in fragment
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void RefreshUrl() {
        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebView.getSettings().setLoadWithOverviewMode(true);
        mywebView.getSettings().setUseWideViewPort(true);
        mywebView.getSettings().setBuiltInZoomControls(true);
        //webview performance
        mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        mywebView.getSettings().setCacheMode(hasConnection(ContactOnlineActivity.this) ?WebSettings.LOAD_NO_CACHE: WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        // mywebView.getSettings().setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        WebView.setWebContentsDebuggingEnabled(true);

        mywebView.loadUrl(currentUrl);
        mywebView.setWebViewClient(new MyWebViewClient());
    }

//    private void webViewGoBack() {
//        mywebView.goBack();
//    }


    //check conection
    public boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activieinfo = cm.getActiveNetworkInfo();
        if (activieinfo != null && activieinfo.isConnected()) {
            return true;
        }
        return false;
    }
@Override
    public void onBackPressed(){
    if(mywebView.canGoBack()){
        if(apiUrl==currentUrl){
            super.onBackPressed();
        }else {
            mywebView.goBack();
        }

    }else {
        super.onBackPressed();
    }
    }
}