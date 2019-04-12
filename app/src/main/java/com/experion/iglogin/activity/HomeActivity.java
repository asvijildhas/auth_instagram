package com.experion.iglogin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.experion.iglogin.R;
import com.experion.iglogin.databinding.ActivityHomeBinding;
import com.experion.iglogin.model.InstagramUserDetails;
import com.experion.iglogin.util.Constants;
import com.experion.iglogin.util.ImageLoaderSupport;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding mActivityHomeBinding;
    private InstagramUserDetails mBasicDetails;
    private ImageLoaderSupport loaderSupport;
    private Context mCtx;

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
            mBasicDetails = (InstagramUserDetails) bundle.getSerializable(Constants.BUNDLE_KEY_USER_BASICS);
            mActivityHomeBinding.setUserData(mBasicDetails);
            loaderSupport.loadImage(mCtx, mBasicDetails.getData().getProfilePicture(),
                    mActivityHomeBinding.userLogo,
                    R.drawable.ic_account_circle,
                    mActivityHomeBinding.progressBar);
        }
    }

    private void initBasics() {
        loaderSupport = new ImageLoaderSupport();
        mCtx = HomeActivity.this.getBaseContext();
    }
}
