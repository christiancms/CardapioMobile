package br.com.ezzysoft.cardapiomobile.dao;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.R;
import br.com.ezzysoft.cardapiomobile.bean.Ajustes;
import br.com.ezzysoft.cardapiomobile.bean.Cliente;
import br.com.ezzysoft.cardapiomobile.bean.Colaborador;
import br.com.ezzysoft.cardapiomobile.bean.Grupo;
import br.com.ezzysoft.cardapiomobile.bean.Marca;
import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.bean.Status;
import br.com.ezzysoft.cardapiomobile.bean.Unidade;
import br.com.ezzysoft.cardapiomobile.bean.Usuario;
import br.com.ezzysoft.cardapiomobile.util.exception.ErroSistema;
import br.com.ezzysoft.cardapiomobile.util.exception.Utilitarios;
import br.com.ezzysoft.cardapiomobile.ws.ClienteHttp;
import br.com.ezzysoft.cardapiomobile.ws.ColaboradorHttp;
import br.com.ezzysoft.cardapiomobile.ws.GrupoHttp;
import br.com.ezzysoft.cardapiomobile.ws.MarcaHttp;
import br.com.ezzysoft.cardapiomobile.ws.ProdutoHttp;
import br.com.ezzysoft.cardapiomobile.ws.StatusHttp;
import br.com.ezzysoft.cardapiomobile.ws.UnidadeHttp;
import br.com.ezzysoft.cardapiomobile.ws.UsuarioHttp;

/**
 * Created by christian on 02/11/17.
 */

public class Sync extends Application {

    ClienteTask mTaskCliente; // 1
    UsuarioTask mTaskUsuario; // 2
    ColaboradorTask mTaskColaborador; // 3
    GrupoTask mTaskGrupo; // 4
    MarcaTask mTaskMarca; // 5
    UnidadeTask mTaskUnidade; // 6
    StatusTask mTaskStatus; // 7
    ProdutoTask mTaskProduto; // 8

    ArrayList<Cliente> mClientes;
    ArrayList<Usuario> mUsuarios;
    ArrayList<Colaborador> mColaboradores;
    ArrayList<Grupo> mGrupos;
    ArrayList<Marca> mMarcas;
    ArrayList<Unidade> mUnidades;
    ArrayList<Status> mStatus;
    ArrayList<Produto> mProdutos;

    AjustesDAO ajustesDAO;

    long id = 0l;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    ContentValues values;
    String[] columns = {"id"};
    String var;
    String senhaMD5;
    Usuario usuarios = new Usuario();
    private Context context;
    private EzzyDB mobiDB;

    public Sync(Context ctx) throws IOException, ErroSistema {
        Toast.makeText(ctx, "Aguarde Iniciando a sincronização", Toast.LENGTH_SHORT).show();

        mobiDB = new EzzyDB(ctx);
        context = ctx;
        Ajustes ajustes;
        ajustesDAO = new AjustesDAO(ctx);
        ajustes = ajustesDAO.busca();
        var = ajustes.getIp() + ":" + ajustes.getPorta();

        testa();
        setContext(ctx);
    }

