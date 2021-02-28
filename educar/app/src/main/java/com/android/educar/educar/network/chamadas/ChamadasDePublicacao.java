package com.android.educar.educar.network.chamadas;

import android.content.Context;
import android.util.Log;

import com.android.educar.educar.model.modelalterado.AlunoFrequenciaMes;
import com.android.educar.educar.model.modelalterado.AlunoNotaMes;
import com.android.educar.educar.model.modelalterado.Ocorrencia;
import com.android.educar.educar.network.service.APIService;
import com.android.educar.educar.utils.Preferences;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadasDePublicacao {

    private Context context;
    private Realm realm;
    private APIService apiService;
    private Preferences preferences;

    public void configRealm() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public ChamadasDePublicacao(Context context) {
        this.context = context;
        preferences = new Preferences(context);
        apiService = new APIService(preferences.getSavedString("token"));
        configRealm();
    }

//    METODOS PARA PUBLICAÇÃO DO MODELO ALUNONOTAMES

    public void verificarAlunoNotaMesParaPublicar() {
        RealmResults<AlunoNotaMes> alunoNotaMes = realm.where(AlunoNotaMes.class).findAll();
        if (alunoNotaMes != null) {
            List<AlunoNotaMes> alunoNotaMes1 = realm.copyFromRealm(alunoNotaMes);
            for (AlunoNotaMes alunoNotaMes2 : alunoNotaMes1) {
                if (alunoNotaMes2.isAlterado() || alunoNotaMes2.isNovo()) {
                    alterarParaModeloDePublicacaoNota(alunoNotaMes2);
                }
            }
        }
    }

    private void alterarParaModeloDePublicacaoNota(AlunoNotaMes alunoNotaMes2) {
        com.android.educar.educar.model.modeloriginal.AlunoNotaMes notaMes =
                new com.android.educar.educar.model.modeloriginal.AlunoNotaMes(alunoNotaMes2.getNota(), (long) alunoNotaMes2.getSequencia(), alunoNotaMes2.getBimestre().getId(),
                        alunoNotaMes2.getDisciplinaAluno(), null, null, null, false, null, alunoNotaMes2.getTipoLancamentoNota(),
                        alunoNotaMes2.getAnoLetivo().getId(), alunoNotaMes2.getMatricula(), alunoNotaMes2.getUnidade(), alunoNotaMes2.getDisciplina().getId(), null, alunoNotaMes2.getDatahora(), alunoNotaMes2.getUsuario());

        if (alunoNotaMes2.isNovo()) {
            notaMes.setId(null);
            publicarAlunosNotaMes(notaMes, alunoNotaMes2);
        } else if (alunoNotaMes2.isAlterado()) {
            notaMes.setId(alunoNotaMes2.getId());
            atualizarAlunoNotaMes(notaMes, alunoNotaMes2);
        }

    }

    private void publicarAlunosNotaMes(com.android.educar.educar.model.modeloriginal.AlunoNotaMes notaMes, final AlunoNotaMes alunoNotaMes2) {
        Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> alunoNotaMesCall = apiService.getAlunoNotaMesEndPoint().publicarAlunoNotaMes(notaMes);
        alunoNotaMesCall.enqueue(new Callback<com.android.educar.educar.model.modeloriginal.AlunoNotaMes>() {
            @Override
            public void onResponse(Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> call, Response<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    alunoNotaMes2.setNovo(false);
                    realm.copyToRealmOrUpdate(alunoNotaMes2);
                    realm.commitTransaction();
                    Log.i("publicado", "Log:  " + response.message());
                }
            }

            @Override
            public void onFailure(Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> call, Throwable t) {
                Log.i("erro de publicacao", "Log:  " + t.getMessage());
            }
        });
    }

    private void atualizarAlunoNotaMes(com.android.educar.educar.model.modeloriginal.AlunoNotaMes notaMes, final AlunoNotaMes alunoNotaMes2) {
        Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> alunoNotaMesCall = apiService.getAlunoNotaMesEndPoint().atualizarAlunoNotaMes(notaMes.getId(), notaMes);
        alunoNotaMesCall.enqueue(new Callback<com.android.educar.educar.model.modeloriginal.AlunoNotaMes>() {
            @Override
            public void onResponse(Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> call, Response<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    alunoNotaMes2.setAlterado(false);
                    realm.copyToRealmOrUpdate(alunoNotaMes2);
                    realm.commitTransaction();
                    Log.i("publicado", "Log:  " + response.message());
                }
            }

            @Override
            public void onFailure(Call<com.android.educar.educar.model.modeloriginal.AlunoNotaMes> call, Throwable t) {
                Log.i("erro de publicacao", "Log:  " + t.getMessage());
            }
        });
    }

