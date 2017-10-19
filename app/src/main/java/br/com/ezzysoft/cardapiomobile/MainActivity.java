package br.com.ezzysoft.cardapiomobile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import br.com.ezzysoft.cardapiomobile.fragments.ListagemPedidoFragment;
import br.com.ezzysoft.cardapiomobile.fragments.PedidoListFragment;
import br.com.ezzysoft.cardapiomobile.fragments.ProdutosListFragment;
import br.com.ezzysoft.cardapiomobile.util.exception.NotificationUtils;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NOTIFICAÇÃO";

    String usuarioLogin = "", endereco = "", porta = "";
    ProdutosListFragment prodListFrag = new ProdutosListFragment();
    ListagemPedidoFragment lpf = new ListagemPedidoFragment();
    PedidoListFragment pedlf = new PedidoListFragment();
    Context ctx = MainActivity.this;
    private TextView infoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = (TextView) findViewById(R.id.infoTextView);

        Bundle extras = getIntent().getExtras();
        usuarioLogin = extras != null ? extras.getString("nomeUsuario") : "";
        endereco = extras != null ? extras.getString("endereco") : "";
        porta = extras != null ? extras.getString("porta") : "";
        prodListFrag.setEndereco(endereco);
        prodListFrag.setPorta(porta);

//        if (usuarioLogin != null &&(usuarioLogin.isEmpty() || !validaLogin(usuarioLogin, endereco, porta))) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                infoTextView.append("\n" + key + ": " + value);
            }
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        infoTextView.append("Token: " + token);
        Log.d(TAG, "Token: " + token);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_login);
        item.setTitle(usuarioLogin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {

            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (Utilitarios.temConexao(ctx)) {
            if (id == R.id.nav_produtos) {

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, prodListFrag)
                        .addToBackStack(null)
                        .commit();

            }
            if (id == R.id.nav_listagem_pedidos) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ListagemPedidoFragment())
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setIcon(R.drawable.no_wifi);
            builder.setTitle("Conexão:");
            builder.setMessage("Sem conexão com o servidor!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create();
            builder.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Boolean validaLogin(String usuarioLogin, String endereco, String porta) {
        return !usuarioLogin.isEmpty() && !endereco.isEmpty() && !porta.isEmpty();
    }

}