    public void testa() {
        new Sync.TestTask().execute();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void iniciarDownloadProduto() {
        if (mProdutos == null) {
            mProdutos = new ArrayList<>();
        }
        if (mTaskProduto == null || mTaskProduto.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskProduto = new ProdutoTask();
            mTaskProduto.execute();
        }
    }

    public void iniciarDownloadGrupo(){
        if (mGrupos == null){
            mGrupos = new ArrayList<>();
        }
        if (mTaskGrupo == null || mTaskGrupo.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskGrupo = new GrupoTask();
            mTaskGrupo.execute();
        }
    }
    public void iniciarDownloadMarca(){
        if (mMarcas == null){
            mMarcas = new ArrayList<>();
        }
        if (mTaskMarca == null || mTaskMarca.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskMarca = new MarcaTask();
            mTaskMarca.execute();
        }
    }
    public void iniciarDownloadStatus(){

        if (mStatus == null){
            mStatus = new ArrayList<>();
        }
        if (mTaskStatus == null || mTaskStatus.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskStatus = new StatusTask();
            mTaskStatus.execute();
        }

    }
    public void iniciarDownloadUnidade(){

        if (mUnidades == null){
            mUnidades = new ArrayList<>();
        }
        if (mTaskUnidade == null || mTaskUnidade.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskUnidade = new UnidadeTask();
            mTaskUnidade.execute();
        }

    }


    private long inserirProduto(List<Produto> listaProduto) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();

            for (Produto produto : listaProduto) {

                values.put("id", produto.getIdProduto());
                values.put("descricao", produto.getDescricao());
                values.put("preco_venda", (produto.getPreco() != null ? produto.getPreco() : 0d));
                values.put("composto", produto.isComposto() ? 1 : 0);
                values.put("grupo_id",produto.getGrupoId());
                values.put("marca_id",produto.getMarcaId());
                values.put("unidade_id",produto.getUnidadeId());


                String whereClause = "id=" + produto.getIdProduto();
                Cursor c = db.query("produto", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("produto", values, "id=?", new String[]{String.valueOf(produto.getIdProduto())});
                    j++;
                } else {
                    id = db.insert("produto", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Produtos inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Produtos Atualizado!");
            Toast.makeText(getContext(), "Concluído", Toast.LENGTH_LONG).show();
        }
    }

    private long inserirUsuario(List<Usuario> listaUsuario) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            for (Usuario usuario : listaUsuario) {
                String senha = "123456";
                senhaMD5 = usuarios.criptografaSenha(senha);

                values = new ContentValues();
                values.put("id", usuario.get_id());
                values.put("usuario", usuario.getLogin());
                values.put("senha", senhaMD5);

                String whereClause = "id=" + usuario.get_id();
                Cursor c = db.query("usuario", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("usuario", values, "id=?", new String[]{String.valueOf(usuario.get_id())});
                    j++;
                } else {

                    id = db.insert("usuario", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Usuário inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            db.close();
            System.out.println("Sync: Usuário Atualizado!");
            iniciarDownloadColaboradores();
        }
        return id;
    }

    private void iniciarDownloadColaboradores() {
        if (mColaboradores == null) {
            mColaboradores = new ArrayList<>();
        }
        if (mTaskColaborador == null || mTaskColaborador.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskColaborador = new ColaboradorTask();
            mTaskColaborador.execute();
        }
    }

    public class TestTask extends AsyncTask<String, Context, String> {
        String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String ip = "";

            Ajustes ajustes;
            ajustesDAO = new AjustesDAO(context);
            try {
                ajustes = ajustesDAO.busca();
                ip = ajustes.getIp() + ":" + ajustes.getPorta();

                HttpURLConnection conexao = Utilitarios.connectar("http://" + ip + Utilitarios.APPURL+"teste/");
                int resposta = conexao.getResponseCode();
                if (resposta == HttpURLConnection.HTTP_OK) {
                    status = "Conectado";
                } else {
                    status = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            if (status.isEmpty() || status == null) {
                Toast.makeText(getContext(), "Sem Conexão, verifique seu sinal de internet", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Conectado ao Servidor", Toast.LENGTH_SHORT).show();
                iniciarDownloadClientes();
            }
        }
    }

    public void iniciarDownloadClientes() {
        if (mClientes == null) {
            mClientes = new ArrayList<>();
        }
        if (mTaskCliente == null || mTaskCliente.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskCliente = new ClienteTask();
            mTaskCliente.execute();
        }

    }

    class ClienteTask extends AsyncTask<Void, Context, List<Cliente>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Cliente> doInBackground(Void... strings) {

            return ClienteHttp.carregarClientesJson(var);
        }

        @Override
        protected void onPostExecute(List<Cliente> clientes) {
            super.onPostExecute(clientes);

            if (clientes != null) {
                mClientes.clear();
                mClientes.addAll(clientes);

                inserirCliente(mClientes);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Cliente!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }
        }
    }

    private long inserirCliente(List<Cliente> listaCliente) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            for (Cliente cliente : listaCliente) {

                values = new ContentValues();
                values.put("id", cliente.get_id());
                values.put("nome", cliente.getNome());

                String whereClause = "id=" + cliente.get_id();
                Cursor c = db.query("cliente", columns, whereClause, null, null, null, null);

                if (c.getCount() > 0) {
                    id = db.update("cliente", values, "id=?", new String[]{String.valueOf(cliente.get_id())});
                    j++;
                } else {
                    id = db.insert("cliente", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Cliente inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Cliente Atualizado!");
            iniciarDownloadUsuarios();
        }
    }

    public void iniciarDownloadUsuarios() {
        if (mUsuarios == null) {
            mUsuarios = new ArrayList<>();
        }
        if (mTaskUsuario == null || mTaskUsuario.getStatus() != AsyncTask.Status.RUNNING) {
            mTaskUsuario = new UsuarioTask();
            mTaskUsuario.execute();
        }

    }

    class UsuarioTask extends AsyncTask<Void, Void, List<Usuario>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Usuario> doInBackground(Void... strings) {
            return UsuarioHttp.carregarUsuariosJson(var);
        }

        @Override
        protected void onPostExecute(List<Usuario> usuarios) {
            super.onPostExecute(usuarios);

            if (usuarios != null) {
                mUsuarios.clear();
                mUsuarios.addAll(usuarios);

                inserirUsuario(mUsuarios);

            } else {
                //iniciarDownloadProduto();
                //iniciarDownloadColaboradores();  VERIFICAR

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Usuário!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }
        }
    }

    class ColaboradorTask extends AsyncTask<Void, Context, List<Colaborador>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Colaborador> doInBackground(Void... strings) {
            return ColaboradorHttp.carregarColaboradoresJson(var);
        }

        @Override
        protected void onPostExecute(List<Colaborador> colaboradores) {
            super.onPostExecute(colaboradores);

            if (colaboradores != null) {
                mColaboradores.clear();
                mColaboradores.addAll(colaboradores);

                inserirColaborador(mColaboradores);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Colaborador!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }

        }
    }

    private long inserirColaborador(List<Colaborador> listaColaborador) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();

            for (Colaborador colaborador:listaColaborador) {
                values.put("id", colaborador.get_id());
                values.put("nome", colaborador.getNome());
                values.put("usuario_id", colaborador.getUsuario_id());

                String whereClause = "id=" + colaborador.get_id();
                Cursor c = db.query("colaborador", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("colaborador", values, "id=?", new String[]{String.valueOf(colaborador.get_id())});
                    j++;
                } else {
                    id = db.insert("colaborador", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Colaboradores inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Colaboradores Atualizado!");
            iniciarDownloadGrupo();
        }
    }

    class GrupoTask extends AsyncTask<Void, Context, List<Grupo>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Grupo> doInBackground(Void... params) {
            return GrupoHttp.carregarGruposJson(var);
        }

        @Override
        protected void onPostExecute(List<Grupo> grupos) {
            super.onPostExecute(grupos);

            if (grupos != null) {
                mGrupos.clear();
                mGrupos.addAll(grupos);

                inserirGrupo(mGrupos);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Grupo!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }
        }
    }

    private long inserirGrupo(List<Grupo> listaGrupo) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();

            for (Grupo grupo:listaGrupo) {
                values.put("id", grupo.get_id());
                values.put("descricao", grupo.getDescricao());

                String whereClause = "id=" + grupo.get_id();
                Cursor c = db.query("grupo", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("grupo", values, "id=?", new String[]{String.valueOf(grupo.get_id())});
                    j++;
                } else {
                    id = db.insert("grupo", "", values);
                    i++;
                }
                c.close();

            }
            Toast.makeText(getContext(), "Grupos inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Grupos Atualizado!");
            iniciarDownloadMarca();
        }
    }

    class MarcaTask extends AsyncTask<Void, Context, List<Marca>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Marca> doInBackground(Void... params) {
            return MarcaHttp.carregarMarcasJson(var);
        }

        @Override
        protected void onPostExecute(List<Marca> marcas) {
            super.onPostExecute(marcas);

            if (marcas != null) {
                mMarcas.clear();
                mMarcas.addAll(marcas);

                inserirMarca(mMarcas);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Marca!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }
        }
    }

