package com.experion.instagramauth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.experion.instagramauth.R;
import com.experion.instagramauth.databinding.ActivityAuthenticationBinding;

import com.experion.instagramauth.util.Constants;


public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding authenticationBinding;

    private String mRedirectURL;
    private String mRequestUrl;
    String access_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);

        initBasics();
        getBundleData();
        initializeWebView();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mRedirectURL = bundle.getString(Constants.REDIRECT_URL);
            mRequestUrl = bundle.getString(Constants.REQUEST_URL);
        }

    }

    private void initBasics() {
    }


    private void initializeWebView() {
        authenticationBinding.webView.getSettings().setJavaScriptEnabled(true);
        WebSettings mWebSettings = authenticationBinding.webView.getSettings();
        mWebSettings.setSavePassword(false);
        mWebSettings.setSaveFormData(false);
        authenticationBinding.webView.clearCache(true);
        authenticationBinding.webView.clearHistory();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }


        authenticationBinding.webView.loadUrl(mRequestUrl);
        authenticationBinding.webView.setWebViewClient(webViewClient);


    }


    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(mRedirectURL)) {


                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("access_token=")) {
                Uri uri = Uri.EMPTY.parse(url);
                access_token = uri.getEncodedFragment();
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                //Log.e("access_token", "access_token:" + access_token);
                returnIntentSetup(access_token, true);
            } else if (url.contains("?error")) {
                //Log.e("access_token", "getting error fetching access token");
                returnIntentSetup(access_token, true);

            }
        }
    };

    @Override
    public void onBackPressed() {
        returnIntentSetup(access_token, false);
    }

    private void returnIntentSetup(String access_token, boolean isSuccess) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.INTENT_KEY_ACCESS_TOKEN, access_token);
        if (isSuccess)
            setResult(RESULT_OK, returnIntent);
        else
            setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
