package com.android.educar.educar.network.helpers;


import com.android.educar.educar.model.modelalterado.TokenAPI;
import com.android.educar.educar.model.modelalterado.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenEndPoint {
    @POST("login/")
    Call<TokenAPI> login(@Body User usuario);
}
