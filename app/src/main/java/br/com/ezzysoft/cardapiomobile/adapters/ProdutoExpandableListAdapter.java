package br.com.ezzysoft.cardapiomobile.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import br.com.ezzysoft.cardapiomobile.R;

/**
 * Created by christian on 13/05/17.
 */
public class ProdutoExpandableListAdapter extends BaseExpandableListAdapter {

    private Map<String, List<String>> dados;
    private List<String> keys;

    public ProdutoExpandableListAdapter(Map<String, List<String>> dados) {
        this.dados = dados;
        this.keys = new ArrayList<String>(dados.keySet());
    }
    @Override
    public Object getGroup(int groupPosition) {
        return keys.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return dados.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition,
                             boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    parent.getContext()).inflate(
                    android.R.layout.simple_expandable_list_item_1,
                    null);
        }
        TextView txt = (TextView)
                convertView.findViewById(android.R.id.text1);
        txt.setTextColor(Color.WHITE);
        txt.setBackgroundColor(Color.GRAY);
        txt.setText(keys.get(groupPosition));
        return convertView;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dados.get(
                keys.get(groupPosition)).get(childPosition);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public View getChildView(int groupPosition,
                             int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    parent.getContext()).inflate(
                    android.R.layout.simple_list_item_1, null);
        }
        TextView txt = (TextView)
                convertView.findViewById(android.R.id.text1);
        txt.setText(dados.get(
                keys.get(groupPosition)).get(childPosition));
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return dados.get(keys.get(groupPosition)).size();
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(
            int groupPosition, int childPosition) {
        return true;
    }
}
