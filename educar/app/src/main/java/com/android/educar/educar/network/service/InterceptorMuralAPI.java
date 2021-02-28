package com.android.educar.educar.network.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class InterceptorMuralAPI implements Interceptor {
    public static final String AUTHORIZATION = "Authorization";
    public final String TOKEN;

    public InterceptorMuralAPI(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader(AUTHORIZATION, TOKEN)
                .build();

        return chain.proceed(request);
    }
}
