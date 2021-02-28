package com.android.educar.educar.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.educar.educar.R;
import com.android.educar.educar.adapter.FrequenciaAdapterLista;
import com.android.educar.educar.mb.BimestreMB;
import com.android.educar.educar.mb.FrequenciaMB;
import com.android.educar.educar.model.modelalterado.Bimestre;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.model.modelalterado.Unidade;
import com.android.educar.educar.utils.Preferences;
import com.android.educar.educar.utils.UtilsFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class FrequenciaFragment extends Fragment {

    private Preferences preferences;
    private ListView alunosFrequencia;
    private TextView unidadeSelecionadaAula;
    private TextView turmaSelecionadaAula;
    private TextView disciplinaSelecionadaAula;
    private Unidade unidadeSelecionada;
    private Disciplina disciplinaSelecionada;
    private Turma turmaSelecionada;
    private TextView dataFrequencia;
    private List<Matricula> pessoaFisicas;
    private EditText conteudoAula;

    private FrequenciaMB frequenciaMB;
    private BimestreMB bimestreMB;

    private Realm realm;
    private TextView bimestreFragmentFrequencia;

    private Funcionario funcionario;
    private Bimestre bimestre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frequencia, container, false);
        bindind(view);
        configRealm();
        setupInit();
        recuperarDados();
        atualizarDadosTela();
        recuperarAlunosRealm();
        return view;
    }

    public void recuperarDados() {
        unidadeSelecionada = realm.copyFromRealm(realm.where(Unidade.class).equalTo("id", preferences.getSavedLong("id_unidade")).findFirst());
        turmaSelecionada = realm.copyFromRealm(realm.where(Turma.class).equalTo("id", preferences.getSavedLong("id_turma")).findFirst());
        disciplinaSelecionada = realm.copyFromRealm(realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst());
    }

    public void bindind(View view) {
        conteudoAula = view.findViewById(R.id.conteudo_aula_id);
        alunosFrequencia = view.findViewById(R.id.alunos_frequencia_recycler_id);
        unidadeSelecionadaAula = view.findViewById(R.id.unidadeselecionada_aula_id);
        disciplinaSelecionadaAula = view.findViewById(R.id.disciplinaselecionada_aula_id);
        turmaSelecionadaAula = view.findViewById(R.id.turmaselecionada_aula_id);
        bimestreFragmentFrequencia = view.findViewById(R.id.bimestre_fragment_frequencia_id);
        dataFrequencia = view.findViewById(R.id.data_frequencia_id);
    }

    public void configRealm() {
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
    }

    public void setupInit() {
        preferences = new Preferences(getContext());
        frequenciaMB = new FrequenciaMB(getContext());
        bimestreMB = new BimestreMB(getContext());
        pessoaFisicas = new ArrayList<>();
        funcionario = realm.copyFromRealm(realm.where(Funcionario.class).findFirst());
        bimestre = realm.copyFromRealm(realm.where(Bimestre.class).equalTo("id", bimestreMB.getBimestreAtual()).findFirst());
    }

    public void recuperarAlunosRealm() {
        this.pessoaFisicas = turmaSelecionada.getMatriculas();
        Collections.sort(pessoaFisicas);
        atualizarAdapterFrequencia(pessoaFisicas);
    }

    public void atualizarAdapterFrequencia(List<Matricula> pessoaFisicas) {
        FrequenciaAdapterLista frequenciaAdapterLista = new FrequenciaAdapterLista(getContext(), pessoaFisicas);
        alunosFrequencia.setAdapter(frequenciaAdapterLista);
    }

    public void atualizarDadosTela() {
        unidadeSelecionadaAula.setText(unidadeSelecionada.getAbreviacao());
        turmaSelecionadaAula.setText(turmaSelecionada.getDescricao());
        disciplinaSelecionadaAula.setText(disciplinaSelecionada.getDescricao());
        bimestreFragmentFrequencia.setText(bimestre.getDescricao());
        dataFrequencia.setText("" + UtilsFunctions.apenasData().format(new Date()));
    }
}
