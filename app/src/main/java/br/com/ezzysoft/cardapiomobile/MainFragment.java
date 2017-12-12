package br.com.ezzysoft.cardapiomobile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import br.com.ezzysoft.cardapiomobile.fragments.ListagemPedidoFragment;
import br.com.ezzysoft.cardapiomobile.fragments.PedidoListFragment;
import br.com.ezzysoft.cardapiomobile.fragments.ProdutosListFragment;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

public class MainFragment extends Fragment {

    private static final String TAG = "NOTIFICAÇÃO";

    ProdutosListFragment prodListFrag = new ProdutosListFragment();
    ListagemPedidoFragment lpf = new ListagemPedidoFragment();
    PedidoListFragment pedlf = new PedidoListFragment();
    private TextView infoTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        infoTextView = (TextView) layout.findViewById(R.id.infoTextView);

//        if (Bundle.getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                String value = getIntent().getExtras().getString(key);
//                infoTextView.append("\n" + key + ": " + value);
//            }
//        }

        String token = FirebaseInstanceId.getInstance().getToken();
        infoTextView.append("Token: " + token);
        System.out.println("Token Id"+token);
        Log.d(TAG, "Token: " + token);

        return layout;
    }
}

