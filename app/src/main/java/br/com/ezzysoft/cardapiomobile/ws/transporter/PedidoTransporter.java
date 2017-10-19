package br.com.ezzysoft.cardapiomobile.ws.transporter;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by christian on 02/07/17.
 */

public class PedidoTransporter implements Serializable {

    private Integer idPedido;
    private String dataPedido;
    private String horaPedido;
    private String descricao;
    private Long clienteId;
    private Long colaboradorId;
    private Integer mesa;
    @JsonProperty("itensPedido")
    private List<ItemTransporter> itensTransporter = new ArrayList<>();

    public PedidoTransporter() {
    }

    public PedidoTransporter(Integer idPedido, String dataPedido, String horaPedido, String descricao, Long clienteId, Long colaboradorId, Integer mesa) {
        this.idPedido = idPedido;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.descricao = descricao;
        this.clienteId = clienteId;
        this.colaboradorId = colaboradorId;
        this.mesa = mesa;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }

    public List<ItemTransporter> getItensTransporter() {
        return itensTransporter;
    }

    public void setItensTransporter(List<ItemTransporter> itensTransporter) {
        this.itensTransporter = itensTransporter;
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

}
