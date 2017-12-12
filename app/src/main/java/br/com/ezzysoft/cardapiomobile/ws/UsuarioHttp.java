package br.com.ezzysoft.cardapiomobile.ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Usuario;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 02/11/17.
 */

public class UsuarioHttp {

    public static List<Usuario> carregarUsuariosJson(String var) {
        String teste;
        try {
            HttpURLConnection conexao = Utilitarios.connectar("http://" + var + Utilitarios.APPURL+"usuario/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                teste = String.valueOf(Utilitarios.bytesParaString(is));
                if (!teste.equals("0")) {
                    JSONObject json = new JSONObject(teste);
                    return lerJsonUsuarios(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Usuario> lerJsonUsuarios(JSONObject json) throws JSONException {
        List<Usuario> listaDeUsuarios = new ArrayList<Usuario>();

        JSONArray jsonUsuarios = json.getJSONArray("usuarios");
        for (int i = 0; i < jsonUsuarios.length(); i++) {
            JSONObject jUsuario = jsonUsuarios.getJSONObject(i);
            Usuario usuario = new Usuario(
                    jUsuario.getInt("id"),
                    jUsuario.getString("usuario"),
                    jUsuario.getString("senha")
            );
            listaDeUsuarios.add(usuario);
        }
        return listaDeUsuarios;
    }
}
