package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 25/06/17.
 */

public class Marca implements Serializable {

    private long _id;
    private String descricao;

    public Marca() {
    }

    public Marca(long _id, String descricao) {
        this._id = _id;
        this.descricao = descricao;
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
}
