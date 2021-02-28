package com.android.educar.educar.network.chamadas;

import android.content.Context;
import android.util.Log;

import com.android.educar.educar.network.service.APIService;
import com.android.educar.educar.network.service.ListaAnoLetivoAPI;
import com.android.educar.educar.network.service.ListaBimestreAPI;
import com.android.educar.educar.utils.Preferences;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnoLetivoChamada {

    private Context context;
    private APIService apiService;
    private Realm realm;
    private int paginaAtualAnoLetivo;
    private int paginaAtualBimetre;
    private Preferences preferences;

    public void configRealm() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public AnoLetivoChamada(Context context) {
        this.context = context;
        preferences = new Preferences(context);
        apiService = new APIService(preferences.getSavedString("token"));
        configRealm();
        paginaAtualAnoLetivo = 1;
        paginaAtualBimetre = 1;
    }

    public void recuperarAlunoLetivoAPI(final int paginaAtual) {
        Call<ListaAnoLetivoAPI> listaAnoLetivoAPICall = apiService.getAnoLetivoEndPoint().anosLetivos();
        listaAnoLetivoAPICall.enqueue(new Callback<ListaAnoLetivoAPI>() {
            @Override
            public void onResponse(Call<ListaAnoLetivoAPI> call, Response<ListaAnoLetivoAPI> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body().getResults());
                    realm.commitTransaction();
                    int pagina = paginaAtual;
                    if (response.body().getNext()!=null) {
                        pagina = pagina + 1;
                        recuperarAlunoLetivoAPI(pagina);
                    }
                    Log.i("RESPONSE", "ANOLETIVO RECUPERADOS");
                }
            }

            @Override
            public void onFailure(Call<ListaAnoLetivoAPI> call, Throwable t) {
                Log.i("ERRO API", t.getMessage());
            }
        });
    }

    public void recuperarBimestreAPI() {
        Call<ListaBimestreAPI> listaAnoLetivoAPICall = apiService.getBimestreEndPoint().bimestres();
        listaAnoLetivoAPICall.enqueue(new Callback<ListaBimestreAPI>() {
            @Override
            public void onResponse(Call<ListaBimestreAPI> call, Response<ListaBimestreAPI> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body().getResults());
                    realm.commitTransaction();
                    Log.i("RESPONSE", "BIMESTRES RECUPERADOS");
                }
            }

            @Override
            public void onFailure(Call<ListaBimestreAPI> call, Throwable t) {
                Log.i("ERRO API", t.getMessage());
            }
        });
    }
}
