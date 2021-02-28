package com.android.educar.educar.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.mb.BimestreMB;
import com.android.educar.educar.model.modelalterado.AnoLetivo;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.GradeCurso;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.model.modelalterado.Unidade;
import com.android.educar.educar.utils.Messages;
import com.android.educar.educar.utils.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;

//import com.android.educar.educar.dao.ClassDAO;
//import com.android.educar.educar.dao.DisciplinaDAO;
//import com.android.educar.educar.dao.ProfessorDAO;
//import com.android.educar.educar.dao.TurmaDAO;
//import com.android.educar.educar.dao.UnidadeDAO;

public class DisciplinaActivity extends AppCompatActivity {

    private Preferences preferences;
    private Messages messages;
    private ListView disciplinas;
    private TextView turmaSelecionaDisciplina;
    private TextView unidadeSelecionadaDisciplina;
    private Turma turmaSelecionada;
    private Unidade unidadeSelecionada;
    private ArrayAdapter<Disciplina> disciplinaArrayAdapter;

    private LinearLayout unidadeDisciplina;
    private LinearLayout turmaDisciplina;

    private Realm realm;
    private Set<Disciplina> disciplinasLista;
    private Funcionario funcionario;

    private BimestreMB bimestreMB;

    private TextView anoLetivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disicplina);
        binding();
        configRealm();
        setupInit();
        onClickItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recuperarUnidade();
        recuperarTurma();
//        recuperarDadosRealm();
        atualizarDadosTela();
        recuperarDisciplinas();
        bimestreMB.getBimestreAtual();
    }

    public void recuperarUnidade() {
        for (int i = 0; i < funcionario.getFuncionarioEscolas().size(); i++) {
            if (funcionario.getFuncionarioEscolas().get(i).getUnidade().getId() == preferences.getSavedLong(Messages.ID_UNIDADE) && funcionario.getFuncionarioEscolas().get(i).getAtivo()) {
                this.unidadeSelecionada = funcionario.getFuncionarioEscolas().get(i).getUnidade();
            }
        }
    }

    public void recuperarTurma() {
        for (int i = 0; i < unidadeSelecionada.getLocalEscolas().size(); i++) {
            for (int j = 0; j < unidadeSelecionada.getLocalEscolas().get(i).getTurmas().size(); j++) {
                if (unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j).getId() == preferences.getSavedLong(Messages.ID_TURMA)) {
                    this.turmaSelecionada = (unidadeSelecionada.getLocalEscolas().get(i).getTurmas().get(j));
                }
            }
        }
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }


    public void recuperarDisciplinas() {

        for (GradeCurso gradeCurso : funcionario.getGradeCursos()) {
            if (gradeCurso.getDisciplina() != null) {
                this.disciplinasLista.add(gradeCurso.getDisciplina());
            } if (gradeCurso.getSeriedisciplina()!=null) {
                this.disciplinasLista.add(gradeCurso.getSeriedisciplina().getDisciplina());
            }
        }

        List<Disciplina> disciplinasOrdem = new ArrayList<>();
        disciplinasOrdem.addAll(disciplinasLista);
        Collections.sort(disciplinasOrdem);
        atualizarAdapterListaDisciplinas(disciplinasOrdem);
    }

    public void binding() {
        disciplinas = findViewById(R.id.disciplinas_list_view_id);
        turmaSelecionaDisciplina = findViewById(R.id.turmaselecionada_disciplina_id);
        unidadeSelecionadaDisciplina = findViewById(R.id.unidadeselecionada_disciplina_id);
        unidadeDisciplina = findViewById(R.id.unidade_disciplina_id);
        turmaDisciplina = findViewById(R.id.turma_disciplina_id);
        anoLetivo = findViewById(R.id.anoLetivo_atual_disciplina_id);
    }

    public void onClickItem() {
        disciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Disciplina disciplina = (Disciplina) disciplinas.getItemAtPosition(position);
                preferences.saveLong("id_disciplina", disciplina.getId());
                nextActivity();
            }
        });
    }

    public void nextActivity() {
        Intent intent = new Intent(getApplicationContext(), AulaAcoesActivity.class);
        startActivity(intent);
    }

    public void setupInit() {
        preferences = new Preferences(this);
        bimestreMB = new BimestreMB(this);
        messages = new Messages();
        disciplinasLista = new HashSet<>();
        funcionario = realm.copyFromRealm(realm.where(Funcionario.class).findFirst());
    }

    public void atualizarAdapterListaDisciplinas(List<Disciplina> disciplinas2) {
        ArrayList<Disciplina> disciplinas = new ArrayList<>(disciplinas2);
        disciplinaArrayAdapter = new ArrayAdapter<Disciplina>(getApplicationContext(), android.R.layout.simple_list_item_1, disciplinas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.BLACK);
                tv.setTextSize(12);

                return view;
            }
        };
        this.disciplinas.setAdapter(disciplinaArrayAdapter);
    }

    public void atualizarDadosTela() {
        unidadeSelecionadaDisciplina.setText(unidadeSelecionada.getNome());
        turmaSelecionaDisciplina.setText(turmaSelecionada.getDescricao());
        anoLetivo.setText(realm.where(AnoLetivo.class).findFirst().getDescricao());

    }

    public void alertaInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações!");
        builder.setMessage("Selecione uma Disciplina para Continuar!");
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
