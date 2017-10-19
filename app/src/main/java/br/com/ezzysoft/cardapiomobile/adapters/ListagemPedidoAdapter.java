package br.com.ezzysoft.cardapiomobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.Pedido;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.fragments.ListagemPedidoFragment;

import static java.security.AccessController.getContext;

/**
 * Created by christian on 02/07/17.
 */

public class ListagemPedidoAdapter extends ArrayAdapter<Pedido> {

    Context ctx;
    List<Pedido> pedidos;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ListagemPedidoFragment lpf;

    private Map itensMap = new HashMap<Integer, String[]>();

    public ListagemPedidoAdapter(Context context, List<Pedido> pedidos, ListagemPedidoFragment lpf) {
        super(context, 0);
        this.ctx = context;
        this.pedidos = pedidos;
        this.lpf = lpf;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Pedido getItem(int position) {
        return pedidos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return pedidos.indexOf(getItem(position));
    }

    public Map getItensMap() {
        return itensMap;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pedido pedido = getItem(position);
        final DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.pedido_list, null);
            holder = new ViewHolder();
            holder.txtNpedido = (TextView) convertView.findViewById(R.id.txtNpedido);
            holder.txtClientePedido = (TextView) convertView.findViewById(R.id.txtClientePedido);
            holder.txtData = (TextView) convertView.findViewById(R.id.txtData);
            holder.txtTotalPedido = (TextView) convertView.findViewById(R.id.txtTotalPedido);
            holder.imgBullet = (ImageView) convertView.findViewById(R.id.imgBullet);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNpedido.setText(pedido.getIdPedido() != null ? String.valueOf(pedido.getIdPedido()) : pedido.getCodigo());
        holder.txtData.setText(pedido.getDataPedido());
        holder.txtTotalPedido.setText("");
        holder.txtClientePedido.setText(pedido.getMesa().toString());

        String posicaoCliente = Long.toString(getItemId(position));

        if (Double.parseDouble(posicaoCliente) % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtNpedido, txtClientePedido, txtData, txtTotalPedido;
        ImageView imgBullet;
    }
}