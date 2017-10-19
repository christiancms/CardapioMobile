package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.ws.PedidoHttp;

/**
 * Created by christian on 30/06/17.
 */

public class PedidosListFragment extends Fragment {

    PedidosTask mTask;
    List<Pedido> mPedidos;
    ListView mListView;
    TextView mTextMensagem;
    ProgressBar mProgressBar;
    ArrayAdapter<Pedido> mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pedido_list, null);
        mTextMensagem = (TextView) layout.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        mListView = (ListView) layout.findViewById(R.id.list);
        mListView.setEmptyView(mTextMensagem);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPedidos == null) {
            mPedidos = new ArrayList<>();
        }
        mAdapter = new ArrayAdapter<Pedido>(getActivity(), android.R.layout.simple_list_item_1, mPedidos);
        mListView.setAdapter(mAdapter);
        if (mTask == null) {
            if (PedidoHttp.temConexao(getActivity())) {
                iniciarDownload();
            } else {
                mTextMensagem.setText("Sem conexão");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            exibirProgress(true);
        }
    }

    private void exibirProgress(boolean exibir) {
        if (exibir) {
            mTextMensagem.setText("Baixando informações dos pedidos...");
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void iniciarDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PedidosTask();
            mTask.execute();
        }
    }

    class PedidosTask extends AsyncTask<Void, Void, List<Pedido>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Pedido> doInBackground(Void... strings) {
            return PedidoHttp.carregarPedidosJson(getEndereco() + ":" + getPorta());
        }

        @Override
        protected void onPostExecute(List<Pedido> pedidos) {
            super.onPostExecute(pedidos);
            exibirProgress(false);
            if (pedidos != null) {
                mPedidos.clear();
                mPedidos.addAll(pedidos);
                mAdapter.notifyDataSetChanged();
            } else {
                mTextMensagem.setText("Falha ao obter pedidos");
            }


        }

        private String endereco;
        private String porta;

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getPorta() {
            return porta;
        }

        public void setPorta(String porta) {
            this.porta = porta;
        }
    }
}
