package br.com.ezzysoft.cardapiomobile.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Produto;
import br.com.ezzysoft.cardapiomobile.util.exception.ErroSistema;

/**
 * Created by christian on 03/11/17.
 */

public class ProdutoDAO implements Crud<Produto> {

    EzzyDB mobiDB;

    public ProdutoDAO(Context context) {
        mobiDB = new EzzyDB(context);
    }

    @Override
    public long salvar(Produto entidade) throws ErroSistema {
        return 0;
    }

    @Override
    public void deletar(Produto entidade) throws ErroSistema {

    }

    @Override
    public List<Produto> buscar() throws ErroSistema {
        return null;
    }

    public Produto findById(Integer id) throws ErroSistema {
        SQLiteDatabase db = mobiDB.getReadableDatabase();
        Produto entidade = new Produto();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM produto WHERE id = ?", new String[]{id.toString()});
            if (c != null) {
                if(c.moveToFirst()){
                    entidade.setIdProduto(c.getLong(0));
                    entidade.setDescricao(c.getString(1));
                }
            }
            c.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return entidade;
    }

    public List<Produto> buscarPorGrupo(Context context, String grupo) {
        SQLiteDatabase db = mobiDB.getReadableDatabase();
        List<Produto> lista = new ArrayList<>();
        String[] argumentos = null;
        Produto elem;
        String sql = "SELECT p.*, g.descricao FROM produto p INNER JOIN grupo g ON p.grupo_id=g.id " +
                " WHERE g.descricao LIKE ? ";
        if (grupo.equals("Todos")){
            grupo = "%";
        } else {
            argumentos = new String[]{grupo};
        }
        try {
            Cursor c = db.rawQuery(sql, argumentos);
            while (c.moveToNext()){
                elem = new Produto();
                elem.setIdProduto(Long.parseLong(c.getString(0)));
                elem.setDescricao(c.getString(1));
                elem.setPreco(Double.parseDouble(c.getString(2)));

                lista.add(elem);
            }
            c.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return lista;
    }

    public List<Produto> carregarProdutoDB(Context context) {
        SQLiteDatabase db = mobiDB.getReadableDatabase();
        List<Produto> lista = new ArrayList<>();
        Produto produto;
        try {
            Cursor c;
            c = db.query("produto", null, null, null, null, null, null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        produto = new Produto();

                        produto.setIdProduto(Long.parseLong(c.getString(0)));
                        produto.setDescricao(c.getString(1));
                        produto.setPreco(Double.parseDouble(c.getString(2)));

                        lista.add(produto);
                    } while (c.moveToNext());
                }
            }
            c.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return lista;
    }
}
