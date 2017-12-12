package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ezzysoft.cardapiomobile.MainActivity;
import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.adapters.GruposListAdapter;
import br.com.ezzysoft.cardapiomobile.adapters.ProdutoExpandableListAdapter;
import br.com.ezzysoft.cardapiomobile.adapters.ProdutosListAdapter;
import br.com.ezzysoft.cardapiomobile.bean.Ajustes;
import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.dao.AjustesDAO;
import br.com.ezzysoft.cardapiomobile.dao.EzzyDB;
import br.com.ezzysoft.cardapiomobile.dao.GrupoDAO;
import br.com.ezzysoft.cardapiomobile.dao.ProdutoDAO;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;
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
    Map<String, List<String>> dados = new HashMap<String, List<String>>();
    List<Produto> mProdutos;
    EditText mMesa;
    ProdutosTask mTask;
    private Button mSubTotal;
    List<ItemPedido> itensPedidoList;
    ProdutosListAdapter produtosListAdapter;
    Pedido ped = new Pedido();
    private String endereco;
    private String porta;
    Bundle bundle;
    ProdutosListAdapter mAdapter;
    String grupo = null;
    private Context context;
    private EzzyDB mobiDB;
    AjustesDAO ajustesDAO;
    String var;





    public ProdutosListFragment() {
    }

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


        spnGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            String label = parent.getItemAtPosition(position).toString();
            // Showing selected spinner item
                grupo = label;
                iniciarDownload();
//            Toast.makeText(parent.getContext(), "Grupo: " + label, Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
            }
        });
        // Loading spinner data from database
        loadSpinnerData();

        mMesa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    new MesaTask().execute();
                }
            }
        });

        mSubTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mMesa.getText().toString().equals("") && !mSubTotal.getText().equals("0")) {
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
                    Toast.makeText(getActivity(), "Pedido sem item ou mesa", Toast.LENGTH_LONG).show();
                }
            }
        });
        return layout;
    }

    private void loadSpinnerData() {

        // Spinner Drop down elements
        List<String> lables = new GrupoDAO(getActivity()).getAllLabels(getActivity());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lables);


        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnGrupo.setAdapter(dataAdapter);
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
        mAdapter = new ProdutosListAdapter(this.getActivity(), mProdutos, this);
        mListView.setAdapter(mAdapter);
        eListView.setAdapter(new ProdutoExpandableListAdapter(dados));
        conecta();
    }

    public void conecta(){

        mobiDB = new EzzyDB(getContext());
        context = getContext();
        Ajustes ajustes;
        ajustesDAO = new AjustesDAO(getContext());
        ajustes = ajustesDAO.busca();
        var = ajustes.getIp() + ":" + ajustes.getPorta();
    }

    class ProdutosTask extends AsyncTask<Void, Void, List<Produto>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Produto> doInBackground(Void... strings) {
//            return ProdutoHttp.carregarProdutosJson(getEndereco() + ":" + getPorta());
            if (grupo == null || grupo.equals("Todos")) {
                return new ProdutoDAO(getActivity()).carregarProdutoDB(getActivity());
            } else {
                return new ProdutoDAO(getActivity()).buscarPorGrupo(getActivity(), grupo);
            }
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

    public class MesaTask extends AsyncTask<String, Context, String> {
        String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String ip = "";

            Ajustes ajustes;
            ajustesDAO = new AjustesDAO(context);
            try {
                ajustes = ajustesDAO.busca();
                ip = ajustes.getIp() + ":" + ajustes.getPorta();

                HttpURLConnection conexao = Utilitarios.connectar("http://" + ip + Utilitarios.APPURL+"teste/");
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
                Toast.makeText(context, "Sem Conexão, verifique seu sinal de internet", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Conectado ao Servidor", Toast.LENGTH_SHORT).show();
                //iniciarDownloadClientes();
            }
        }
    }
}