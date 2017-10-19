package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.adapters.ProdutoExpandableListAdapter;
import br.com.ezzysoft.cardapiomobile.adapters.ProdutosListAdapter;
import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.ws.ProdutoHttp;

/**
 * Created by christian on 30/06/17.
 */

public class ProdutosListFragment extends Fragment {


    TextView mTextMesagem;
    ProgressBar mProgressBar;
    ListView mListView;
    ExpandableListView eListView;
    Spinner spnGrupo;
    List<String> listRs = new ArrayList<String>();
    Map<String, List<String>> dados = new HashMap<String, List<String>>();
    List<Produto> mProdutos;
    EditText mMesa;
    ProdutosTask mTask;
    private Button mSubTotal;
    List<ItemPedido> itensPedidoList;
    ProdutosListAdapter produtosListAdapter;
    Pedido ped = new Pedido();
    Boolean enviar = false;
    String docXml = "";
    private String endereco;
    private String porta;
    Bundle bundle;

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

    public List<ItemPedido> getItensPedidoList() {
        return itensPedidoList;
    }

    public Button getmSubTotal() {
        return mSubTotal;
    }

    public void setmSubTotal(Button mSubTotal) {
        this.mSubTotal = mSubTotal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_produtos_list, container, false);

        mTextMesagem = (TextView) layout.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        spnGrupo = (Spinner) layout.findViewById(R.id.spnGrupo);
        eListView = (ExpandableListView) layout.findViewById(R.id.expandableListView);
        eListView.setEmptyView(mTextMesagem);
        mListView = (ListView) layout.findViewById(R.id.list);
        mListView.setEmptyView(mTextMesagem);
        mMesa = (EditText) layout.findViewById(R.id.inputSearch);
        mSubTotal = (Button) layout.findViewById(R.id.btnSubTotal);

        mSubTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mMesa.getText().toString().equals("") || mSubTotal.getText().equals("0")) {
                    String intx = "";
                    Long codItem = 0L;
                    FragmentManager fragmentManager = getFragmentManager();
                    ped.setColaboradorId(1L);
                    ped.setMesa(Integer.parseInt(mMesa.getText().toString()));
                    Toast.makeText(getActivity(), "Subtotal: " + mSubTotal.getText(), Toast.LENGTH_SHORT).show();
                    produtosListAdapter = (ProdutosListAdapter) mListView.getAdapter();
                    ItemPedido item;
                    itensPedidoList = new ArrayList<>();
                    Iterator<Map.Entry<Integer, String[]>> iterator = produtosListAdapter.getItensMap().entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, String[]> reg = iterator.next();
                        item = new ItemPedido();
                        codItem++;
                        Double y = Double.parseDouble(reg.getValue()[1]);
                        if (y > 0d) {
                            try {
                                intx = "" + reg.getKey();
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }
                            item.setIdItem(codItem);
                            item.setProdutoId(Integer.valueOf(intx));
                            Double vlr = Double.parseDouble((reg.getValue()[0]).replace(",", "."));
                            item.setVlrUnitProduto(vlr);
                            item.setQuantidade(reg.getValue()[1] != null ? Double.parseDouble(reg.getValue()[1]) : 0d);
                            itensPedidoList.add(item);
                        }
                    }

                    String subTotal = mSubTotal.getText().toString();
                    String mesa = mMesa.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("mesa", mesa);
                    bundle.putString("mSubTotal", subTotal);
                    PedidoListFragment pedidoListFragment = new PedidoListFragment();
                    pedidoListFragment.setArguments(bundle);
                    List<ItemPedido> mLista;
                    pedidoListFragment.setmItensPedido(itensPedidoList);

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, pedidoListFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Informe a mesa", Toast.LENGTH_LONG).show();
                }
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mProdutos == null) {
            mProdutos = new ArrayList<>();
        }

        if (mTask == null) {
            if (ProdutoHttp.temConexao(getActivity())) {
                iniciarDownload();
            } else {
                mTextMesagem.setText("Sem conexão");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            exibirProgress(true);
        }
    }

    private void exibirProgress(boolean exibir) {
        if (exibir) {
            mTextMesagem.setText("Baixando informações dos produtos...");
        }
        mTextMesagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void iniciarDownload() {
        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new ProdutosTask();
            mTask.execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        List<String> listagemGrupos = new ArrayList<>();
        if (!mProdutos.isEmpty()){
            for (Produto p: mProdutos) {
                if (!listagemGrupos.contains(p.getNomeGrupo())) {
                    listagemGrupos.add(p.getNomeGrupo());
                }
            }
            Collections.sort(listagemGrupos);
            for (String nome :listagemGrupos) {
                dados.put(nome, listagemGrupos);
            }
        }
        final ProdutosListAdapter mAdapter = new ProdutosListAdapter(this.getActivity(), mProdutos, this);
        mListView.setAdapter(mAdapter);
        eListView.setAdapter(new ProdutoExpandableListAdapter(dados));
    }

    class ProdutosTask extends AsyncTask<Void, Void, List<Produto>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Produto> doInBackground(Void... strings) {
            return ProdutoHttp.carregarProdutosJson(getEndereco() + ":" + getPorta());
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            super.onPostExecute(produtos);
            exibirProgress(false);
            if (produtos != null) {
                mProdutos.clear();
                mProdutos.addAll(produtos);
            } else {
                mTextMesagem.setText("Falha ao obter produtos");
            }
            onStart();

        }
    }
}