//    METODOS PARA PUBLICAÇÃO DO MODELO OCORRENCIA

    private void publicarOcorrencia(Ocorrencia ocorrencia, final Ocorrencia ocorrenciaAntiga) {
        Call<Ocorrencia> ocorrenciaCall = apiService.getOcorrenciaEndPoint().postOcorrencia(ocorrencia);
        ocorrenciaCall.enqueue(new Callback<Ocorrencia>() {
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {
                if (response.isSuccessful()) {
                    Log.i("publicado", "Log: " + response.message());
                    atualizarOcorrenciaParaAtualizado(ocorrenciaAntiga);
                }
            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {
                Log.i("erro de publicacao", "Log: " + t.getMessage());
            }
        });
    }

    public void atualizarOcorrenciaParaAtualizado(Ocorrencia ocorrencia) {
        realm.beginTransaction();
        ocorrencia.setNovo(false);
        realm.copyToRealmOrUpdate(ocorrencia);
        realm.commitTransaction();
    }

    public void verificarOcorrenciasParaPublicar() {
        List<Ocorrencia> ocorrencias = realm.copyFromRealm(realm.where(Ocorrencia.class).findAll());
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.isNovo()) {
//                realm.beginTransaction();
//                realm.copyToRealmOrUpdate(ocorrencia);
//                realm.commitTransaction();

                Ocorrencia oc = new Ocorrencia();
                oc.setDatahora(ocorrencia.getDatahora());
                oc.setDatahoracadastro(ocorrencia.getDatahoracadastro());
                oc.setFuncionarioEscola(ocorrencia.getFuncionarioEscola());
                oc.setDescricao(ocorrencia.getDescricao());
                oc.setMatriculaAluno(ocorrencia.getMatriculaAluno());
                oc.setTipoOcorrencia(ocorrencia.getTipoOcorrencia());
                oc.setAluno(ocorrencia.getAluno());
                oc.setAnoLetivo(ocorrencia.getAnoLetivo());
                oc.setFuncionario(ocorrencia.getFuncionario());
                oc.setUnidade(ocorrencia.getUnidade());
                oc.setEnviadoSms(ocorrencia.isEnviadoSms());
                oc.setDataEnvioSms(ocorrencia.getDataEnvioSms());
                oc.setResumoSms(ocorrencia.getResumoSms());
                oc.setObservacao(ocorrencia.getObservacao());
                oc.setNumeroTelefone(ocorrencia.getNumeroTelefone());
                oc.setUsuario(ocorrencia.getUsuario());

                publicarOcorrencia(oc, ocorrencia);
            }
        }
    }


    //    METODOS PARA PUBLICAÇÃO DO MODELO ALUNOFREQUENCIAMES
    public void verificarAnoFrequenciaMesParaPublicar() {

        RealmResults<AlunoFrequenciaMes> alunoFrequenciasMeses = realm.where(AlunoFrequenciaMes.class).findAll();
        if (alunoFrequenciasMeses != null) {
            List<AlunoFrequenciaMes> alunoFrequenciasMes = realm.copyFromRealm(alunoFrequenciasMeses);
            for (AlunoFrequenciaMes alunoFrequenciaMes : alunoFrequenciasMes) {
//                if (alunoFrequenciaMes.isAlterado() || alunoFrequenciaMes.isNovo())
                alterarParaModeloDePublicacao(alunoFrequenciaMes);
            }
        }
    }

    public void alterarParaModeloDePublicacao(AlunoFrequenciaMes alunoFrequenciaMes) {
        com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes aFreqM =
                new com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes(alunoFrequenciaMes.getTotalFaltas(), alunoFrequenciaMes.getDisciplinaAluno(), alunoFrequenciaMes.getMatricula(), alunoFrequenciaMes.getBimestre().getId(), alunoFrequenciaMes.getTipoLancamentoFrequencia(), alunoFrequenciaMes.getDisciplina().getId());

        if (alunoFrequenciaMes.isNovo()) {
            publicarAlunoFrequenciaMes(aFreqM, alunoFrequenciaMes);
        } else if (alunoFrequenciaMes.isAlterado()) {
            aFreqM.setId(alunoFrequenciaMes.getId());
            atualizarAnoFrequenciaMes(aFreqM, alunoFrequenciaMes);
        }
    }

    private void atualizarAnoFrequenciaMes(com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes alunoFrequenciaMes, final AlunoFrequenciaMes alunoFrequenciaMes2) {
        Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> alunoFrequenciaMesCall = apiService.getAlunoFrequenciaMesEndPoint().atualizarAlunoFrequenciaMes(alunoFrequenciaMes.getId(), alunoFrequenciaMes);
        alunoFrequenciaMesCall.enqueue(new Callback<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes>() {
            @Override
            public void onResponse(Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> call, Response<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> response) {
                if (response.isSuccessful()) {
                    Log.i("publicado", "Log: " + response.message());
                    realm.beginTransaction();
                    alunoFrequenciaMes2.setAlterado(false);
                    realm.copyToRealmOrUpdate(alunoFrequenciaMes2);
                    realm.commitTransaction();
                }
            }

            @Override
            public void onFailure(Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> call, Throwable t) {
                Log.i("erro de publicacao", "Log: " + t.getMessage());
            }
        });
    }

    private void publicarAlunoFrequenciaMes(com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes alunoFrequenciaMes, final AlunoFrequenciaMes alunoFrequenciaMes2) {
        Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> alunoFrequenciaMesCall = apiService.getAlunoFrequenciaMesEndPoint().publicarAlunoFrequenciaMes(alunoFrequenciaMes);
        alunoFrequenciaMesCall.enqueue(new Callback<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes>() {
            @Override
            public void onResponse(Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> call, Response<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> response) {
                if (response.isSuccessful()) {
                    Log.i("publicacao", "Log: " + response.message());
                    realm.beginTransaction();
                    alunoFrequenciaMes2.setNovo(false);
                    realm.copyToRealmOrUpdate(alunoFrequenciaMes2);
                    realm.commitTransaction();
                }
            }

            @Override
            public void onFailure(Call<com.android.educar.educar.model.modeloriginal.AlunoFrequenciaMes> call, Throwable t) {
                Log.i("erro de publicacoao", "log: " + t.getMessage());
            }
        });
    }
}
