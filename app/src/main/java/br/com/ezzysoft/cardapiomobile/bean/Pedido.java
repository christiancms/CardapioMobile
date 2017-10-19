package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by christian on 21/06/17.
 */

public class Pedido implements Serializable {

    private Integer idPedido;
    private String dataPedido;
    private String horaPedido;
    private String descricao;
    private Long clienteId;
    private Long colaboradorId;
    private Integer mesa;
    private List<ItemPedido> itenspedido;
    private String codigo;

    public Pedido() {
    }

    public Pedido(Integer idPedido, String dataPedido, String horaPedido, String descricao, Long clienteId, Long colaboradorId, Integer mesa) {
        this.idPedido = idPedido;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.descricao = descricao;
        this.clienteId = clienteId;
        this.colaboradorId = colaboradorId;
        this.mesa = mesa;
    }

    public Pedido(Integer idPedido, String dataPedido, String horaPedido, String descricao, Long clienteId, Long colaboradorId, Integer mesa, List<ItemPedido> itenspedido) {
        this.idPedido = idPedido;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.descricao = descricao;
        this.clienteId = clienteId;
        this.colaboradorId = colaboradorId;
        this.mesa = mesa;
        this.itenspedido = itenspedido;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = this.idPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
    }

    public List<ItemPedido> getItenspedido() {
        return itenspedido;
    }

    public void setItenspedido(List<ItemPedido> itenspedido) {
        this.itenspedido = itenspedido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.idPedido);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.idPedido, other.idPedido)) {
            return false;
        }
        return true;
    }
}