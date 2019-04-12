package com.experion.instagramauth.interfaces;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);

    void onTokenError(String auth_token);
}
