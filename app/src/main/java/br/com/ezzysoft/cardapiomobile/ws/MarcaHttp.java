package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Marca;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/11/17.
 */

public class MarcaHttp {
    public static List<Marca> carregarMarcasJson(String var) {

        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"marca/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonMarcas(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<Marca> lerJsonMarcas(JSONObject json) throws JSONException {
        List<Marca> listaMarcas = new ArrayList<Marca>();

        JSONArray jsonMarcas = json.getJSONArray("marcas");
        for (int i = 0; i < jsonMarcas.length(); i++) {
            JSONObject jMarca = jsonMarcas.getJSONObject(i);
            Marca marca = new Marca(
                    jMarca.getInt("id"),
                    jMarca.getString("descricao")
            );
            listaMarcas.add(marca);
        }
        return listaMarcas;
    }
}
