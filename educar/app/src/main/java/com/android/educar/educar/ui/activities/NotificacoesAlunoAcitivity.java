package com.android.educar.educar.ui.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.educar.educar.R;
import com.android.educar.educar.adapter.OcorrenciaListaAdapter;
import com.android.educar.educar.model.modelalterado.Aluno;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.PessoaFisica;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.utils.Preferences;

import io.realm.Realm;

public class NotificacoesAlunoAcitivity extends AppCompatActivity {

    private Realm realm;
    private TextView alunoDetalhe;
    private TextView turmaDetalhe;
    private TextView disciplinaDetalhe;
    private ListView notificacoes;
    private PessoaFisica pessoaFisica;
    private Matricula matricula;
    private Aluno aluno;
    private CalendarView calendarView;
    private Preferences preferences;
    private Turma turma;
    private Disciplina disciplina;
    private TextView matriculaNotificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes_aluno_acitivity);
        configRealm();
        binding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupInit();
        recuperarDadosRealm();
        atualizarListaDeNotificacoes();
        atualizarDadosTela();
    }

    public void setupInit() {
        preferences = new Preferences(this);
    }

    public void configRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void binding() {
        alunoDetalhe = findViewById(R.id.aluno_notificacoes_id);
        turmaDetalhe = findViewById(R.id.turma_notificacoes_aluno_id);
        disciplinaDetalhe = findViewById(R.id.disciplina_notificacao_aluno_id);
        notificacoes = findViewById(R.id.notificacoes_aluno_id);
        matriculaNotificacao = findViewById(R.id.matricula_notificacao_id);
    }

    public void recuperarDadosRealm() {
        matricula = realm.where(Matricula.class).equalTo("id", preferences.getSavedLong("id_matricula_aluno")).findFirst();
        turma = realm.where(Turma.class).equalTo("id", preferences.getSavedLong("id_turma")).findFirst();
        disciplina = realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst();
    }

    public void atualizarListaDeNotificacoes() {
        OcorrenciaListaAdapter ocorrenciaListaAdapter = new OcorrenciaListaAdapter(this, matricula.getOcorrencias());
        notificacoes.setAdapter(ocorrenciaListaAdapter);
    }

    public void atualizarDadosTela() {
        matriculaNotificacao.setText("" + matricula.getId());
        turmaDetalhe.setText(turma.getDescricao());
        alunoDetalhe.setText(matricula.getAluno().getPessoaFisica().getNome());
        disciplinaDetalhe.setText(disciplina.getDescricao());
    }
}
