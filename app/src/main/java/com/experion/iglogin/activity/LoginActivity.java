package com.experion.iglogin.activity;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.experion.iglogin.R;

import com.experion.iglogin.application_basics.BaseActivity;
import com.experion.iglogin.databinding.ActivityLoginBinding;
import com.experion.iglogin.fragment.LoginFragment;
import com.experion.instagramauth.interfaces.AuthenticationListener;
import com.experion.iglogin.interfaces.OnButtonClickCallBack;
import com.experion.iglogin.util.AppPreferences;
import com.experion.iglogin.util.Constants;
import com.experion.iglogin.util.Navigator;
import com.experion.instagramauth.fragment.AuthenticationFragment;


public class LoginActivity extends BaseActivity implements AuthenticationListener, OnButtonClickCallBack {
    private ActivityLoginBinding mLoginBinding;

    private AppPreferences appPreferences = null;
    private String token = null;
    private Context mContext;
    private AuthenticationFragment authFragment;
    private LoginFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initBasics();
        changeFragment(loginFragment, true);


    }

    private void initBasics() {
        mContext = LoginActivity.this.getBaseContext();
        authFragment = new AuthenticationFragment();
        loginFragment = new LoginFragment();
        appPreferences = new AppPreferences(mContext);
    }

    private void changeFragment(Fragment fragment, boolean addToBackStack) {
        try {
            Navigator.getInstance().loadFragment(LoginActivity.this, R.id.fragment_container, fragment, addToBackStack);
        } catch (Exception e) {
            Log.e(LoginActivity.class.getSimpleName(), "Exception:" + e.getMessage());
        }
    }


    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putInstagramToken(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_USER_TOKEN, token);
        Navigator.getInstance().navigate(LoginActivity.this, HomeActivity.class, bundle, true, true);
    }

    @Override
    public void onTokenError(String auth_token) {

    }

    @Override
    public void onButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.REDIRECT_URL, getResources().getString(R.string.redirect_url));
        bundle.putString(Constants.REQUEST_URL, getRequestUrl());
        authFragment.setArguments(bundle);
        changeFragment(authFragment, false);
    }

    private String getRequestUrl() {

        return getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                getResources().getString(R.string.client_id) +
                "&redirect_uri=" + getResources().getString(R.string.redirect_url) +
                "&response_type=token&display=touch&scope=public_content";
    }

}
