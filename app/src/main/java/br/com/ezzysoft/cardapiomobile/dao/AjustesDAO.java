package br.com.ezzysoft.cardapiomobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.ezzysoft.cardapiomobile.bean.Ajustes;
import br.com.ezzysoft.cardapiomobile.util.exception.ErroSistema;

/**
 * Created by christian on 02/11/17.
 */

public class AjustesDAO implements Crud<Ajustes>{

    EzzyDB mobiDB;

    public AjustesDAO(Context context) {
        mobiDB = new EzzyDB(context);
    }

    @Override
    public long salvar(Ajustes entidade) throws ErroSistema {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        long id;

        try {
            ContentValues values1 = new ContentValues();

            values1.put("ip", entidade.getIp());
            values1.put("porta", entidade.getPorta());

            id = db.insert("ajustes", "", values1);

            return id;

        } catch (Exception e) {
            e.printStackTrace();
            return 0l;
        } finally {
            db.close();
        }
    }

    @Override
    public void deletar(Ajustes entidade) throws ErroSistema {

    }

    @Override
    public List<Ajustes> buscar() throws ErroSistema {
        return null;
    }

    public Ajustes busca() {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        Ajustes entidade = new Ajustes();

        Long idAjustes = 1l;

        try {
            Cursor ca = db.rawQuery("SELECT * FROM ajustes WHERE id = ?", new String[]{idAjustes.toString()});
            if (ca != null) {
                if (ca.moveToFirst()) {
                    entidade.setId(ca.getLong(0));
                    entidade.setIp(ca.getString(1));
                    entidade.setPorta(ca.getString(2));
                }
            }
            ca.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
        return entidade;
    }

    public long modifica(Ajustes entidade) throws ErroSistema {
        SQLiteDatabase db = mobiDB.getWritableDatabase();
        Long id = 1l;
        try {
            ContentValues values = new ContentValues();
            values.put("ip", entidade.getIp());
            values.put("porta", entidade.getPorta());
            db.update("ajustes", values, "id=?", new String[]{String.valueOf(id)});
            return id;
        } finally {
            db.close();
        }
    }

    public long count() throws ErroSistema {
        Long count = 0l;

        SQLiteDatabase db = mobiDB.getWritableDatabase();

        try {
            Cursor c = db.rawQuery("select count(id) from ajustes", null);
            c.moveToFirst();

            if (c != null) {
                count = c.getLong(0);
            }
            c.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0l;
        } finally {
            db.close();
        }
        return count;
    }
}
