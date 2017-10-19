package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.adapters.ListagemPedidoAdapter;
import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.ws.PedidoHttp;
import br.com.ezzysoft.cardapiomobile.ws.ProdutoHttp;

/**
 * Created by christian on 02/07/17.
 */

public class ListagemPedidoFragment extends Fragment {

    List<Pedido> mPedidos;
    PedidoListagemTask mTask;
    Pedido pedido;
    TextView txtNpedido, txtClientePedido, txtData, txtTotalPedido, mTextMesagem;
    ListView mListView;
    Double valorTotalCompras;
    private String endereco;
    private String porta;
    ProgressBar mProgressBar;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle extras = getActivity().getIntent().getExtras();
        setEndereco(extras != null ? extras.getString("endereco") : "");
        setPorta(extras != null ? extras.getString("porta") : "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_listagem_pedido, null);
        mListView = (ListView) layout.findViewById(R.id.listagemPedido);
        mTextMesagem = (TextView) layout.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        mListView.setEmptyView(mTextMesagem);
        ColorDrawable azul = new ColorDrawable(this.getResources().getColor(R.color.colorPrimary));
        mListView.setDivider(azul);
        mListView.setDividerHeight(3);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                FragmentManager fragmentManager = getFragmentManager();

                pedido = (Pedido) adapterView.getItemAtPosition(position);
                if (pedido != null) {

                    String codigoPed = pedido.getCodigo();
                    Bundle bundle = new Bundle();
                    bundle.putString("codPedido", codigoPed);
                    bundle.putString("origemPedido", "listaPedidos");

                    PedidoListFragment pelf = new PedidoListFragment();
                    pelf.setArguments(bundle);


                    List<ItemPedido> mLista = new ArrayList<ItemPedido>();
                    pelf.setmItensPedido((List<ItemPedido>) mLista);

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, pelf)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        txtNpedido = (TextView) layout.findViewById(R.id.txtNpedido);
        txtClientePedido = (TextView) layout.findViewById(R.id.txtClientePedido);
        txtData = (TextView) layout.findViewById(R.id.txtData);
        txtTotalPedido = (TextView) layout.findViewById(R.id.txtTotalPedido);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mPedidos == null) {
            mPedidos = new ArrayList<>();
        }

        if (mTask == null) {
            iniciarDownload();
        }
    }
    private void exibirProgress(boolean exibir) {
        if (exibir) {
            mTextMesagem.setText("Baixando informações dos pedidos...");
        }
        mTextMesagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void iniciarDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PedidoListagemTask();
            mTask.execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final ListagemPedidoAdapter mAdapter = new ListagemPedidoAdapter(this.getActivity(), mPedidos, this);
        mListView.setAdapter(mAdapter);
//        mListView.setSelection(0);
    }

    class PedidoListagemTask extends AsyncTask<Void, Void, List<Pedido>> {
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
            } else {
                mTextMesagem.setText("Sem pedidos");
            }
            onStart();
        }
    }
}
