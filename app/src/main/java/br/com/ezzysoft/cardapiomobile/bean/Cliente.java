package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by christian on 03/07/17.
 */

public class Cliente implements Serializable{

    private long _id;
    private String nome;

    public Cliente() {
    }

    public Cliente(long _id, String nome) {
        this._id = _id;
        this.nome = nome;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
