package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 02/11/17.
 */

public class Status implements Serializable {

    private long _id;
    private int indice;
    private String classe;
    private String opcao;

    public Status() {
    }

    public Status(long _id, int indice, String classe, String opcao) {
        this._id = _id;
        this.indice = indice;
        this.classe = classe;
        this.opcao = opcao;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }
}
