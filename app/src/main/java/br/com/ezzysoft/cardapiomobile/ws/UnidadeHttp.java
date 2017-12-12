package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Unidade;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/11/17.
 */

public class UnidadeHttp {
    public static List<Unidade> carregarUnidadesJson(String var) {
        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"unidade/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonUnidades(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private static List<Unidade> lerJsonUnidades(JSONObject json) throws JSONException {
        List<Unidade> listaUnidades = new ArrayList<Unidade>();

        JSONArray jsonUnidades = json.getJSONArray("unidades");
        for (int i = 0; i < jsonUnidades.length(); i++) {
            JSONObject jUnidade = jsonUnidades.getJSONObject(i);
            Unidade unidade = new Unidade(
                    jUnidade.getInt("id"),
                    jUnidade.getString("sigla")
            );
            listaUnidades.add(unidade);
        }
        return listaUnidades;
    }
}
