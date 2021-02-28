package com.android.educar.educar.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.model.modelalterado.AlunoFrequenciaMes;
import com.android.educar.educar.model.modelalterado.AlunoNotaMes;
import com.android.educar.educar.model.modelalterado.Ocorrencia;
import com.android.educar.educar.network.chamadas.ChamadasDePublicacao;
import com.android.educar.educar.utils.Preferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SincronizacaoActivity extends AppCompatActivity {


    private Preferences preferences;
    private ChamadasDePublicacao chamadasDePublicacao;
    private ProgressDialog progressDialog;
    private Button atualizaBancoAtual;

    private Realm realm;

    private LinearLayout sincronizarFrequencia;
    private LinearLayout sincronizarNotas;
    private LinearLayout sincronizarNotificacoes;

    private CheckBox statusSincronizacaoFrequencia;
    private CheckBox statusSincronizacaoNota;
    private CheckBox statusSincronizacaoNotificacao;

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacao);
        binding();
        setupInit();
        onClick();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Recuperando seus dados...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        configRealm();
        statusSincronizacaoFrequencia.setChecked(consultarStatusParaSincronizarFrequencia());
        statusSincronizacaoFrequencia.setEnabled(false);
        statusSincronizacaoNota.setChecked(consultarStatusParaSincronizarNota());
        statusSincronizacaoNota.setEnabled(false);
        statusSincronizacaoNotificacao.setChecked(consultarStatusParaSincronizarNotificacao());
        statusSincronizacaoNotificacao.setEnabled(false);
    }

    private boolean consultarStatusParaSincronizarNota() {
        RealmResults<AlunoNotaMes> alunoNotasMes = realm.where(AlunoNotaMes.class).findAll();
        if (alunoNotasMes != null) {
            List<AlunoNotaMes> alunoNota = realm.copyFromRealm(alunoNotasMes);
            for (AlunoNotaMes alunoNotaMes : alunoNota) {
                if (alunoNotaMes.isNovo() || alunoNotaMes.isAlterado()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean consultarStatusParaSincronizarNotificacao() {
        RealmResults<Ocorrencia> ocorrencias = realm.where(Ocorrencia.class).findAll();
        if (ocorrencias != null) {
            List<Ocorrencia> ocorrencias1 = realm.copyFromRealm(ocorrencias);
            for (Ocorrencia ocor : ocorrencias1) {
                if (ocor.isNovo()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean consultarStatusParaSincronizarFrequencia() {
        RealmResults<AlunoFrequenciaMes> alunoFrequenciaMes = realm.where(AlunoFrequenciaMes.class).findAll();
        if (alunoFrequenciaMes != null) {
            List<AlunoFrequenciaMes> alunoFrequenciaMes1 = realm.copyFromRealm(alunoFrequenciaMes);
            for (AlunoFrequenciaMes frequenciaMes : alunoFrequenciaMes1) {
                if (frequenciaMes.isNovo() || frequenciaMes.isAlterado()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setupInit() {
        chamadasDePublicacao = new ChamadasDePublicacao(getApplicationContext());
        preferences = new Preferences(getApplicationContext());
    }

    public void binding() {
        sincronizarFrequencia = findViewById(R.id.sincronizar_frequencia_id);
        sincronizarNotas = findViewById(R.id.sincronizar_notas_id);
        sincronizarNotificacoes = findViewById(R.id.sincronizar_notificacoes_id);
        atualizaBancoAtual = findViewById(R.id.atualizar_banco_atual_id);
        statusSincronizacaoFrequencia = findViewById(R.id.status_sincronizacao_frequencia_id);
        statusSincronizacaoNota = findViewById(R.id.status_sincronizacao_notas_id);
        statusSincronizacaoNotificacao = findViewById(R.id.status_sincronizacao_notificacoes_id);
    }


    public void onClick() {
        sincronizarFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statusSincronizacaoFrequencia.isChecked()) {
                    chamadasDePublicacao.verificarAnoFrequenciaMesParaPublicar();
                    onResume();
                    Snackbar.make(findViewById(android.R.id.content), "Frequências Sincronizadas.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                }else {
                    Snackbar.make(findViewById(android.R.id.content), "Frequências já estão Sincronizadas.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sincronizarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statusSincronizacaoNota.isChecked()) {
                    chamadasDePublicacao.verificarAlunoNotaMesParaPublicar();
                    onResume();
                    Snackbar.make(findViewById(android.R.id.content), "Notas Sincronizadas.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    }).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Notas já estão Sincronizadas.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sincronizarNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statusSincronizacaoNotificacao.isChecked()) {
                    chamadasDePublicacao.verificarOcorrenciasParaPublicar();
                    onResume();
                    Snackbar.make(findViewById(android.R.id.content), "Notificações Sincronizadas.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Notificações já estão Sincronizadas.", Snackbar.LENGTH_LONG).show();
                }
            }
        });


        atualizaBancoAtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaInformacaoSincronizar();
            }
        });
    }

    public void alertaInformacaoSincronizar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta!");
        builder.setMessage("Sincronize seus dados antes de Atualizar seu banco atual.");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.saveBoolean("alerta_info_primeiro_acesso", false);
                startActivity(new Intent(getApplicationContext(), UnidadeActivity.class));
            }
        }).setNegativeButton("Cancelar", null).show();
    }
}
