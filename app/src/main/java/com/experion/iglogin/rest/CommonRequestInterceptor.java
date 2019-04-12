package com.experion.iglogin.rest;

/**
 * Common request interceptor
 */


import com.experion.iglogin.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Common Request Header Manipulation class.
 * Intercept all the request and append the given headers.
 */
class CommonRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .header(Constants.HEADER_DEVICE_PLATFORM, Constants.HEADER_DEVICE_PLATFORM_VAL)
                .header(Constants.HEADER_CONTENT, Constants.HEADER_CONTENT_VAL)
                .header(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VAL)
                .build();
        return chain.proceed(request);
    }
}
