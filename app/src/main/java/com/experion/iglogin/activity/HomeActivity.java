package com.experion.iglogin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.experion.iglogin.R;
import com.experion.iglogin.databinding.ActivityHomeBinding;
import com.experion.iglogin.fragment.LoginFragment;
import com.experion.iglogin.model.InstagramUserDetails;
import com.experion.iglogin.util.ApplicationUtil;
import com.experion.iglogin.util.Constants;
import com.experion.iglogin.util.ImageLoaderSupport;
import com.experion.iglogin.util.Navigator;
import com.experion.iglogin.view_model.UserDetailsViewModel;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding mActivityHomeBinding;
    private InstagramUserDetails mBasicDetails;
    private ImageLoaderSupport loaderSupport;
    private Context mCtx;
    private UserDetailsViewModel userDetailsViewModel;
    private String user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initBasics();
        getBundleData();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_token = bundle.getString(Constants.BUNDLE_KEY_USER_TOKEN);
            getUserInfoByAccessToken(user_token);
        }
    }

    private void initBasics() {
        userDetailsViewModel = ViewModelProviders.of(HomeActivity.this).get(UserDetailsViewModel.class);

        loaderSupport = new ImageLoaderSupport();
        mCtx = HomeActivity.this.getBaseContext();
    }

    private void getUserInfoByAccessToken(String user_token) {
        if (ApplicationUtil.isNetworkAvailable(mCtx)) {
            userDetailsViewModel.getUserBasicDetails(user_token).observe(HomeActivity.this, new Observer<InstagramUserDetails>() {
                @Override
                public void onChanged(InstagramUserDetails instagramUserDetails) {
                    if (instagramUserDetails != null) {

                        mBasicDetails = instagramUserDetails;
                        setupUserDetails(mBasicDetails);


                    }

                }
            });
        } else {

            ApplicationUtil.shortToast(mCtx, getResources().getString(R.string.err_internet_failure));
        }

    }

    private void setupUserDetails(InstagramUserDetails mBasicDetails) {
        mActivityHomeBinding.setUserData(mBasicDetails);
        loaderSupport.loadImage(mCtx, mBasicDetails.getData().getProfilePicture(),
                mActivityHomeBinding.userLogo,
                R.drawable.ic_account_circle,
                mActivityHomeBinding.progressBar);
    }
}
