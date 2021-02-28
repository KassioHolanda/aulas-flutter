package com.android.educar.educar.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.model.modelalterado.AnoLetivo;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.FuncionarioEscola;
import com.android.educar.educar.model.modelalterado.LocalEscola;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.TokenAPI;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.model.modelalterado.Unidade;
import com.android.educar.educar.model.modelalterado.User;
import com.android.educar.educar.network.chamadas.AnoLetivoChamada;
import com.android.educar.educar.network.service.APIService;
import com.android.educar.educar.network.service.ListaTipoOcorrenciaAPI;
import com.android.educar.educar.utils.Messages;
import com.android.educar.educar.utils.Preferences;
import com.android.educar.educar.utils.UtilsFunctions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnidadeActivity extends AppCompatActivity {

    private ListView unidades;
    private Preferences preferences;
    private Messages messages;
    private ArrayAdapter<Unidade> unidadeArrayAdapter;
    private Button sincronizarDados;
    private Realm realm;
    private Funcionario funcionario;
    private APIService apiService;
    private TextView professorLogado;
    private ProgressDialog progressDialog;
    private List<Unidade> listaDeUnidades;
    private AnoLetivoChamada anoLetivoChamada;
    private TextView anoletivo;
    private AnoLetivo anoLetivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidade);
        bindind();
        configRealm();
        setupInit();
        onClickItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarPrimeiroAcesso();
        recuperarToken();
