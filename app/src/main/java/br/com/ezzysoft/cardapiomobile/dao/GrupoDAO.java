package br.com.ezzysoft.cardapiomobile.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian on 03/11/17.
 */

public class GrupoDAO {

    EzzyDB mobiDB;

    public GrupoDAO(Context context) {
        mobiDB = new EzzyDB(context);
    }

    public List<String> getAllLabels(Context context){
        SQLiteDatabase db = mobiDB.getReadableDatabase();
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM grupo";

        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (i == 0) {
                    labels.add("Todos");
                }
                labels.add(cursor.getString(1));
                i++;

            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
}
