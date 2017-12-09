package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 03/07/17.
 */

public class Colaborador implements Serializable{

    private long _id;
    private String nome;
    private long usuario_id;

    public Colaborador() {
    }

    public Colaborador(long _id, String nome) {
        this._id = _id;
        this.nome = nome;
    }

    public Colaborador(long _id, String nome, long usuario_id) {
        this._id = _id;
        this.nome = nome;
        this.usuario_id = usuario_id;
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

    public long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(long usuario_id) {
        this.usuario_id = usuario_id;
    }
}
