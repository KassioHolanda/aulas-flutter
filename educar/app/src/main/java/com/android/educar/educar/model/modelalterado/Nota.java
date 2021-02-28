package com.android.educar.educar.model.modelalterado;

import io.realm.RealmObject;

public class Nota extends RealmObject{

    private Long matricula;
    private float nota;

    public Nota() {
    }


    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }
}
