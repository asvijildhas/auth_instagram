package com.experion.iglogin.activity;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.experion.iglogin.R;

import com.experion.iglogin.application_basics.BaseActivity;
import com.experion.iglogin.databinding.ActivityLoginBinding;
import com.experion.iglogin.dialog.AuthenticationDialog;
import com.experion.iglogin.interfaces.AuthenticationListener;
import com.experion.iglogin.model.InstagramUserDetails;
import com.experion.iglogin.util.AppPreferences;
import com.experion.iglogin.util.ApplicationUtil;
import com.experion.iglogin.util.Constants;
import com.experion.iglogin.util.Navigator;
import com.experion.iglogin.view_model.UserDetailsViewModel;
import com.experion.instagramauth.activity.AuthenticationActivity;


public class LoginActivity extends BaseActivity implements AuthenticationListener {

    private ActivityLoginBinding mLoginBinding;
    private ClickHandler mClickHandler;
    private Context mContext;

    private AuthenticationDialog authenticationDialog;
    private AppPreferences appPreferences = null;

    private String token = null;
    private String TAG = null;
    private AuthenticationListener authenticationListener;

    private UserDetailsViewModel userDetailsViewModel;

    private boolean isProgressing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        initBasics();
        initListener();
    }

    private void initBasics() {
        mContext = LoginActivity.this.getBaseContext();

        userDetailsViewModel = ViewModelProviders.of(LoginActivity.this).get(UserDetailsViewModel.class);
        authenticationListener = (AuthenticationListener) this;
        appPreferences = new AppPreferences(this);

        TAG = LoginActivity.class.getSimpleName();
    }

    private void initListener() {
        mClickHandler = new ClickHandler(mContext);
        mLoginBinding.setHandler(mClickHandler);
    }

    private void getUserInfoByAccessToken(String user_token) {
        if (ApplicationUtil.isNetworkAvailable(mContext)) {
            userDetailsViewModel.getUserBasicDetails(user_token).observe(LoginActivity.this, new Observer<InstagramUserDetails>() {
                @Override
                public void onChanged(InstagramUserDetails instagramUserDetails) {
                    if (instagramUserDetails != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.BUNDLE_KEY_USER_BASICS, instagramUserDetails);
                        Navigator.getInstance().navigate(LoginActivity.this, HomeActivity.class, bundle,
                                false, false);

                    }
                    isProgressing = false;
                    mLoginBinding.progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            isProgressing = false;
            ApplicationUtil.shortToast(mContext, getResources().getString(R.string.err_internet_failure));
        }

    }


    @Override
    public void onTokenReceived(String auth_token) {
//onTokenReceived

        if (auth_token == null)
            return;
        appPreferences.putInstagramToken(AppPreferences.TOKEN, auth_token);
        token = auth_token;

        getUserInfoByAccessToken(token);
    }

    public class ClickHandler {
        private Context mContext;

        public ClickHandler(Context mContext) {
            this.mContext = mContext;
        }

        public void onInstagramLogin(View view) {
            // displayAuthenticationDialog();
            appPreferences.clear();
            if (!isProgressing) {
                isProgressing = true;
                mLoginBinding.progressBar.setVisibility(View.VISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString(Constants.REDIRECT_URL, getResources().getString(R.string.redirect_url));
                bundle.putString(Constants.REQUEST_URL, getRequestUrl());

                Navigator.getInstance().navigateForResult(LoginActivity.this, AuthenticationActivity.class,
                        bundle, false, false, Constants.INTENT_KEY_AUTHENTICATION_IG);
            }

        }

    }

    private String getRequestUrl() {

        return getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                getResources().getString(R.string.client_id) +
                "&redirect_uri=" + getResources().getString(R.string.redirect_url) +
                "&response_type=token&display=touch&scope=public_content";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.INTENT_KEY_AUTHENTICATION_IG) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    token = data.getStringExtra(Constants.INTENT_KEY_ACCESS_TOKEN);
                    Log.e(TAG, "[token]:" + token);
                    authenticationListener.onTokenReceived(token);
                }
            } else if (resultCode == RESULT_CANCELED) {

                mLoginBinding.progressBar.setVisibility(View.GONE);
                isProgressing = false;
            }
        }
    }


    private void displayAuthenticationDialog() {
        authenticationDialog = new AuthenticationDialog(LoginActivity.this, LoginActivity.this);
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();
    }
}
