package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Status;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/11/17.
 */

public class StatusHttp {
    public static List<Status> carregarStatusJson(String var) {
        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"status/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonStatus(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<Status> lerJsonStatus(JSONObject json) throws JSONException {
        List<Status> listaStatus = new ArrayList<Status>();

        JSONArray jsonStatus = json.getJSONArray("status");
        for (int i = 0; i < jsonStatus.length(); i++) {
            JSONObject jStatus = jsonStatus.getJSONObject(i);
            Status status = new Status(
                    jStatus.getInt("id"),
                    jStatus.getInt("indice"),
                    jStatus.getString("classe"),
                    jStatus.getString("opcao")
            );
            listaStatus.add(status);
        }
        return listaStatus;
    }

}
