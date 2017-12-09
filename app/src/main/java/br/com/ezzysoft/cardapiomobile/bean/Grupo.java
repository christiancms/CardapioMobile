package br.com.ezzysoft.cardapiomobile.bean;

/**
 * Created by christian on 07/05/17.
 */
public class Grupo {

    private long _id;
    private String descricao;

    public Grupo() {
    }

    public Grupo(long _id, String descricao) {
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
