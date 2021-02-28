package com.android.educar.educar.mb;

import android.content.Context;
import android.widget.Toast;

import com.android.educar.educar.model.modelalterado.AlunoFrequenciaMes;
import com.android.educar.educar.model.modelalterado.Bimestre;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.DisciplinaAluno;
import com.android.educar.educar.model.modelalterado.Frequencia;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.SerieDisciplina;
import com.android.educar.educar.model.modelalterado.Turma;
import com.android.educar.educar.utils.Preferences;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class FrequenciaMB {

    private Context context;
    private Realm realm;
    private Preferences preferences;
    private BimestreMB bimestreMB;
    private Turma turma;
    private DisciplinaAluno disciplinaAluno;

    public FrequenciaMB(Context context) {
        this.context = context;
        configRealm();
        preferences = new Preferences(context);
        bimestreMB = new BimestreMB(context);
        turma = realm.copyFromRealm(realm.where(Turma.class).equalTo("id", preferences.getSavedLong("id_turma")).findFirst());
    }

    public void configRealm() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void salvarFrequencia() {
        List<Frequencia> frequencias = realm.copyFromRealm(realm.where(Frequencia.class).findAll());
        for (Frequencia frequencia : frequencias) {

            if (frequencia.isNovo() || frequencia.isAlterado()) {
                Matricula matricula = realm.copyFromRealm(realm.where(Matricula.class).equalTo("id", frequencia.getMatricula()).findFirst());

                SerieDisciplina serieDisciplina = realm.copyFromRealm(realm.where(SerieDisciplina.class)
                        .equalTo("disciplina.id", preferences.getSavedLong("id_disciplina"))
                        .equalTo("serie.id", matricula.getSerie().getId()).findFirst());

                DisciplinaAluno da = realm.where(DisciplinaAluno.class).equalTo("serieDisciplina.id", serieDisciplina.getId())
                        .equalTo("matricula", frequencia.getMatricula()).findFirst();

                if (da != null) {
                    disciplinaAluno = realm.copyFromRealm(da);
                }

                AlunoFrequenciaMes alunoFrequenciaMes1 = recuperarAlunoFrequenciaMes(disciplinaAluno, frequencia);

                if (alunoFrequenciaMes1 == null) {
                    alunoFrequenciaMes1 = criarAlunoFrequenciaMes(matricula, disciplinaAluno, bimestreMB.getBimestreAtual());
                    matricula.getAlunoFrequenciasMes().add(alunoFrequenciaMes1);
                }

                atualizarTotalDeFaltas(alunoFrequenciaMes1, frequencia, matricula);

            }
        }
        Toast.makeText(context, "Frequência do dia " + new Date().getDate() + " Registrada!", Toast.LENGTH_LONG).show();
    }

    public AlunoFrequenciaMes recuperarAlunoFrequenciaMes(DisciplinaAluno disciplinaAluno, Frequencia frequencia) {
        AlunoFrequenciaMes alunoFrequenciaMes1;

        if (disciplinaAluno != null) {
            alunoFrequenciaMes1 = realm.where(AlunoFrequenciaMes.class)
                    .equalTo("matricula", frequencia.getMatricula()).equalTo("bimestre.id", bimestreMB.getBimestreAtual())
                    .equalTo("disciplinaAluno", disciplinaAluno.getId()).findFirst();

        } else {
            alunoFrequenciaMes1 = realm.where(AlunoFrequenciaMes.class)
                    .equalTo("matricula", frequencia.getMatricula())
                    .equalTo("disciplina.id", preferences.getSavedLong("id_disciplina"))
                    .equalTo("bimestre.id", bimestreMB.getBimestreAtual()).findFirst();
        }

        if (alunoFrequenciaMes1 != null)
            return realm.copyFromRealm(alunoFrequenciaMes1);

        return alunoFrequenciaMes1;
    }

    public void salvarNovoAlunoFrequenciaMes(AlunoFrequenciaMes alunoFrequenciaMes1, Frequencia frequencia) {
        realm.beginTransaction();

        if (!frequencia.isPresenca()) {
            alunoFrequenciaMes1.setTotalFaltas(alunoFrequenciaMes1.getTotalFaltas() + 1);
        }

        frequencia.setNovo(false);
        frequencia.setAlterado(false);

        alunoFrequenciaMes1.setTipoLancamentoFrequencia("LANCADO_APP");
        realm.copyToRealmOrUpdate(alunoFrequenciaMes1);
        realm.copyToRealmOrUpdate(frequencia);

        realm.commitTransaction();
    }

    public void atualizarAlunoFrequenciaMes(AlunoFrequenciaMes alunoFrequenciaMes1, Frequencia frequencia) {
        realm.beginTransaction();

        if (!frequencia.isPresenca()) {
            alunoFrequenciaMes1.setTotalFaltas(alunoFrequenciaMes1.getTotalFaltas() + 1);
        } else {
            if (alunoFrequenciaMes1.getTotalFaltas() > 0)
                alunoFrequenciaMes1.setTotalFaltas(alunoFrequenciaMes1.getTotalFaltas() - 1);
        }

        alunoFrequenciaMes1.setAlterado(true);

        frequencia.setNovo(false);
        frequencia.setAlterado(false);

        alunoFrequenciaMes1.setTipoLancamentoFrequencia("LANCADO_APP");
        realm.insertOrUpdate(alunoFrequenciaMes1);
        realm.copyToRealmOrUpdate(frequencia);
        realm.commitTransaction();

        alunoFrequenciaMes1 = realm.where(AlunoFrequenciaMes.class)
                .equalTo("matricula", frequencia.getMatricula()).equalTo("bimestre.id", bimestreMB.getBimestreAtual())
                .equalTo("disciplinaAluno", disciplinaAluno.getId()).findFirst();

        AlunoFrequenciaMes al = realm.copyFromRealm(alunoFrequenciaMes1);

//        Toast.makeText(context, "Frequência do dia " + new Date().getDate() + " Atualizada!", Toast.LENGTH_LONG).show();
    }

    public void atualizarTotalDeFaltas(AlunoFrequenciaMes alunoFrequenciaMes1, Frequencia frequencia, Matricula matricula) {
        if (alunoFrequenciaMes1.isNovo()) {
            if (alunoFrequenciaMes1 != null) {
                salvarNovoAlunoFrequenciaMes(alunoFrequenciaMes1, frequencia);
                realm.beginTransaction();
//                realm.copyToRealmOrUpdate(frequencia);
                realm.copyToRealmOrUpdate(matricula);
                realm.commitTransaction();
            }


        } else {
            if (alunoFrequenciaMes1 != null) {
                atualizarAlunoFrequenciaMes(alunoFrequenciaMes1, frequencia);
//                realm.beginTransaction();
//                realm.copyToRealmOrUpdate(frequencia);
//                realm.copyToRealmOrUpdate(matricula);
//                realm.commitTransaction();
                alunoFrequenciaMes1 = realm.where(AlunoFrequenciaMes.class)
                        .equalTo("matricula", frequencia.getMatricula()).equalTo("bimestre.id", bimestreMB.getBimestreAtual())
                        .equalTo("disciplinaAluno", disciplinaAluno.getId()).findFirst();

                AlunoFrequenciaMes al = realm.copyFromRealm(alunoFrequenciaMes1);
            }
        }
    }

    private AlunoFrequenciaMes criarAlunoFrequenciaMes(Matricula matricula, DisciplinaAluno disciplinaAluno, Long idBimestreAtual) {
        realm.beginTransaction();
        AlunoFrequenciaMes alunoFrequenciaMes1 = new AlunoFrequenciaMes();
        Long ultimoId = (Long) realm.where(AlunoFrequenciaMes.class).max("id");

        if (ultimoId != null) {
            alunoFrequenciaMes1.setId(ultimoId + 1);
        } else {
            alunoFrequenciaMes1.setId((long) (realm.where(AlunoFrequenciaMes.class).findAll().size() + 1));
        }

        alunoFrequenciaMes1.setNovo(true);
        alunoFrequenciaMes1.setMatricula(matricula.getId());
        alunoFrequenciaMes1.setBimestre(realm.copyFromRealm(realm.where(Bimestre.class).equalTo("id", idBimestreAtual).findFirst()));

        if (disciplinaAluno != null)
            alunoFrequenciaMes1.setDisciplinaAluno(disciplinaAluno.getId());

        alunoFrequenciaMes1.setTotalFaltas(0);
        alunoFrequenciaMes1.setDisciplina(realm.copyFromRealm(realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst()));

        realm.copyToRealmOrUpdate(alunoFrequenciaMes1);
        realm.commitTransaction();

        return alunoFrequenciaMes1;
    }
}
