package com.experion.iglogin.view_model;

import android.util.Log;

import com.experion.iglogin.model.InstagramUserDetails;
import com.experion.iglogin.rest.ApiInterface;
import com.experion.iglogin.rest.RestService;
import com.experion.iglogin.util.Constants;
import com.google.gson.Gson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsViewModel extends ViewModel {

    private MutableLiveData<InstagramUserDetails> responseData;

    public LiveData<InstagramUserDetails> getUserBasicDetails(String token) {

        if (responseData == null) {
            responseData = new MutableLiveData<>();
        }
        getUserDetails(token);
        return responseData;
    }

    public void getUserDetails(String user_token) {

        ApiInterface api = RestService.getApi(Constants.INSTAGRAM_SELF_URL);
        if (api == null) {
        } else {
            Call<InstagramUserDetails> call = api.getUserDetails(user_token);
            call.enqueue(new Callback<InstagramUserDetails>() {
                @Override
                public void onResponse(Call<InstagramUserDetails> call, Response<InstagramUserDetails> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            responseData.setValue(response.body());
                            //remove memory
                            populateNullResponse();
                        } else {
                            populateNullResponse();
                        }
                    } else {
                        populateNullResponse();
                    }

                }

                @Override
                public void onFailure(Call<InstagramUserDetails> call, Throwable t) {
                    populateNullResponse();
                }
            });
        }
    }

    private void populateNullResponse() {
        responseData.setValue(null);
        responseData = null;
    }
}
