package com.android.educar.educar.mb;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.android.educar.educar.model.modelalterado.AlunoNotaMes;
import com.android.educar.educar.model.modelalterado.AnoLetivo;
import com.android.educar.educar.model.modelalterado.Bimestre;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.DisciplinaAluno;
import com.android.educar.educar.model.modelalterado.Funcionario;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.SerieDisciplina;
import com.android.educar.educar.model.modelalterado.Usuario;
import com.android.educar.educar.utils.Preferences;
import com.android.educar.educar.utils.UtilsFunctions;

import java.util.Date;

import io.realm.Realm;

public class NotaMB {

    private Realm realm;
    private Context context;
    private Preferences preferences;
    private DisciplinaAluno disciplinaAluno;
    private SerieDisciplina serieDisciplina;
    private BimestreMB bimestreMB;

    private AlunoNotaMes alunoNotaMes;

    public void configRealm() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public NotaMB(Context context) {
        preferences = new Preferences(context);
        this.context = context;
        this.disciplinaAluno = new DisciplinaAluno();
        this.serieDisciplina = new SerieDisciplina();
        bimestreMB = new BimestreMB(context);
        configRealm();
    }

    public void alertarErro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alerta!");
        builder.setMessage("Ocorreu um Erro, solicite Administrador!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    public void salvarNota(String descricao, Matricula matricula) {
        realm.beginTransaction();
        SerieDisciplina serieDisciplina = recuperarSerieDisciplina(matricula);
        DisciplinaAluno disciplinaAluno = recuperarDisciplinaAluno(matricula, serieDisciplina);

        AlunoNotaMes alunoNotaMes = recuperarAlunoNotaMes(matricula, disciplinaAluno);

        if (alunoNotaMes == null) {
            alunoNotaMes = cricarNovoAlunoNotaMes(matricula, disciplinaAluno);

            matricula.getAlunosNotaMes().add(alunoNotaMes);
            realm.copyToRealmOrUpdate(matricula);
        } else {
            if (!alunoNotaMes.isNovo()) {
                alunoNotaMes.setAlterado(true);
            }
        }

        alunoNotaMes.setNota(Float.valueOf(descricao));
        realm.copyToRealmOrUpdate(alunoNotaMes);
        realm.commitTransaction();

    }

    private AlunoNotaMes cricarNovoAlunoNotaMes(Matricula matricula, DisciplinaAluno disciplinaAluno) {
        AlunoNotaMes alunoNotaMes = new AlunoNotaMes();
//        realm.beginTransaction();
        alunoNotaMes.setMatricula(matricula.getId());
        Long ultimoId = (Long) realm.where(AlunoNotaMes.class).max("id");

        if (ultimoId == null) {
            alunoNotaMes.setId(realm.where(AlunoNotaMes.class).findAll().size() + 1);
        } else {
            alunoNotaMes.setId(ultimoId + 1);
        }

        alunoNotaMes.setTipoLancamentoNota("LANCAMENTO_APP");
        alunoNotaMes.setNota((float) 0);
        alunoNotaMes.setInseridoFechamento(false);
        alunoNotaMes.setAnoLetivo(realm.copyFromRealm(realm.where(AnoLetivo.class).findFirst()));
        alunoNotaMes.setBimestre(realm.copyFromRealm(realm.where(Bimestre.class).equalTo("id", bimestreMB.getBimestreAtual()).findFirst()));

        if (disciplinaAluno != null)
            alunoNotaMes.setDisciplinaAluno(disciplinaAluno.getId());

        alunoNotaMes.setDisciplina(realm.copyFromRealm(realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst()));
        alunoNotaMes.setDatahora(UtilsFunctions.formatoDataPadrao().format(new Date()));
        alunoNotaMes.setNovo(true);
        alunoNotaMes.setAlterado(false);
        alunoNotaMes.setSequencia(0);

        Usuario usuario = realm.copyFromRealm(realm.where(Funcionario.class).findFirst().getPessoaFisica().getUsuario());

        alunoNotaMes.setUsuario(usuario.getId());
        alunoNotaMes.setUnidade(preferences.getSavedLong("id_unidade"));

        realm.copyToRealmOrUpdate(alunoNotaMes);
//        realm.commitTransaction();

        return alunoNotaMes;
    }

    public AlunoNotaMes recuperarAlunoNotaMes(Matricula matricula, DisciplinaAluno disciplinaAluno) {
        AlunoNotaMes alunoNotaMes;
        if (disciplinaAluno != null) {
            alunoNotaMes = realm.where(AlunoNotaMes.class)
                    .equalTo("disciplinaAluno", disciplinaAluno.getId())
                    .equalTo("matricula", matricula.getId()).findFirst();
        } else {
            alunoNotaMes = realm.where(AlunoNotaMes.class)
                    .equalTo("bimestre.id", bimestreMB.getBimestreAtual())
                    .equalTo("disciplina.id", preferences.getSavedLong("id_disciplina"))
                    .equalTo("matricula", matricula.getId()).findFirst();
        }

        if (alunoNotaMes != null) {
            return realm.copyFromRealm(alunoNotaMes);
        }
        return alunoNotaMes;
    }

    public DisciplinaAluno recuperarDisciplinaAluno(Matricula matricula, SerieDisciplina serieDisciplina) {
        DisciplinaAluno disciplinaAluno = realm.where(DisciplinaAluno.class)
                .equalTo("matricula", matricula.getId())
                .equalTo("serieDisciplina.id", serieDisciplina.getId()).findFirst();

        if (disciplinaAluno != null)
            return realm.copyFromRealm(disciplinaAluno);

        return disciplinaAluno;
    }

    public SerieDisciplina recuperarSerieDisciplina(Matricula matricula) {

        SerieDisciplina se = realm.where(SerieDisciplina.class)
                .equalTo("serie.id", matricula.getSerie().getId())
                .equalTo("disciplina.id", preferences.getSavedLong("id_disciplina"))
                .findFirst();

        if (se != null)
            return realm.copyFromRealm(se);

        return se;
    }
}
