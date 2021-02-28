package com.android.educar.educar.model.modelalterado;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Serie extends RealmObject{

    @PrimaryKey
    private Long id;
    private String descricao;
    private String nivel;


    public Serie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}
