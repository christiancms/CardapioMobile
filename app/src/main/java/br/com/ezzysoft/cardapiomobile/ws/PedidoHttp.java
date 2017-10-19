package br.com.ezzysoft.cardapiomobile.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.bean.Produto;

/**
 * Created by christian on 21/06/17.
 */

public class PedidoHttp {


    OutputStreamWriter osw;

    private static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(300 * SEGUNDOS);
        conexao.setConnectTimeout(300 * SEGUNDOS);

        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setRequestProperty("Accept-Charset", "UTF-8");
        conexao.setDoInput(true);
        conexao.setDoOutput(true);
        conexao.setRequestMethod("PUT");
        conexao.connect();
        return conexao;
    }

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static List<Pedido> carregarPedidosJson(String enderecoPorta) {
        try {
            HttpURLConnection conexao = connectar("http://" + enderecoPorta + "/restaurante/ws/pedido/lista");
            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonPedidos(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Pedido> lerJsonPedidos(JSONObject json) throws JSONException {
        System.out.println(json);
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Pedido p;
        String dataFormatada = "";
        ItemPedido iped;
        List<Pedido> listaDePedidos = new ArrayList<>();
        List<ItemPedido> listaItens = new ArrayList<>();
        JSONArray jsonPedidos = json.getJSONArray("pedidos");
        for (int i = 0; i < jsonPedidos.length(); i++) {
            JSONObject jsonPed = jsonPedidos.getJSONObject(i);//  if (jsonPed.has("horaPedido")){ }

            p = new Pedido();
            Object o = jsonPed.getInt("idPedido");
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println(o.getClass().getName());
            System.out.println(p.getClass().getName());
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            p.setIdPedido(Integer.parseInt(jsonPed.getString("idPedido")));
            p.setCodigo(jsonPed.getString("idPedido"));
            try {

                Date data = formato.parse(jsonPed.getString("dataPedido"));
                formato.applyPattern("dd/MM/yyyy");
                dataFormatada = formato.format(data);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println(dataFormatada);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            } catch (ParseException ex) {
            }
            p.setDataPedido(dataFormatada);
            p.setHoraPedido(jsonPed.getString("horaPedido"));
            p.setDescricao(jsonPed.getString("descricao"));
            p.setClienteId(jsonPed.getLong("clienteId"));
            p.setColaboradorId(jsonPed.getLong("colaboradorId"));
            p.setMesa(jsonPed.getInt("mesa"));

            JSONArray jsonItens = jsonPed.getJSONArray("itensPedido");
            for (int j = 0; j < jsonItens.length(); j++) {
                JSONObject jsonIped = jsonItens.getJSONObject(j);
                iped = new ItemPedido(
                        jsonIped.getLong("idItem"),
                        jsonIped.getInt("produtoId"),
                        jsonIped.getDouble("quantidade"),
                        jsonIped.getLong("pedidoId")
                );
                listaItens.add(iped);
            }
            p.setItenspedido(listaItens);
            listaDePedidos.add(p);
        }
        return listaDePedidos;
    }

    public static String enviarJson(String jsonNovoPedido, String enderecoPorta) throws JSONException {
//        String ret = "erro";
//        HttpURLConnection conexao = null;
//        OutputStream outputStream;
//        try {
//            conexao = connectar("http://" + enderecoPorta + "/distribuido/ws/pedido/cria");
//            outputStream = conexao.getOutputStream();
//            outputStream.write(jsonNovoPedido.getBytes());
//            outputStream.flush();
//
//            ret = "ok";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conexao != null) {
//                conexao.disconnect();
//            }
//        }
//        return ret;

        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 25000);
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://" + enderecoPorta + "/restaurante/ws/pedido/cria");
        try {
            httpPost.setEntity(new StringEntity(jsonNovoPedido));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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

//    public static List<Pedido> carregarPedidosXml(String enderecoPorta) {
//        try {
//            HttpURLConnection conexao = connectar("http://" + enderecoPorta + "/distribuido/ws/produto/lista");
//            int resposta = conexao.getResponseCode();
//            if (resposta == HttpURLConnection.HTTP_OK) {
//                InputStream is = conexao.getInputStream();
//
//                return lerXmlPedidos(is);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public static List<Pedido> lerXmlPedidos(InputStream is) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        List<Pedido> listaPedidos = new ArrayList<>();
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
//                if ("grupoId".equals(tagAtual)) {
//                    subTag = tagAtual;
//                }
//                if ("marcaId".equals(tagAtual)) {
//                    subTag = tagAtual;
//                }
//                if ("unidadeId".equals(tagAtual)) {
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
//                } else if ("descricao".equals(tagAtual) && subTag.equals("grupoId")) {
//                    produto.setGrupo(text);
//                } else if ("descricao".equals(tagAtual) && subTag.equals("marcaId")) {
//                    produto.setMarca(text);
//                } else if ("descricao".equals(tagAtual) && subTag.equals("unidadeId")) {
//
//                }
//            }
//            eventType = xpp.next();
//        }
//        return listaPedidos;
//    }