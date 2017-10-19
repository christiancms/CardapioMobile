package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.adapters.PedidoListAdapter;
import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.ws.PedidoHttp;
import br.com.ezzysoft.cardapiomobile.ws.transporter.ItemTransporter;
import br.com.ezzysoft.cardapiomobile.ws.transporter.PedidoTransporter;

import static br.com.ezzysoft.cardapiomobile.R.id.lvPedido;
import static br.com.ezzysoft.cardapiomobile.R.id.txtClientePedido;

/**
 * Created by christian on 02/07/17.
 */

public class PedidoListFragment extends Fragment {

    final DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    List<ItemPedido> mItensPedido;
    PedidoListAdapter pla;
    Pedido pedido;
    ItemPedido iped;
    Button buttonEmitir;
    TextView mTotalPedido, mTextMesagem, mQuantidadeProduto, mDescricaoProd, mPrecoProduto, mClientePedido;
    ListView mListView;
    EditText addInfo;
    Context ctx = (Context)getActivity();
    PedidoTask mTask;
    PedidoTask mEnvioTask;

    Double valorTotalCompras;
    String obs, jsonNovoPedido="", var = "", respota;
    Boolean flag = false, status = false;
    private String endereco ;
    private String porta ;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mItensPedido == null) {
            mItensPedido = new ArrayList<>();
        }
        if (getArguments() != null) {
            if ((getArguments().getString("origemPedido")) == ("listaPedidos")) {
                int numeroPedido = Integer.parseInt(getArguments().getString("codPedido"));

                mTotalPedido.setText("");
            } else {
                mClientePedido.setText(getArguments().getString("mesa"));
                mTotalPedido.setText(getArguments().getString("mSubTotal"));
            }
        }

        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PedidoTask();
            mTask.execute();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle extras = getActivity().getIntent().getExtras();
        setEndereco(extras != null ? extras.getString("endereco") : "");
        setPorta(extras != null ? extras.getString("porta") : "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pedido_list, null);

        mListView = (ListView) layout.findViewById(lvPedido);
        ColorDrawable azul = new ColorDrawable(this.getResources().getColor(R.color.colorPrimary));
        mListView.setDivider(azul);
        mListView.setDividerHeight(3);

        mTextMesagem = (TextView) layout.findViewById(android.R.id.empty);
        mListView = (ListView) layout.findViewById(R.id.lvPedido);
        mQuantidadeProduto = (TextView) layout.findViewById(R.id.txtQuantidadeProduto);
        mDescricaoProd = (TextView) layout.findViewById(R.id.txtDescricaoProd);
        mPrecoProduto = (TextView) layout.findViewById(R.id.txtPrecoProduto);
        mListView.setEmptyView(mTextMesagem);
        mClientePedido = (TextView) layout.findViewById(R.id.txtClientePedido);
        mTotalPedido = (TextView) layout.findViewById(R.id.txtTotalPedido);
        addInfo = (EditText) layout.findViewById(R.id.edtPrazo);
        buttonEmitir = (Button) layout.findViewById(R.id.buttonEmitir);
        buttonEmitir.setVisibility(View.VISIBLE);

        if (getArguments() != null) {

            if (getArguments().getString("origemPedido") == "listaPedidos") {
                int numeroPedido = Integer.parseInt(getArguments().getString("codPedido"));
                if (getArguments().getString("origemPedido") == "listaPedidos") {
                    buttonEmitir.setText("Sincronizar");
                }
            }
        }


        buttonEmitir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Long retornoAlterar = 0l;

                Format formatter = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


                Toast.makeText(getActivity(), "Pedido Concluído ", Toast.LENGTH_LONG).show();
                pla = (PedidoListAdapter) mListView.getAdapter();
                List<ItemPedido> listaItens = new ArrayList<ItemPedido>();
                Iterator<Map.Entry<Integer, String[]>> iterator = pla.getItensMap().entrySet().iterator();
                obs = "" + addInfo.getText();

                pedido = new Pedido();
                pedido.setClienteId(0l); // Zero, não precisa informar o cliente.
                pedido.setColaboradorId(1l); // Christian
                pedido.setMesa(Integer.parseInt(mClientePedido.getText().toString()));
                pedido.setDescricao(obs);
                pedido.setHoraPedido(formatter.format(new Date()));
                pedido.setDataPedido(sdf.format(new Date()));

                for (ItemPedido elem: getmItensPedido()) {
                    iped = new ItemPedido();//mItensPedido
                    iped.setQuantidade(elem.getQuantidade());
                    iped.setProdutoId(elem.getProdutoId());
                    iped.setVlrUnitProduto(elem.getVlrUnitProduto());
                    listaItens.add(iped);
                }


                pedido.setItenspedido(listaItens);

                try {
                    jsonNovoPedido = pedidoToJson(pedido);
                    System.out.println(jsonNovoPedido);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!jsonNovoPedido.isEmpty()){
                    new EnvioTask().execute();
                }
            }
        });

        return layout;
    }

    public String pedidoToJson(Pedido elem) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("HH:mm:ss");

        Format formatter = new SimpleDateFormat("HH:mm:ss");
        System.out.println(formatter.format(new Date()));


        ObjectMapper mapper = new ObjectMapper();
        List<ItemPedido> itens;
        PedidoTransporter pt;
        ItemTransporter it;
        List<ItemTransporter> itenspedido = new ArrayList<>();

        String dados = "";
        if (elem != null) {

            pt = new PedidoTransporter();
            itens = new ArrayList<>();
//                java.sql.Time timeValue = new java.sql.Time(df.parse(elem.getHoraPedido()).getTime());

            pt.setIdPedido(elem.getIdPedido() != null ? elem.getIdPedido() : 0);
            pt.setClienteId(elem.getClienteId() != null ? elem.getClienteId() : 0L);
            pt.setColaboradorId(elem.getColaboradorId());
            pt.setDataPedido(elem.getDataPedido());
            pt.setHoraPedido(String.valueOf(formatter.format(new Date())));
            pt.setMesa(elem.getMesa());
            pt.setDescricao(elem.getDescricao() != null ? elem.getDescricao() : "");

            itens = elem.getItenspedido();
            itenspedido = new ArrayList<>();
            for (ItemPedido elemento : itens) {
                if (Objects.equals(elem.getIdPedido(), elemento.getPedidoId())) {
                    it = new ItemTransporter();
                    it.setIdItem(0l);
                    it.setPedidoId(elemento.getPedidoId() != null ? elemento.getPedidoId() : 0L);
                    it.setProdutoId(elemento.getProdutoId().longValue());
                    it.setQuantidade(elemento.getQuantidade());
                    itenspedido.add(it);
                }
            }
            pt.setItensTransporter(itenspedido);
            try {
                dados += mapper.writeValueAsString(pt);
                dados += ", ";
            } catch (JsonProcessingException ex) {
                Logger.getLogger(PedidoListFragment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dados = dados.substring(0, dados.length() - 2);
        }
        return dados;
    }

    public void setmItensPedido(List<ItemPedido> mItensPedido) {
        this.mItensPedido = mItensPedido;
    }

    public List<ItemPedido> getmItensPedido() {
        return mItensPedido;
    }

    @Override
    public void onStart() {
        super.onStart();

        final PedidoListAdapter mAdapter = new PedidoListAdapter(this.getActivity(), mItensPedido);
        mListView.setAdapter(mAdapter);
    }

    class PedidoTask extends AsyncTask<Void, Void, List<ItemPedido>> {


        @Override
        protected List<ItemPedido> doInBackground(Void... params) {

            return mItensPedido;
        }

        @Override
        protected void onPostExecute(List<ItemPedido> itemPedidos) {
            super.onPostExecute(itemPedidos);

            onStart();
        }
    }

    public void toServer() {
        FragmentManager fragmentManager = getFragmentManager();

            Toast.makeText(getActivity(), "Enviado para o Servidor.", Toast.LENGTH_SHORT).show();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ListagemPedidoFragment())
                    .addToBackStack(null)
                    .commit();
    }

    class EnvioTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            try {
                respota =  PedidoHttp.enviarJson(jsonNovoPedido, getEndereco() + ":" + getPorta());
                System.out.println("Resposta: "+respota);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            toServer();
        }
    }

}