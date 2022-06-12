package com.apps.inslibrary.interfaces;

public interface HttpListener<T> {
    void onError(String error);

    void onLogin(boolean isLoggedIn);

    void onSuccess(T result);
}
