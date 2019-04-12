package com.experion.iglogin.rest;


import com.experion.iglogin.model.InstagramUserDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET(ApiEndUrl.GET_USER_SELF)
    Call<InstagramUserDetails> getUserDetails(@Query("access_token") String user_token);
}
