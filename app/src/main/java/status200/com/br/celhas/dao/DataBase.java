package status200.com.br.celhas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joao on 01/07/17.
 */

public class DataBase  extends SQLiteOpenHelper {

    public DataBase(Context context)
    {
        super(context, "AGENDA", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( ScriptSQL.getCreateContato() );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
