package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Colaborador;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/07/17.
 */

public class ColaboradorHttp {
    public static List<Colaborador> carregarColaboradoresJson(String var) {

        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"colaborador/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonColaboradores(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Colaborador> lerJsonColaboradores(JSONObject json) throws JSONException {
        List<Colaborador> listaColaboradores = new ArrayList<Colaborador>();

        JSONArray jsonColaboradores = json.getJSONArray("colaboradores");
        for (int i = 0; i < jsonColaboradores.length(); i++) {
            JSONObject jColaborador = jsonColaboradores.getJSONObject(i);
            Colaborador colaborador = new Colaborador(
                    jColaborador.getInt("idColaborador"),
                    jColaborador.getString("nome"),
                    jColaborador.getInt("usuarioId")
            );
            listaColaboradores.add(colaborador);
        }
        return listaColaboradores;
    }
}
