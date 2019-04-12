package com.experion.iglogin.interfaces;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}
