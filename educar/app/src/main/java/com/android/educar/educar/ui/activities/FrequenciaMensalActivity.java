package com.android.educar.educar.ui.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.mb.FrequenciaMB;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Frequencia;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.utils.Preferences;

import java.util.List;

import io.realm.Realm;

public class FrequenciaMensalActivity extends AppCompatActivity {

    private Realm realm;
    private TextView alunoDetalhe;
    private TextView turmaDetalhe;
    private TextView disciplinaDetalhe;
    private Matricula matricula;
    private Preferences preferences;
    private Turma turma;
    private Disciplina disciplina;
    private ListView listaDeFaltas;
    private FrequenciaMB frequenciaMB;
    private TextView matriculaFrequenciaMnsal;

    public void binding() {
        alunoDetalhe = findViewById(R.id.aluno2_notificacoes_id);
        turmaDetalhe = findViewById(R.id.turma2_notificacoes_aluno_id);
        disciplinaDetalhe = findViewById(R.id.disciplina2_notificacao_aluno_id);
        listaDeFaltas = findViewById(R.id.lista_de_faltas_id);
        matriculaFrequenciaMnsal = findViewById(R.id.matricula_frequencia_mensal_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia_mensal);
        configRealm();
        binding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupInit();
        recuperarDadosRealm();
        atualizarDadosTela();
        atualizandoListaDePresenca();
    }

    public void setupInit() {
        preferences = new Preferences(this);
        frequenciaMB = new FrequenciaMB(this);
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void recuperarDadosRealm() {
        matricula = realm.where(Matricula.class).equalTo("id", preferences.getSavedLong("id_matricula_lista")).findFirst();
        turma = realm.where(Turma.class).equalTo("id", preferences.getSavedLong("id_turma")).findFirst();
        disciplina = realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst();
    }

    public void atualizarDadosTela() {
        turmaDetalhe.setText(turma.getDescricao());
        alunoDetalhe.setText(matricula.getAluno().getPessoaFisica().getNome());
        disciplinaDetalhe.setText(disciplina.getDescricao());
        matriculaFrequenciaMnsal.setText(matricula.getId()+"");
    }

    public void atualizandoListaDePresenca() {
        final List<Frequencia> frequenciasLista = realm.where(Frequencia.class).equalTo("presenca", false).equalTo("matricula", matricula.getId()).findAll();
        ArrayAdapter<Frequencia> frequencias = new ArrayAdapter<Frequencia>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, frequenciasLista){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.BLACK);
                tv.setTextSize(12);
                return view;
            }
        };
        listaDeFaltas.setAdapter(frequencias);

        listaDeFaltas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alterarPresenca(frequenciasLista.get(i));
            }
        });
    }

    public void alterarPresenca(final Frequencia frequencia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        final View viewDialog = inflater.inflate(R.layout.dialog_alterar_presenca_aluno, null);

        final EditText aluno = viewDialog.findViewById(R.id.aluno_presenca_mensal_id);
        final EditText data = viewDialog.findViewById(R.id.data_presenca_mensal_id);
        final CheckBox preseca = viewDialog.findViewById(R.id.presenca_id_mensal);

        aluno.setText(realm.copyFromRealm(realm.where(Matricula.class).equalTo("id", frequencia.getMatricula()).findFirst()).getAluno().getPessoaFisica().getNome());
        aluno.setEnabled(false);
        data.setText(frequencia.getDate());
        data.setEnabled(false);
        preseca.setChecked(frequencia.isPresenca());

        preseca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preseca.isChecked()) {
                    realm.beginTransaction();
                    frequencia.setPresenca(true);
                    frequencia.setAlterado(true);
                    realm.copyToRealmOrUpdate(frequencia);
                    realm.commitTransaction();
                }
            }
        });

        builder.setView(viewDialog).setTitle("Alterar Presença")
                .setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }

                }).setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        frequenciaMB.salvarFrequencia();
    }
}