    private long inserirMarca(List<Marca> listaMarca) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();
            for (Marca marca : listaMarca) {
                values.put("id", marca.get_id());
                values.put("descricao", marca.getDescricao());

                String whereClause = "id=" + marca.get_id();
                Cursor c = db.query("marca", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("marca", values, "id=?", new String[]{String.valueOf(marca.get_id())});
                    j++;
                } else {
                    id = db.insert("marca", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Marcas inseridas: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Marcas Atualizado!");
            iniciarDownloadUnidade();
        }
    }

    class StatusTask extends AsyncTask<Void, Context, List<Status>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<br.com.ezzysoft.cardapiomobile.bean.Status> doInBackground(Void... params) {
            return StatusHttp.carregarStatusJson(var);
        }

        @Override
        protected void onPostExecute(List<br.com.ezzysoft.cardapiomobile.bean.Status> statuss) {
            super.onPostExecute(statuss);

            if (statuss != null) {
                mStatus.clear();
                mStatus.addAll(statuss);

                inserirStatus(mStatus);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Status!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }

        }
    }

    private long inserirStatus(List<Status> listaStatus) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();
            for (Status status:listaStatus) {
                values.put("id", status.get_id());
                values.put("indice", status.getIndice());
                values.put("classe", status.getClasse());
                values.put("opcao", status.getOpcao());

                String whereClause = "id=" + status.get_id();
                Cursor c = db.query("status", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("status", values, "id=?", new String[]{String.valueOf(status.get_id())});
                    j++;
                } else {
                    id = db.insert("status", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Status inseridos: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Status Atualizado!");
            iniciarDownloadProduto();
        }
    }

    class UnidadeTask extends AsyncTask<Void, Context, List<Unidade>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Unidade> doInBackground(Void... params) {
            return UnidadeHttp.carregarUnidadesJson(var);
        }

        @Override
        protected void onPostExecute(List<Unidade> unidades) {
            super.onPostExecute(unidades);

            if(unidades != null){
                mUnidades.clear();
                mUnidades.addAll(unidades);

                inserirUnidade(mUnidades);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Unidade!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();
            }
        }
    }

