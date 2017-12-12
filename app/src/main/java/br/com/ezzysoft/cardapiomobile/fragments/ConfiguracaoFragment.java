package br.com.ezzysoft.cardapiomobile.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.Ajustes;
import br.com.ezzysoft.cardapiomobile.dao.AjustesDAO;
import br.com.ezzysoft.cardapiomobile.util.exception.ErroSistema;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;


public class ConfiguracaoFragment extends Fragment {


    private EditText edtEndereco, edtPorta, edtPassword;
    private Button btnSalvar, btnTestar, btnLimpar;
    AjustesDAO ajustesDAO;
    Ajustes ajustes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // pesquisar

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_configuracao, null);
//        return inflater.inflate(R.layout.fragment_configuracao, container, false);

        edtEndereco = (EditText) layout.findViewById(R.id.edtEndereco);
        edtPorta = (EditText) layout.findViewById(R.id.edtPorta);
        edtPassword = (EditText) layout.findViewById(R.id.password);
        btnLimpar = (Button) layout.findViewById(R.id.btnLimpar);
        btnSalvar = (Button) layout.findViewById(R.id.btnSalvar);
        btnTestar = (Button) layout.findViewById(R.id.btnTestar);

        onLoad();

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().deleteDatabase("ezzydb.sqlite");
                Toast.makeText(getActivity(), "Banco de Dados Apagado com Sucesso!", Toast.LENGTH_LONG).show();
                Log.e("Botão Limpa Dados", "DROP Banco de Dados");


            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajustesDAO = new AjustesDAO(getActivity());

                try {
                    Long returnAjustes = ajustesDAO.count();
                    if (returnAjustes != null && returnAjustes == 0) {
                        salvar();
                    } else {
                        alterar();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ErroSistema erroSistema) {
                    erroSistema.printStackTrace();
                }

            }
        });

        btnTestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Long returnAjustes = ajustesDAO.count();
                    if (returnAjustes != null && returnAjustes == 0) {
                        salvar();
                    } else {
                        alterar();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ErroSistema erroSistema) {
                    erroSistema.printStackTrace();
                }

                new TestTask().execute();

            }


        });

        return layout;
    }

    private void onLoad(){
        try {
            carregar();
        } catch (ErroSistema erroSistema) {
            erroSistema.printStackTrace();
        } finally {
            Log.e("Carregar:", "Informações do banco de dados!");
        }
    }

    private void carregar() throws ErroSistema {
        Ajustes ajustes;

        ajustesDAO = new AjustesDAO(getActivity());
        ajustes = ajustesDAO.busca();

        edtEndereco.setText((ajustes.getIp() != null ? ajustes.getIp() : ""));
        edtPorta.setText((ajustes.getPorta() != null ? ajustes.getPorta() : ""));
    }

    private Long salvar() throws IOException {
        ajustes = new Ajustes();
        ajustesDAO = new AjustesDAO(getActivity());

        Long codRetorno = 0l;

        ajustes.setIp(edtEndereco.getText() != null ? edtEndereco.getText().toString() : "");
        ajustes.setPorta(edtPorta.getText() != null ? edtPorta.getText().toString() : "");

        try {
            codRetorno = ajustesDAO.salvar(ajustes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Toast.makeText(getActivity(), "Salvo com Sucesso", Toast.LENGTH_LONG).show();

        return codRetorno;
    }

    private Long alterar() throws IOException, ErroSistema {
        ajustes = new Ajustes();
        ajustesDAO = new AjustesDAO(getActivity());
        ajustes.setIp(edtEndereco.getText() != null ? edtEndereco.getText().toString() : "");
        ajustes.setPorta(edtPorta.getText() != null ? edtPorta.getText().toString() : "");

        Long codRetorno;
        codRetorno = ajustesDAO.modifica(ajustes);
        Toast.makeText(getActivity(), "Salvo com Sucesso", Toast.LENGTH_LONG).show();
        return codRetorno;
    }

    public static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(13 * SEGUNDOS);
        conexao.setConnectTimeout(10 * SEGUNDOS);
        conexao.setRequestMethod("POST");
        conexao.setDoOutput(false);
        conexao.setDoInput(true);
        conexao.setRequestProperty("Content-Type", "application/json");
        conexao.setRequestProperty("Accept-Charset", "UTF-8");
        conexao.connect();
        return conexao;
    }

    public class TestTask extends AsyncTask<String, Context, String>{
        String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ajustesDAO = new AjustesDAO(getActivity());

            try {
                ajustes = ajustesDAO.busca();

                HttpURLConnection conexao = connectar("http://" + ajustes.getIp() + ":" + ajustes.getPorta() + Utilitarios.APPURL+"teste/");
                int resposta = conexao.getResponseCode();

                if (resposta == HttpURLConnection.HTTP_OK) {
                    status = "Conectado";
                } else {
                    status = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            if (status.isEmpty() || status == null) {
                Toast.makeText(getActivity(), "Sem Conexão", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Conectado ao Servidor", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
