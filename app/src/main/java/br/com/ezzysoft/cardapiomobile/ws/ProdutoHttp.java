package br.com.ezzysoft.cardapiomobile.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

/**
 * Created by christian on 21/06/17.
 */

public class ProdutoHttp {

    public static OutputStream outputStream;

    private static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(300 * SEGUNDOS);
        conexao.setConnectTimeout(300 * SEGUNDOS);

        conexao.setRequestMethod("POST");
        conexao.setDoInput(true);
        conexao.setDoOutput(true);
        conexao.setRequestProperty("Content-Type", "application/xml");
        conexao.setRequestProperty("Accept-Charset", "UTF-8");
        conexao.connect();
        return conexao;
    }

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }




    public static void enviarPedidoXML(String enderecoPorta, String xml) {
        System.out.println(xml);
        URL url;
        try {
            HttpURLConnection conexao = connectar("http://" + enderecoPorta + Utilitarios.APPURL +"pedido/cria");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                outputStream = conexao.getOutputStream();
                outputStream.write(xml.getBytes());
                outputStream.flush();
            }



//            url = new URL("http://" + enderecoPorta + "/distribuido/ws/pedido/cria");
//            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
//            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                conexao.setReadTimeout(10000);
//                conexao.setConnectTimeout(15000);
//                conexao.setRequestMethod("POST");
//                conexao.setDoInput(true);
//                conexao.setDoOutput(true);
//                conexao.setRequestProperty("Content-Type", "application/xml");
//                conexao.setRequestProperty("Accept-Charset", "UTF-8");
//                outputStream = conexao.getOutputStream();
//                outputStream.write(xml.getBytes());
//                outputStream.flush();
            }
         catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static List<Produto> lerXmlProdutos(InputStream is) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        List<Produto> listaProdutos = new ArrayList<>();
//        Produto produto = null;
//        String tagAtual = "", subTag = "";
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        XmlPullParser xpp = factory.newPullParser();
//        xpp.setInput(is, "UTF-8");
//        int eventType = xpp.getEventType();
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if (eventType == XmlPullParser.START_TAG) {
//                tagAtual = xpp.getName();
//                if ("produto".equals(tagAtual)) {
//                    produto = new Produto();
//                }
//                if ("grupoId".equals(tagAtual)){
//                    subTag = tagAtual;
//                }
//                if ("marcaId".equals(tagAtual)){
//                    subTag = tagAtual;
//                }
//                if ("unidadeId".equals(tagAtual)){
//                    subTag = tagAtual;
//                }
//            } else if (eventType == XmlPullParser.END_TAG) {
//                if ("produto".equals(xpp.getName())) {
//                    listaProdutos.add(produto);
//                    subTag = "";
//                }
//                if ("grupoId".equals(xpp.getName())) {
//                    subTag = "";
//                }
//                if ("marcaId".equals(xpp.getName())) {
//                    subTag = "";
//                }
//                if ("unidadeId".equals(xpp.getName())) {
//                    subTag = "";
//                }
//            } else if (eventType == XmlPullParser.TEXT && !xpp.isWhitespace()) {
//                String text = xpp.getText();
//                if ("dataCadastro".equals(tagAtual)) {
//                    produto.setUltAtt(new Date());
//                }
//                if ("precoCompra".equals(tagAtual)) {
//                    produto.setPrecoVenda(text);
//                }
//                if ("sigla".equals(tagAtual)) {
//                    produto.setUnidade(text);
//                }
//                if ("id".equals(tagAtual) && subTag.equals("")) {
//                    produto.setId(Integer.parseInt(text));
//                }
//                if ("descricao".equals(tagAtual) && subTag.equals("")) {
//                    produto.setDescricao(text);
//                }else if("descricao".equals(tagAtual) && subTag.equals("grupoId")){
//                    produto.setGrupo(text);
//                }else if("descricao".equals(tagAtual) && subTag.equals("marcaId")){
//                    produto.setMarca(text);
//                } else if("descricao".equals(tagAtual) && subTag.equals("unidadeId")){
//
//                }
//            }
//            eventType = xpp.next();
//        }
//        return listaProdutos;
//    }

//    public static List<Produto> carregarProdutosXml(String enderecoPorta) {
//        try {
//            HttpURLConnection conexao = connectar("http://" + enderecoPorta + "/distribuido/ws/produto/lista");
//            int resposta = conexao.getResponseCode();
//            if (resposta == HttpURLConnection.HTTP_OK) {
//                InputStream is = conexao.getInputStream();
//
//                return lerXmlProdutos(is);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
    public static List<Produto> carregarProdutosJson(String var) {
        String stringJson;
        try {
            HttpURLConnection conexao = connectar("http://" + var + Utilitarios.APPURL+"produto/lista");
            int resposta = conexao.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                stringJson = String.valueOf(Utilitarios.bytesParaString(is));
                if (!stringJson.equals("0")) {
                    JSONObject json = new JSONObject(stringJson);
                    return lerJsonProdutos(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Produto> lerJsonProdutos(JSONObject json) throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Produto> listaProdutos = new ArrayList<Produto>();

        JSONArray jsonProduto = json.getJSONArray("produtos");
        for (int i = 0; i < jsonProduto.length(); i++) {
            JSONObject jProduto = jsonProduto.getJSONObject(i);
            Produto produto = new Produto(
                    jProduto.getLong("idProduto"),
                    jProduto.getString("descricao"),
                    jProduto.getDouble("preco"),
                    jProduto.getLong("grupoId"),
                    jProduto.getLong("marcaId"),
                    jProduto.getLong("unidadeId"),
                    jProduto.getString("nomeGrupo")

            );
            listaProdutos.add(produto);
        }
        return listaProdutos;
    }
//    private static String bytesParaString(InputStream is) throws IOException {
//        byte[] buffer = new byte[1024];
//        // O bufferzao vai armazenar todos os bytes lidos
//        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
//        // precisamos saber quantos bytes foram lidos
//        int bytesLidos;
//        // Vamos lendo de 1KB por vez...
//        while ((bytesLidos = is.read(buffer)) != -1) {
//            // copiando a quantidade de bytes lidos do buffer para o bufferzão
//            bufferzao.write(buffer, 0, bytesLidos);
//        }
//        return new String(bufferzao.toByteArray(), "UTF-8");
//    }
}
