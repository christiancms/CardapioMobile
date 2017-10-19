package br.com.ezzysoft.cardapiomobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.fragments.ProdutosListFragment;

/**
 * Created by christian on 30/06/17.
 */

public class ProdutosListAdapter extends ArrayAdapter<Produto> {

    final DecimalFormat df = new DecimalFormat("##0.00");
    List<Produto> produtos;
    Context ctx;
    private Map itensMap = new HashMap<Integer, String[]>();
    HashMap<Integer, Double> produtoQuantidade = new HashMap<Integer, Double>();


    Double quantidade;
    Double subTotal = 0d;
    ProdutosListFragment plf;
    String dados[];
    Double totalParcial = 0d;

    public ProdutosListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ProdutosListAdapter(Context context, List<Produto> listaProdutos, ProdutosListFragment plf){
        super(context, 0);
        this.ctx = context;
        this.produtos = listaProdutos;
        this.plf = plf;
    }

    public Map getItensMap() {
        return itensMap;
    }

    public void setItensMap(Map itensMap) {
        this.itensMap = itensMap;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Produto getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return produtos.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Produto produto = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_produto_list, null);
            holder = new ViewHolder();
            holder.txtDescricaoProd = (TextView) convertView.findViewById(R.id.txtDescricaoProd);
            holder.txtPrecoProduto = (TextView) convertView.findViewById(R.id.txtPrecoProduto);
            holder.txtQuantidadeProduto = (TextView) convertView.findViewById(R.id.txtQuantidadeProduto);
            holder.tvPosicaoProduto = (TextView) convertView.findViewById(R.id.tvPosicaoProduto);
            holder.btnAdd = (Button) convertView.findViewById(R.id.btnAdd);
            holder.btnRem = (Button) convertView.findViewById(R.id.btnRem);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.tvPosicaoProduto.setText(Long.toString(getItemId(position)));
        holder.txtDescricaoProd.setText(produto.getIdProduto() + " - " + produto.getDescricao());
        holder.txtPrecoProduto.setText(produto.getPreco().toString());
        if (produtoQuantidade.containsKey(produto.getIdProduto())) {
            holder.txtQuantidadeProduto.setText(String.valueOf(produtoQuantidade.get(produto.getIdProduto())));
        } else {
            holder.txtQuantidadeProduto.setText("0");
        }

        final DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dados = new String[2];
                quantidade = 1 + Double.parseDouble(holder.txtQuantidadeProduto.getText().toString());
                Double qtde = updateQuantidade(produto.getIdProduto().intValue(), quantidade);
                dados[0] = produto.getPreco().toString(); // preço unitário
                dados[1] = String.valueOf(quantidade); // quantidade.
                itensMap.put(produto.getIdProduto(), dados);
                Double x = updateItensLista(itensMap);// id, [preço, qtde, descont]
                holder.txtQuantidadeProduto.setText(String.valueOf(qtde));
                String valorFormatado = decimalFormat.format(x);
                plf.getmSubTotal().setText(valorFormatado.replace(",","."));
            }
        });
        holder.btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dados = new String[2];
                quantidade = Double.parseDouble(holder.txtQuantidadeProduto.getText().toString());
                if (quantidade > 0) {
                    quantidade = quantidade - 1;
                    Double qtde = updateQuantidade(produto.getIdProduto().intValue(), quantidade);

                    dados[0] = produto.getPreco().toString(); // preço unitário
                    dados[1] = String.valueOf(qtde); // quantidade
                    itensMap.put(produto.getIdProduto(), dados);
                    Double x = updateItensLista(itensMap);
                    holder.txtQuantidadeProduto.setText(String.valueOf(qtde));
                    String valorFormatado = decimalFormat.format(x);
                    plf.getmSubTotal().setText(valorFormatado.replace(",","."));
                } else {
                    Toast.makeText(getContext(), "Não é permitido quantidade negativa", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String posicaoCliente = Long.toString(getItemId(position));

        if (Double.parseDouble(posicaoCliente) % 2 == 0) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    /**
     * @param mapaParcial com id, [preço,quantidade, desconto]
     * @return o valor parcial do pedido
     */
    private Double updateItensLista(Map<Integer, String[]> mapaParcial) {
        totalParcial = 0d;
        Double x = 0d;
        Iterator<Map.Entry<Integer, String[]>> iterator = itensMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String[]> reg = iterator.next();
//            if (!reg.getValue()[1].isEmpty() && reg.getValue()[2] != null) {
//                x = Double.parseDouble(reg.getValue()[2]);
//            }

//            Double desconto = (100 - x) / 100;
            Double precoDesconto = Double.parseDouble(reg.getValue()[0]);

            totalParcial = (totalParcial + precoDesconto * Double.parseDouble(reg.getValue()[1]));
        }
        return totalParcial;
    }

    public Double updateQuantidade(int id, Double quantidade) {
        produtoQuantidade.put(id, quantidade);
        return quantidade;
    }



    public Double updateSubtotal() {
        subTotal = 0d;
        double valorProduto;
        for (Map.Entry<Integer, Double> entrada : produtoQuantidade.entrySet()) {
            int produtoId = entrada.getKey();
            Double quantidade = entrada.getValue();
        }
        return subTotal;
    }

    static class ViewHolder {
        TextView txtPrecoProduto, txtDescricaoProd, tvPosicaoProduto,txtQuantidadeProduto;
        Button btnAdd, btnRem;
        EditText edtPercentualDesconto;
    }


}