//        atualizarDadosTela();
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void bindind() {
        unidades = findViewById(R.id.unidades_list_view_id);
        sincronizarDados = findViewById(R.id.sincronizar_dados);
        professorLogado = findViewById(R.id.professorlogado_id);
        anoletivo = findViewById(R.id.bimestre_atual_unidade_id);
    }

    public void atualizarDadosTela() {
        String nome = realm.where(AnoLetivo.class).findFirst().getDescricao();
        anoletivo.setText(nome);
        professorLogado.setText(funcionario.getPessoaFisica().getNome());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dados_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_infor_aluno:
                alertaInformacao();
                break;
            case R.id.sair_id:
                alertaInformacaoSincronizar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletarBanco() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        preferences.limpar();
    }

    public void onClickItem() {
        unidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Unidade unidade = (Unidade) unidades.getItemAtPosition(position);
                preferences.saveLong(messages.ID_UNIDADE, unidade.getId());
                nextAcivity();
            }
        });

        sincronizarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilsFunctions.isConnect(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), SincronizacaoActivity.class));
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "SEM CONEXÃO", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void nextAcivity() {
        verificarAnoLetivoAtual();
        Intent intent = new Intent(getApplicationContext(), TurmaActivity.class);
        startActivity(intent);
    }

    public void setupInit() {
        this.funcionario = new Funcionario();
        preferences = new Preferences(this);
        apiService = new APIService(preferences.getSavedString("token"));
        messages = new Messages();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Recuperando seus dados...");
        anoLetivoChamada = new AnoLetivoChamada(getApplicationContext());
    }

    public void atualizarAdapterListaUnidades() {
        unidadeArrayAdapter = new ArrayAdapter<Unidade>(getApplicationContext(), android.R.layout.simple_list_item_1, this.listaDeUnidades) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.BLACK);
                tv.setTextSize(12);
                return view;
            }
        };
        this.unidades.setAdapter(unidadeArrayAdapter);
    }

    public void alertaInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações!");
        builder.setMessage("Selecione uma Unidade para Continuar!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    public void alertaInformacaoSincronizar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Antes de sair sincronize seus dados, ao sair seus dadaos serão apagados!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmarSaidaUsuario();
            }
        }).show();
    }

    public void confirmarSaidaUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Você deseja realmente sair?");
        builder.setCancelable(false);
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                deletarBanco();
                onStop();
            }
        }).setNegativeButton("NÂO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    public void recuperarDadosFuncionario() {
        progressDialog.show();
        Call<List<Funcionario>> funcionarioCall = apiService.getCorpoEndPoint().getFuncionario(preferences.getSavedString("cpf"));
        funcionarioCall.enqueue(new Callback<List<Funcionario>>() {
            @Override
            public void onResponse(Call<List<Funcionario>> call, Response<List<Funcionario>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        recuperarFuncionario(response.body().get(0));
                    }
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<List<Funcionario>> call, Throwable t) {
                progressDialog.hide();
                Log.i("erro_api", t.getMessage());
            }
        });
    }

    public void novaThreadTurma() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recuperarDadosTurmas();
            }
        }).start();
    }


    public void novaThreadAlunoFrequenciaENotaMes(final Long matriculaId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recuperarDadosDaMatricula(matriculaId);
            }
        }).start();
    }

    public void recuperarTipoOcorrencia(final int numero) {
        Call<ListaTipoOcorrenciaAPI> tipoOcorrenciaCall = apiService.getTipoOcorrenciaEndPoint().tiposOcorrencia(numero);
        tipoOcorrenciaCall.enqueue(new Callback<ListaTipoOcorrenciaAPI>() {
            @Override
            public void onResponse(Call<ListaTipoOcorrenciaAPI> call, Response<ListaTipoOcorrenciaAPI> response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body().getResults());
                    realm.commitTransaction();
                    int num = numero;
                    if (response.body().getNext() != null) {
                        num = numero + 1;
                        recuperarTipoOcorrencia(num);
                    } else {
                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListaTipoOcorrenciaAPI> call, Throwable t) {
                progressDialog.hide();
                Log.i("erro_api", t.getMessage());
            }
        });
    }

    private void recuperarDadosDaMatricula(final Long matriculaId) {
        progressDialog.show();
//        progressDialog.setMessage("Recuperando Matriculas");
        Call<Matricula> matriculaCall = apiService.getMatriculaEndPoint().getAlunoNotaMesEAlunoFrequenciaMes(matriculaId);
        matriculaCall.enqueue(new Callback<Matricula>() {
            @Override
            public void onResponse(Call<Matricula> call, Response<Matricula> response) {
                if (response.isSuccessful()) {
                    salvarDados(response.body(), matriculaId);
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Matricula> call, Throwable t) {
                progressDialog.hide();
                Log.i("erro_api", t.getMessage());
            }
        });
    }

    private void salvarDados(Matricula body, Long matriculaId) {
        realm.beginTransaction();
        Matricula matricula = realm.copyFromRealm(realm.where(Matricula.class).equalTo("id", matriculaId).findFirst());
        matricula.setAlunoFrequenciasMes(body.getAlunoFrequenciasMes());
        matricula.setAlunosNotaMes(body.getAlunosNotaMes());
        matricula.setDisciplinaAlunos(body.getDisciplinaAlunos());
        matricula.setOcorrencias(body.getOcorrencias());
        realm.copyToRealmOrUpdate(matricula);
        realm.copyToRealmOrUpdate(body.getDisciplinaAlunos());
        realm.commitTransaction();
    }


    private void recuperarAlunosFrequenciaMes(List<Matricula> matriculas) {
        for (Matricula matricula : matriculas) {
            novaThreadAlunoFrequenciaENotaMes(matricula.getId());
        }
    }

    public void recuperarDadosTurmas() {
//        progressDialog.setMessage("Recuperando turmas");
        for (FuncionarioEscola funcionarioEscola : this.funcionario.getFuncionarioEscolas()) {
            for (LocalEscola localEscola : funcionarioEscola.getUnidade().getLocalEscolas()) {
                for (Turma turma : localEscola.getTurmas()) {
                    recuperarTurmas(turma);
                }
            }
        }
//        progressDialog.hide();
    }

    public void recuperarTurmas(final Turma turma) {
        progressDialog.show();
//        progressDialog.setMessage("Recuperando Turmas");
        Call<Turma> listaTurmaAPICall = apiService.getTurmaEndPoint().getTurmaCompleta(turma.getId());
        listaTurmaAPICall.enqueue(new Callback<Turma>() {
            @Override
            public void onResponse(Call<Turma> call, Response<Turma> response) {
                if (response.isSuccessful()) {
                    recuperarTurma(response.body(), turma.getId());
                }
                if (response.code() == 500) {
                    Log.i("erro_api", response.message());
                }

//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Turma> call, Throwable t) {
                progressDialog.hide();
                Log.i("erro_api", t.getMessage());
//                Snackbar.make(findViewById(android.R.id.content), "Ocorreu um Erro, Solicite Administrador, ao salvar as turmas.", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                }).show();
            }
        });
    }

    public void verificarAnoLetivoAtual() {
        preferences.saveLong("id_anoletivo", realm.copyFromRealm(realm.where(AnoLetivo.class).findFirst()).getId());
    }

    private void recuperarTurma(Turma body, Long turmaId) {
        realm.beginTransaction();
        Turma turma = realm.copyFromRealm(realm.where(Turma.class).equalTo("id", turmaId).findFirst());
        turma.setMatriculas(body.getMatriculas());
        realm.copyToRealmOrUpdate(turma);
        realm.commitTransaction();
        recuperarAlunosFrequenciaMes(turma.getMatriculas());
    }

    private void recuperarFuncionario(Funcionario body) {
        this.funcionario = body;

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this.funcionario);
        realm.commitTransaction();

        novaThreadTurma();
        atualizarDadosTela();
        recuperarUnidadesFuncionario();
        atualizarAdapterListaUnidades();
        recuperarTipoOcorrencia(1);
//        alertaInformacaoPrimeiraUtilizacao();
    }

    public void recuperarUnidadesFuncionario() {
        this.listaDeUnidades = new ArrayList<>();
        for (FuncionarioEscola funcionarioEscola : funcionario.getFuncionarioEscolas()) {
            if (funcionarioEscola.getDataFinal() == null)
                this.listaDeUnidades.add(funcionarioEscola.getUnidade());
        }
    }

    public void verificarPrimeiroAcesso() {
        if (!preferences.getSavedBoolean("alerta_info_primeiro_acesso")) {
//            recuperarDadosIndependentesAPI();
            anoLetivoChamada.recuperarAlunoLetivoAPI(1);
            anoLetivoChamada.recuperarBimestreAPI();
            recuperarDadosFuncionario();
//            atualizarDadosTela();
            preferences.saveBoolean("alerta_info_primeiro_acesso", true);
        } else {
            Funcionario f = realm.where(Funcionario.class).findFirst();
            this.funcionario = realm.copyFromRealm(f);
            atualizarDadosTela();
            recuperarUnidadesFuncionario();
            atualizarAdapterListaUnidades();
            verificarAnoLetivoAtual();
        }
    }

    public void recuperarToken() {
        Call<TokenAPI> tokenAPICall = apiService.getTokenEndPoint().login(new User("admin", "senha123"));
        tokenAPICall.enqueue(new Callback<TokenAPI>() {
            @Override
            public void onResponse(Call<TokenAPI> call, Response<TokenAPI> response) {
                if (response.isSuccessful()) {
                    preferences.saveString("token",response.body().getAcess());
//                    Toast.makeText(getApplicationContext(), "Token Salvo", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TokenAPI> call, Throwable t) {

            }
        });
    }
}
