package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Cliente;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/11/17.
 */

public class ClienteHttp {

    public static List<Cliente> carregarClientesJson(String var) {
        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"cliente/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonClientes(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Cliente> lerJsonClientes(JSONObject json) throws JSONException {
        List<Cliente> listaDeClientes = new ArrayList<Cliente>();

        JSONArray jsonCliente = json.getJSONArray("clientes");
        for (int i = 0; i < jsonCliente.length(); i++) {
            JSONObject jCliente = jsonCliente.getJSONObject(i);
            Cliente cliente = new Cliente(
                    jCliente.getInt("id"),
                    jCliente.getString("nome")
            );
            listaDeClientes.add(cliente);
        }
        return listaDeClientes;
    }
}
