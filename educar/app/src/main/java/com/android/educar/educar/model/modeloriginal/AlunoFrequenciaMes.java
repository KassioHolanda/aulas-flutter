package com.android.educar.educar.model.modeloriginal;

import com.google.gson.annotations.SerializedName;

public class AlunoFrequenciaMes {
    private Long id;
    @SerializedName("totalfaltas")
    private int totalFaltas;
    @SerializedName("disciplinaaluno")
    private Long disciplinaAluno;
    private Long matricula;
    private Long bimestre;
    @SerializedName("tipolancamentofrequencia")
    private String tipoLancamentoFrequencia;
    private Long disciplina;

    public AlunoFrequenciaMes(Long id, int totalFaltas, Long disciplinaAluno,
                              Long matricula, Long bimestre, String tipoLancamentoFrequencia,
                              Long disciplina) {
        this.id = id;
        this.totalFaltas = totalFaltas;
        this.disciplinaAluno = disciplinaAluno;
        this.matricula = matricula;
        this.bimestre = bimestre;
        this.tipoLancamentoFrequencia = tipoLancamentoFrequencia;
        this.disciplina = disciplina;
    }

    public AlunoFrequenciaMes(int totalFaltas,
                              Long disciplinaAluno, Long matricula,
                              Long bimestre, String tipoLancamentoFrequencia,
                              Long disciplina) {

        this.totalFaltas = totalFaltas;
        this.disciplinaAluno = disciplinaAluno;
        this.matricula = matricula;
        this.bimestre = bimestre;
        this.tipoLancamentoFrequencia = tipoLancamentoFrequencia;
        this.disciplina = disciplina;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
