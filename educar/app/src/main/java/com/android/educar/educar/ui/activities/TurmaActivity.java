package com.android.educar.educar.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.adapter.TurmaAdapter;
import com.android.educar.educar.model.modelalterado.AnoLetivo;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.model.modelalterado.Unidade;
import com.android.educar.educar.utils.Messages;
import com.android.educar.educar.utils.Preferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class TurmaActivity extends AppCompatActivity {

    private ListView turmas;
    private Preferences preferences;
    private Unidade unidadeSelecionada;
    private TextView unidadeSelecionadaTurma;
    private LinearLayout unidade;
    private Unidade unidadeFuncionario;
    private TurmaAdapter turmaArrayAdapter;
    private List<Turma> turmaList;
    private Realm realm;
    private Messages messages;
    private Funcionario funcionario;
    private TextView anoLetivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma);
        bindind();
        configRealm();
        setupInit();
        onClickItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        recuperarDadosRealm();
//        mensagemInicial();
        recuperarUnidade();
        atualizarDadosTela();
        recuperarTurmas();
        atualizarAdapterListaTurmas();
    }


    public void mensagemInicial() {
        Toast.makeText(getApplicationContext(), "Selecione uma Turma para Continuar!", Toast.LENGTH_LONG).show();
    }

    public void bindind() {
        turmas = findViewById(R.id.turmas_list_view_id);
        unidadeSelecionadaTurma = findViewById(R.id.unidadeselecionada_turma_id);
        unidade = findViewById(R.id.unidade_turma_id);
        anoLetivo = findViewById(R.id.anoLetivo_atual_turma_id);
    }

    public void recuperarUnidade() {
        for (int i = 0; i < funcionario.getFuncionarioEscolas().size(); i++) {
            if (funcionario.getFuncionarioEscolas().get(i).getAtivo()) {
                if (funcionario.getFuncionarioEscolas().get(i).getUnidade().getId() == preferences.getSavedLong(Messages.ID_UNIDADE)) {
                    this.unidadeSelecionada = funcionario.getFuncionarioEscolas().get(i).getUnidade();
                }
            } else {
                Snackbar.make(findViewById(R.id.parent), "FUNCIONARIO INATIVO", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        }
    }

    public void recuperarTurmas() {
        turmaList = new ArrayList<>();
        for (int i = 0; i < unidadeSelecionada.getLocalEscolas().size(); i++) {
            for (int j = 0; j < unidadeSelecionada.getLocalEscolas().get(i).getTurmas().size(); j++) {
                try {

                    if (unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j).getNivel().equals("FUNDAMENTAL") && unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j).getStatusTurma().equals("CADASTRADA")) {
                        this.turmaList.add(unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j));
                    }
                } catch (NullPointerException e) {
//                    Log.i("turma", unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j).getDescricao());
                }
            }
        }
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void setupInit() {
        turmaList = new ArrayList<>();
        preferences = new Preferences(this);
        messages = new Messages();
        funcionario = realm.copyFromRealm(realm.where(Funcionario.class).findFirst());
    }

    public void onClickItem() {
        turmas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Turma turma = (Turma) turmas.getItemAtPosition(position);
                preferences.saveLong("id_turma", turma.getId());
                try {
                    preferences.saveLong("id_serie", turma.getSerie().getId());
                } catch (NullPointerException e) {

                }

                nextAcitivity();
            }
        });
    }

    public void nextAcitivity() {
        Intent intent = new Intent(getApplicationContext(), DisciplinaActivity.class);
        startActivity(intent);
    }

    public void atualizarAdapterListaTurmas() {
        if (turmaList.size() == 0) {
            alertaInformacaoSemTurmas();
        }

        turmaArrayAdapter = new TurmaAdapter(getApplicationContext(), turmaList);
        this.turmas.setAdapter(turmaArrayAdapter);
    }

    public void atualizarDadosTela() {
        unidadeSelecionadaTurma.setText(unidadeSelecionada.getNome());
        anoLetivo.setText(realm.where(AnoLetivo.class).findFirst().getDescricao());
    }

    public void alertaInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações!");
        builder.setMessage("Selecione uma Turma para Continuar!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    public void alertaInformacaoSemTurmas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações!");
        builder.setMessage("Esta Unidade não possui Turmas!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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
