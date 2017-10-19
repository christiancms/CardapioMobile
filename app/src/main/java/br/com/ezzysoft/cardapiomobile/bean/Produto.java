package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 21/06/17.
 */

public class Produto implements Serializable {

    private Long idProduto;
    private String descricao;
    private Double preco;
    private String dataCadastro;
    private Long grupoId;
    private Long marcaId;
    private Long unidadeId;
    private Grupo grupo;
    private Marca marca;
    private Unidade unidade;
    private String nomeGrupo;

    public Produto() {
    }

    public Produto(Long idProduto, String descricao, Double preco, Long grupoId, Long marcaId, Long unidadeId, String nomeGrupo) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.grupoId = grupoId;
        this.marcaId = marcaId;
        this.unidadeId = unidadeId;
        this.nomeGrupo = nomeGrupo;
    }

    public Produto(Long idProduto, String descricao, Double preco, String dataCadastro, Long grupoId, Long marcaId, Long unidadeId) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.dataCadastro = dataCadastro;
        this.grupoId = grupoId;
        this.marcaId = marcaId;
        this.unidadeId = unidadeId;
    }

    public Produto(Long idProduto, String descricao, Double preco, Long grupoId, Long marcaId, Long unidadeId) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.grupoId = grupoId;
        this.marcaId = marcaId;
        this.unidadeId = unidadeId;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public Long getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Long unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
}
