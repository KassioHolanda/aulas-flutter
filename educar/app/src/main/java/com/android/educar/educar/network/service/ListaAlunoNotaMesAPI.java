package com.android.educar.educar.network.service;

import com.android.educar.educar.model.modelalterado.AlunoNotaMes;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunoNotaMesAPI {
    private int count;
    private String next;
    private String previous;
    private List<AlunoNotaMes> results;

    public ListaAlunoNotaMesAPI() {
        this.results = new ArrayList<>();
    }

    public List<AlunoNotaMes> getResults() {
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
