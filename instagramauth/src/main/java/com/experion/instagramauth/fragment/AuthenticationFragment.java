package com.experion.instagramauth.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.experion.instagramauth.R;
import com.experion.instagramauth.databinding.FragmentAuthenticationBinding;
import com.experion.instagramauth.interfaces.AuthenticationListener;
import com.experion.instagramauth.util.Constants;

public class AuthenticationFragment extends Fragment {

    private String mRedirectURL;
    private String mRequestUrl;
    private String access_token = "";

    private FragmentAuthenticationBinding authenticationBinding;
    View rootView;
    private AuthenticationListener authenticationListener;

    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mRedirectURL = bundle.getString(Constants.REDIRECT_URL);
                mRequestUrl = bundle.getString(Constants.REQUEST_URL);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authenticationBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_authentication, container, false);
        rootView = authenticationBinding.getRoot();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initBasics();
        initializeWebView();
    }

    private void initBasics() {
        authenticationListener = (AuthenticationListener) getActivity();
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

            authenticationBinding.progressBar.setVisibility(View.GONE);

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
                authenticationListener.onTokenReceived(access_token);
            } else if (url.contains("?error")) {
                authenticationListener.onTokenError(Constants.UNKNOWN_ERROR);
            }
        }
    };


}
