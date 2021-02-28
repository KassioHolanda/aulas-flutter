package com.android.educar.educar.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.mb.BimestreMB;
import com.android.educar.educar.model.modelalterado.AnoLetivo;
import com.android.educar.educar.model.modelalterado.Bimestre;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.Serie;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.model.modelalterado.Unidade;
import com.android.educar.educar.utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;

public class AulaAcoesActivity extends AppCompatActivity {
    private TextView unidade;
    private TextView disciplina;
    private TextView turma;
    private TextView serieacoes;
    private TextView bimestre;
    private Preferences preferences;

    private LinearLayout paginaNotas;
    private LinearLayout paginaOcorrencia;
    private LinearLayout paginaFrequenciaMensal;
    private LinearLayout paginaFrequencia;
    private FloatingActionButton floatingActionButton;
    private LinearLayout linearSerie;

    private Realm realm;

    private Funcionario funcionario;
    private Unidade unidadeSelecionada;
    private Turma turmaSelecionada;
    private Disciplina disciplinaSelecionada;
    private Serie serieSelecionada;
    private Bimestre bimestreSelecionado;

    private BimestreMB bimestreMB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula_acoes);
        binding();
        configRealm();
        setupInit();
        settings();
        clickOnItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        recuperarUnidade();
//        recuperarTurma();
        recuperarDisciplina();
        atualizarDados();
    }

    public void setupInit() {
        preferences = new Preferences(getApplicationContext());
        funcionario = realm.copyFromRealm(realm.where(Funcionario.class).findFirst());
        bimestreMB = new BimestreMB(getApplicationContext());
        bimestreSelecionado = realm.copyFromRealm(realm.where(Bimestre.class).equalTo("id", preferences.getSavedLong("id_bimestre")).findFirst());
        unidadeSelecionada = realm.copyFromRealm(realm.where(Unidade.class).equalTo("id", preferences.getSavedLong("id_unidade")).findFirst());
        turmaSelecionada = realm.copyFromRealm(realm.where(Turma.class).equalTo("id", preferences.getSavedLong("id_turma")).findFirst());
        disciplinaSelecionada = realm.copyFromRealm(realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst());
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void recuperarDisciplina() {
        for (int i = 0; i < turmaSelecionada.getGradeCursos().size(); i++) {
            try {
                if (turmaSelecionada.getGradeCursos().get(i).getSeriedisciplina().getSerie().getId() == preferences.getSavedLong("id_serie")) {
                    serieSelecionada = turmaSelecionada.getGradeCursos().get(i).getSeriedisciplina().getSerie();
                }

            } catch (NullPointerException e) {

            }
        }
    }

    public void atualizarDados() {
        unidade.setText(unidadeSelecionada.getNome());
        turma.setText(turmaSelecionada.getDescricao());
        disciplina.setText(disciplinaSelecionada.getDescricao());
        try {
            serieacoes.setText(serieSelecionada.getDescricao());
        } catch (Exception e) {
            linearSerie.setVisibility(View.GONE);
            serieacoes.setText("");
        }
        bimestre.setText(bimestreSelecionado.getDescricao());
    }

    public void clickOnItem() {
        paginaFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FrequenciaActivity.class));
            }
        });

        paginaNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotaFragmentActivity.class));
            }
        });

        paginaOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OcorrenciaActivity.class));
            }
        });

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), UnidadeActivity.class));
//            }
//        });

        paginaFrequenciaMensal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FrequenciaMensalActivityAlunos.class));
            }
        });
    }

    public void binding() {
        unidade = findViewById(R.id.unidade_id);
        bimestre = findViewById(R.id.bimestre_acoes_id);
        turma = findViewById(R.id.turma_id);
        disciplina = findViewById(R.id.disciplina_id);
        paginaFrequencia = findViewById(R.id.frequencias_diaria_id);
        paginaFrequenciaMensal = findViewById(R.id.frequencias_mensal_id);
        paginaNotas = findViewById(R.id.notas_id);
        paginaOcorrencia = findViewById(R.id.ocorrencias_id);
        serieacoes = findViewById(R.id.serieacoes_id);
//        floatingActionButton = findViewById(R.id.fab_home_id);
        linearSerie = findViewById(R.id.serie_acoes_id);
    }

    public void settings() {
        final androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        AnoLetivo anoLetivo = realm.copyFromRealm(realm.where(AnoLetivo.class).findFirst());
        actionBar.setTitle(anoLetivo.getDescricao());
    }

    public void alertaInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações!");
        builder.setMessage("Selecione uma Opção para Continuar!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_infor_aluno:
                alertaInformacao();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dados_alunos, menu);
        return true;
    }
}
