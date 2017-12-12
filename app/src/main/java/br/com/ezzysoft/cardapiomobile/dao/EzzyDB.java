package br.com.ezzysoft.cardapiomobile.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian on 02/11/17.
 */

public class EzzyDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "ezzydb.sqlite";
    public static final int VERSAO = 1;
    private static final String CREATE_IF_NOT = "create table if not exists";
    private static final String INSERT = "INSERT INTO";
    private static final String COLUMN_ID = "id integer primary key autoincrement";


    private Context context;

    public EzzyDB(Context context) {
        super(context, DB_NAME, null, VERSAO);
        this.context = context;
    }

    public EzzyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TAG = "EzzyDB";
        try {

            try {
                Log.i(TAG, " - Criando a tabela Ajustes ");

                db.execSQL(CREATE_IF_NOT + " ajustes(" + COLUMN_ID + ", ip text, porta text)");

            } catch (Exception e) {
                Log.e(TAG, " - Erro ao criar a tabela Ajustes: ", e.getCause());
                Log.e(TAG, " - Erro ao criar a tabela Ajustes: ", e.fillInStackTrace());
            }

            try {
                db.execSQL(INSERT + " ajustes VALUES(1, '0.0.0.0', '8080');");

            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + INSERT + " ajustes VALUES(1, '0.0.0.0', '8080');", e);
            }
            try {
                // Cliente
                db.execSQL(CREATE_IF_NOT + " cliente(" + COLUMN_ID + ", nome text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: cliente", e);
            }
            try {
                // Colaborador
                db.execSQL(CREATE_IF_NOT + " colaborador(" + COLUMN_ID + ", nome text, usuario_id integer," +
                        "foreign key (usuario_id) references usuario(id))");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: colaborador", e);
            }
            try {
                // Grupo
                db.execSQL(CREATE_IF_NOT + " grupo(" + COLUMN_ID + ", descricao text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: grupo", e);
            }
            try {
                // Marca
                db.execSQL(CREATE_IF_NOT + " marca(" + COLUMN_ID + ", descricao text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: marca", e);
            }
            try {
                // Produto
                db.execSQL(CREATE_IF_NOT + " produto(" + COLUMN_ID + ", descricao text, preco_venda real, composto integer," +
                        " grupo_id integer, marca_id integer, unidade_id integer," +
                        "foreign key (grupo_id) references grupo(id)," +
                        "foreign key (marca_id) references marca(id)," +
                        "foreign key (unidade_id) references unidade(id))");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: produto", e);
            }
            try {
                // Status
                db.execSQL(CREATE_IF_NOT + " status(" + COLUMN_ID + ", classe text, indice integer, opcao text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: status", e);
            }
            try {
                // Unidade
                db.execSQL(CREATE_IF_NOT + " unidade(" + COLUMN_ID + ", descricao text, sigla text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: unidade", e);
            }
            try {
                // Usuário
                db.execSQL(CREATE_IF_NOT + " usuario(" + COLUMN_ID + ", usuario text, senha text)");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: usuario", e);
            }
            try {
                // Pedido
                db.execSQL(CREATE_IF_NOT + " pedido(" + COLUMN_ID + ", total_pedido real,total_produtos real," +
                        "total_servico real, forma_pgto integer, descricao text, data_pedido numeric," +
                        " hora_pedido numeric, mesa numeric, cliente_id integer, colaborador_id integer, status_id integer," +
                        "foreign key (cliente_id) references cliente(id)," +
                        "foreign key (colaborador_id) references colaborador(id)," +
                        "foreign key (status_id) references status(id)," +
                        "foreign key (forma_pgto) references status(id))");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: pedido", e);
            }
            try {
                // Itens do Pedido
                db.execSQL(CREATE_IF_NOT + " itenspedido(" + COLUMN_ID + ", pedido_id integer, produto_id integer," +
                        " quantidade real, valor_item real, valor_unit real," +
                        "foreign key (pedido_id) references pedido(id)," +
                        "foreign key (produto_id) references produto(id))");
            } catch (Exception e) {
                Log.e(TAG, " - Erro ao executar: " + CREATE_IF_NOT + " tabela: itenspedido", e);
            }

        } catch (Exception e) {
            Log.e(TAG, " - Erro ao executar 'onCreate' SQLiteDatabase.", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String TAG = "EzzyDB";

        Log.w(TAG, "711585 - Upgrading database from version " + oldVersion + " to " + newVersion);
        if (newVersion > oldVersion){

        }else {
            Log.w(TAG, " Não é possível fazer o downgrade.");
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
