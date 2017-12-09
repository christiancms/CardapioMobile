package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 25/06/17.
 */

public class Unidade implements Serializable {

    private long _id;
    private String descricao;
    private String sigla;

    public Unidade() {
    }

    public Unidade(long _id, String sigla) {
        this._id = _id;
        this.sigla = sigla;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
