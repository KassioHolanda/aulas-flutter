package com.android.educar.educar.network.helpers;

import com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes;
import com.android.educar.educar.network.service.ListaAlunoFrequenciaMesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlunoFrequenciaMesEndPoint {

    @GET("alunofrequenciames/")
    Call<ListaAlunoFrequenciaMesAPI> alunosFrequenciaMes();

    @GET("alunofrequenciames/{id}")
    Call<AlunoFrequenciaMes> getAlunoFrequenciaMes(@Path("id") Long id);

    @GET("alunofrequenciames/matricula={matricula}/")
    Call<List<AlunoFrequenciaMes>> alunoFrequenciaMes(@Path("matricula") Long matricula);

    @GET("alunofrequenciames/")
    Call<ListaAlunoFrequenciaMesAPI> alunosFrequenciaMes(@Query("page") int page);

    @POST("alunofrequenciames/")
    Call<AlunoFrequenciaMes> publicarAlunoFrequenciaMes(@Body AlunoFrequenciaMes alunoFrequenciaMes);

    @PUT("alunofrequenciames/id={id}/")
    Call<AlunoFrequenciaMes> atualizarAlunoFrequenciaMes(@Path("id") Long id, @Body AlunoFrequenciaMes alunoFrequenciaMes);

}
