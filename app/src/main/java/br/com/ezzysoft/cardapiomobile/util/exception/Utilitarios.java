package br.com.ezzysoft.cardapiomobile.util.exception;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by christian on 30/06/17.
 */

public class Utilitarios {

    public static final String APPURL = "/restaurante/ws/";

    public static PackageInstaller.Session mySessionObject = null;

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        // O bufferzao vai armazenar todos os bytes lidos
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        // precisamos saber quantos bytes foram lidos
        int bytesLidos;
        // Vamos lendo de 1KB por vez...
        while ((bytesLidos = is.read(buffer)) != -1) {
            // copiando a quantidade de bytes lidos do buffer para o bufferzão
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }

    public static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(300 * SEGUNDOS);
        conexao.setConnectTimeout(300 * SEGUNDOS);
        conexao.setRequestMethod("POST");
        conexao.setDoOutput(true);
        conexao.setDoInput(true);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setRequestProperty("Accept-Charset", "UTF-8");
        conexao.connect();
        return conexao;
    }
}
