package com.koshertech.su.net;

public interface ResponseListener<T> {
    void onResponse(T response);
}
