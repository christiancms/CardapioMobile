package br.com.ezzysoft.cardapiomobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.ItemPedido;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.fragments.ProdutosListFragment;

/**
 * Created by christian on 02/07/17.
 */

public class PedidoListAdapter extends ArrayAdapter<ItemPedido> {

    Context ctx;
    List<ItemPedido> itensPedido;
    private Map itensMap = new HashMap<Integer, String[]>();
    ProdutosListFragment prodListFragment;

    public PedidoListAdapter(@NonNull Context context, List<ItemPedido> itensPedido) {
        super(context, 0);
        this.ctx = context;
        this.itensPedido = itensPedido;
    }

    @Override
    public int getCount() {
        return itensPedido.size();
    }

    @Nullable
    @Override
    public ItemPedido getItem(int position) {
        return itensPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itensPedido.indexOf(getItem(position));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         final ItemPedido item = getItem(position);
        final DecimalFormat df = new DecimalFormat("##0.00");

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.itens_pedido_list, null);
            holder = new ViewHolder();
            holder.txtDescricaoProd = (TextView) convertView.findViewById(R.id.txtDescricaoProd);
            holder.txtQuantidadeProduto = (TextView) convertView.findViewById(R.id.txtQuantidadeProduto);
            holder.txtPrecoProduto = (TextView) convertView.findViewById(R.id.txtPrecoProduto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtDescricaoProd.setText("");
        holder.txtQuantidadeProduto.setText(df.format(item.getQuantidade()));
        holder.txtPrecoProduto.setText(df.format(item.getVlrUnitProduto()));


        String posicaoItem = Long.toString(getItemId(position));

        if (Double.parseDouble(posicaoItem) % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
    public Map getItensMap() {
        return itensMap;
    }

    static class ViewHolder {
        TextView txtDescricaoProd, txtQuantidadeProduto, txtPrecoProduto;
    }


}
