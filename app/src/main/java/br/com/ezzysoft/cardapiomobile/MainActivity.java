package br.com.ezzysoft.cardapiomobile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import br.com.ezzysoft.cardapiomobile.fragments.ConfiguracaoFragment;
import br.com.ezzysoft.cardapiomobile.fragments.ListagemPedidoFragment;
import br.com.ezzysoft.cardapiomobile.fragments.PedidoListFragment;
import br.com.ezzysoft.cardapiomobile.fragments.ProdutosListFragment;
import br.com.ezzysoft.cardapiomobile.fragments.SyncFragment;
import br.com.ezzysoft.cardapiomobile.util.exception.Contents;
import br.com.ezzysoft.cardapiomobile.util.exception.QRCodeEncoder;
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
    ImageView myImage;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage = (ImageView) findViewById(R.id.imageView1);

        Bundle extras = getIntent().getExtras();
        usuarioLogin = extras != null ? extras.getString("nomeUsuario") : "";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        if (extras.getString("txtnotificacao") != null) {
            /*for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                infoTextView.append("\n" + key + ": " + value);
            }*/


            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View prompView = layoutInflater.inflate(R.layout.dialog_notificacao, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(prompView);

            infoTextView = (TextView) prompView.findViewById(R.id.infoNotificacao);
            infoTextView.setText(extras.getString("txtnotificacao"));


            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        //infoTextView.append("Token: " + token);
        System.out.println("Token Id" + token);
        Log.d(TAG, "Token: " + token);

        geraQRCardapio();

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
        FragmentManager fm = getFragmentManager();
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                fm.beginTransaction()
                        .replace(R.id.content_frame, new ConfiguracaoFragment())
                        .addToBackStack(null)
                        .commit();
                myImage.setVisibility(View.INVISIBLE);
                return true;
            case R.id.action_logout:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        prefs = getSharedPreferences("configuracoes", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String nome = prefs.getString("usuario", null);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (Utilitarios.temConexao(ctx)) {
            if (id == R.id.nav_produtos) {

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ProdutosListFragment())
                        .addToBackStack(null)
                        .commit();

            }
            if (id == R.id.nav_listagem_pedidos) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ListagemPedidoFragment())
                        .addToBackStack(null)
                        .commit();
            }
            if (id == R.id.nav_manage) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new SyncFragment())
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
        myImage.setVisibility(View.INVISIBLE);
        return true;
    }

    public Boolean validaLogin(String usuarioLogin, String endereco, String porta) {
        return !usuarioLogin.isEmpty() && !endereco.isEmpty() && !porta.isEmpty();
    }

    public void geraQRCardapio(){
        String qrInputText = "http://ezzysoft.ddns.net:8181/restaurante/cardapio/mobi.jsf";

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

}

