package com.android.educar.educar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.educar.educar.R;
import com.android.educar.educar.mb.BimestreMB;
import com.android.educar.educar.mb.NotaMB;
import com.android.educar.educar.model.modelalterado.Bimestre;
import com.android.educar.educar.model.modelalterado.Disciplina;
import com.android.educar.educar.model.modelalterado.Matricula;
import com.android.educar.educar.model.modelalterado.Nota;
import com.android.educar.educar.ui.fragments.NotaFragment;
import com.android.educar.educar.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class NotaAdapterLista extends BaseAdapter {

    private List<Matricula> matriculaList;
    private Context context;
    private Realm realm;
    private NotaMB notaMB;

    protected TextView nomeAluno;
    protected CardView addNota;
    protected TextView idAluno;
    protected TextView notaAluno;

    private Preferences preferences;
    private List<Nota> notas;
    private Matricula matricula;
    private Matricula matriculaSelecionada;

    private BimestreMB bimestreMB;


    public NotaAdapterLista(List<Matricula> matriculaList, Context context) {
        this.matriculaList = matriculaList;
        this.context = context;
        notaMB = new NotaMB(context);
        notas = new ArrayList<>();
        preferences = new Preferences(context);
        bimestreMB = new BimestreMB(context);
        matriculaSelecionada = new Matricula();
        configRealm();
    }

    public void configRealm() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public int getCount() {
        return matriculaList.size();
    }

    @Override
    public Object getItem(int i) {
        return matriculaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout linearLayout;
        View row = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            row = inflater.inflate(R.layout.alunos_lista_nota, viewGroup, false);
        } else {
            row = view;
        }

        nomeAluno = row.findViewById(R.id.nomealuno_nota_id);
        addNota = row.findViewById(R.id.add_nota_id);
        idAluno = row.findViewById(R.id.idaluno_nota_id);
        notaAluno = row.findViewById(R.id.nota_aluno_id);

        int ordem = i;
        final int position = i;
        ordem = ordem + 1;

        matricula = matriculaList.get(position);

        idAluno.setText("" + ordem);
        nomeAluno.setText(matriculaList.get(i).getAluno().getPessoaFisica().getNome());

        try {
            notaAluno.setText("" + notaMB.recuperarAlunoNotaMes(matricula, null).getNota());
        } catch (NullPointerException e) {
            notaAluno.setText("Inserir");
        }

        notaAluno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) || keyCode == EditorInfo.IME_NULL) {
                    matriculaSelecionada = matriculaList.get(position);
                    preferences.saveLong("id_matricula", matricula.getId());
                    adicionarNota();
                    return true;
                }
                return false;
            }
        });

        addNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matriculaSelecionada = matriculaList.get(position);
                preferences.saveLong("id_matricula", matricula.getId());
                adicionarNota();
            }
        });

        return row;
    }

    public void adicionarNota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        final View viewDialog = inflater.inflate(R.layout.dialog_nova_nota, null);
        final EditText aluno = viewDialog.findViewById(R.id.nome_aluno_nota_id);
        final EditText disciplina = viewDialog.findViewById(R.id.disciplina_nota_id);
        final EditText bimestre = viewDialog.findViewById(R.id.bimestre_nota_id);
        final EditText nota = viewDialog.findViewById(R.id.nota_id);
        final EditText idAluno = viewDialog.findViewById(R.id.id_aluno_nota_id);

        idAluno.setText("" + matriculaSelecionada.getId());
        aluno.setText(matriculaSelecionada.getAluno().getPessoaFisica().getNome());
        disciplina.setText(realm.where(Disciplina.class).equalTo("id", preferences.getSavedLong("id_disciplina")).findFirst().getDescricao());
        bimestre.setText(realm.where(Bimestre.class).equalTo("id", bimestreMB.getBimestreAtual()).findFirst().getDescricao());

        try {
            nota.setText("" + notaMB.recuperarAlunoNotaMes(matriculaSelecionada, null).getNota());
        } catch (NullPointerException e) {
            nota.setText("");
        }


        builder.setView(viewDialog).setTitle("Inserir Nota")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nota.getText() == null || nota.getText().length() == 0) {
                            alertaInformacao();
                        } else if (Float.valueOf(nota.getText().toString()) > 10) {
                            alertaInformacao();
                        } else {
//                            if (notaMB.recuperarAlunoNotaMes(matriculaSelecionada) == null) {
                            Toast.makeText(context, "Nota Inserida ao aluno " + matriculaSelecionada.getAluno().getPessoaFisica().getNome() + "!", Toast.LENGTH_LONG).show();
                            notaMB.salvarNota(nota.getText().toString(), matriculaSelecionada);
//                            } else {
//                                Toast.makeText(context, "Nota do aluno " + matriculaSelecionada.getAluno().getPessoaFisica().getNome() + " Atualizada!", Toast.LENGTH_LONG).show();
//                                notaMB.atualizarAlunoNotaMes(nota.getText().toString(), matriculaSelecionada);
//                            }
                            atualizarFragment();
                        }
                    }
                }).setNegativeButton("Cancelar", null).show();
    }

    public void alertaInformacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Informação!");
        builder.setMessage("A nota Inserida é Inválida!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adicionarNota();
            }

        }).show();
    }

    public void atualizarFragment() {
//        context.startActivity(new Intent(context, NotaFragmentActivity.class));
        FragmentManager fragment = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();

        Fragment fragment1 = new NotaFragment();

        fragmentTransaction.add(R.id.id_layout_nota, fragment1);
        fragmentTransaction.commit();
    }
}
