package hcx.global.hcxglobal;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private GifImageView gifImageView;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "lastURLPref";
    public static String lastVisitedURL = "https://hcx.global";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        gifImageView = (GifImageView) findViewById(R.id.loadergifIV);

        mWebView = (WebView) findViewById(R.id.web_view);
        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView,true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUserAgentString("Chrome/58.0.0.0 Mobile");
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setVisibility(View.GONE);
        mWebView.setPadding(0, 0, 0, 0);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains("lastURLKey")) {
            lastVisitedURL = sharedpreferences.getString("lastURLKey", "https://hcx.global");
        }

        renderWebPage(lastVisitedURL);
    }

    protected void renderWebPage(String urlToRender){
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                // Do something when page loading finished
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("lastURLKey", url);
                editor.commit();
                mWebView.setVisibility(View.VISIBLE);
                //Toast.makeText(mContext,url,Toast.LENGTH_LONG).show();
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress){
                mProgressBar.setProgress(newProgress);
                if(newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);
                }
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(urlToRender);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }



}