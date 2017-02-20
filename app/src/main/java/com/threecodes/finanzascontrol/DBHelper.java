package com.threecodes.finanzascontrol;

import android.content.Context;
import android.database.sqlite.*;

/**
 * Created by K on 18/02/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final  String DB_NAME = "cuenta.sqlite";
    private static final  int DBS_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DBS_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBManager.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}




