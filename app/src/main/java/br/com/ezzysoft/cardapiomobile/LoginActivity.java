package br.com.ezzysoft.cardapiomobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    Context ctx = LoginActivity.this;

    private EditText mEndereco, mPorta;
    private Spinner mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mEndereco = (EditText) findViewById(R.id.edtEndereco);
//        mPorta = (EditText) findViewById(R.id.edtPorta);
        mLogin = (Spinner) findViewById(R.id.colaboradores);

        ImageButton mEmailSignInButton = (ImageButton) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("ezzy");
    }

    private void attemptLogin() {
        String nomeUsuario;
        nomeUsuario = mLogin.getSelectedItem().toString();
        if (Utilitarios.temConexao(ctx)) {
            if (mLogin.getSelectedItemId() != 0) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("nomeUsuario", nomeUsuario);
                startActivity(intent);
            } else {
                Toast.makeText(ctx, "Informe o usuário!", Toast.LENGTH_SHORT).show();
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
    }
}

