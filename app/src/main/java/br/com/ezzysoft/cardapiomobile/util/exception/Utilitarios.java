package br.com.ezzysoft.cardapiomobile.util.exception;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by christian on 30/06/17.
 */

public class Utilitarios {

    public static PackageInstaller.Session mySessionObject = null;

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private static String bytesParaString(InputStream is) throws IOException {
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
}
