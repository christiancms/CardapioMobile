package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by christian on 21/06/17.
 */

public class ItemPedido implements Serializable {

    private Long idItem;
    private Integer produtoId;
    private Double quantidade;
    private Long pedidoId;
    private Double vlrUnitProduto;

    public ItemPedido() {
    }

    public ItemPedido(Integer produtoId, Double quantidade, Long pedidoId) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.pedidoId = pedidoId;
    }

    public ItemPedido(Long idItem, Integer produtoId, Double quantidade, Long pedidoId) {
        this.idItem = idItem;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.pedidoId = pedidoId;
    }

    public ItemPedido(Long idItem, Integer produtoId, Double quantidade, Long pedidoId, Double vlrUnitProduto) {
        this.idItem = idItem;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.pedidoId = pedidoId;
        this.vlrUnitProduto = vlrUnitProduto;
    }

    public Double getVlrUnitProduto() {
        return vlrUnitProduto;
    }

    public void setVlrUnitProduto(Double vlrUnitProduto) {
        this.vlrUnitProduto = vlrUnitProduto;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}