    private long inserirUnidade(List<Unidade> listaUnidade) {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        int i = 0, j = 0;

        try {
            values = new ContentValues();

            for (Unidade unidade: listaUnidade) {
                values.put("id", unidade.get_id());
                values.put("sigla", unidade.getSigla());
                values.put("descricao", unidade.getDescricao());

                String whereClause = "id=" + unidade.get_id();
                Cursor c = db.query("unidade", columns, whereClause, null, null, null, null);
                if (c.getCount() > 0) {
                    id = db.update("unidade", values, "id=?", new String[]{String.valueOf(unidade.get_id())});
                    j++;
                } else {
                    id = db.insert("unidade", "", values);
                    i++;
                }
                c.close();
            }
            Toast.makeText(getContext(), "Unidades inseridas: " + i + " \n e atualizados: " + j, Toast.LENGTH_SHORT).show();
            return id;
        } finally {
            db.close();
            System.out.println("Sync: Unidades Atualizado!");
            iniciarDownloadStatus();
        }
    }

    class ProdutoTask extends AsyncTask<Void, Void, List<Produto>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Produto> doInBackground(Void... strings) {
            return ProdutoHttp.carregarProdutosJson(var);
        }

        @Override
        protected void onPostExecute(List<Produto> produtos) {
            super.onPostExecute(produtos);

            if (produtos != null) {
                mProdutos.clear();
                mProdutos.addAll(produtos);

                inserirProduto(mProdutos);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.caution);
                builder.setTitle("Erro:");
                builder.setMessage("Sem Produto!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });
                builder.create();
                builder.show();

                // Iniciar Download da próxima Entidade.
                // Fim
            }
        }
    }
}
