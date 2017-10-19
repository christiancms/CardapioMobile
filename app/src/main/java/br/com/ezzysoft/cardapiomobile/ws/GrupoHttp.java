package br.com.ezzysoft.cardapiomobile.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Grupo;

/**
 * Created by christian on 07/05/17.
 */
public class GrupoHttp {

    public static final String
    GRUPOS_URL_JSON = "";

    private static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(300 * SEGUNDOS);
        conexao.setConnectTimeout(300 * SEGUNDOS);
        conexao.setRequestMethod("POST");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setRequestProperty("Accept-Charset", "UTF-8");
        conexao.connect();
        return conexao;
    }





    public static List<Grupo> lerJsonGrupos(JSONObject json) throws JSONException {
        List<Grupo> listaDeGrupos = new ArrayList<Grupo>();

        JSONArray jsonGrupo = json.getJSONArray("grupo");
        for (int i = 0; i < jsonGrupo.length(); i++) {
            JSONObject jsonGrupos = jsonGrupo.getJSONObject(i);
            Grupo grupo = new Grupo(
                    jsonGrupos.getInt("id"),
                    jsonGrupos.getString("descricao")
            );
            listaDeGrupos.add(grupo);
        }
        return listaDeGrupos;
    }

}
