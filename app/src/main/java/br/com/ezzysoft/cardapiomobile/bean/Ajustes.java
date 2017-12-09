package br.com.ezzysoft.cardapiomobile.bean;

import java.io.Serializable;

/**
 * Created by christian on 02/11/17.
 */

public class Ajustes implements Serializable {

    private Long id;
    private String ip;
    private String porta;

    public Ajustes() {
    }

    public Ajustes(String ip, String porta) {
        this.ip = ip;
        this.porta = porta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }
}
