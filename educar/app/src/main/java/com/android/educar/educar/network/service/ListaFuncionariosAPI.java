package com.android.educar.educar.network.service;

import com.android.educar.educar.model.modelalterado.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class ListaFuncionariosAPI {

    private int count;
    private String next;
    private String previous;
    private List<Funcionario> results;

    public ListaFuncionariosAPI() {
        this.results = new ArrayList<>();
    }

    public List<Funcionario> getResults() {
        return results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

}
