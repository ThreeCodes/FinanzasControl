package com.threecodes.finanzascontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by K on 18/02/2017.
 */

public class DBManager {

    public static final String TB_NAME = "tb_cuenta";

    public static final String CN_ID = "_id";
    public static final String CN_SALDO = "saldo";
    public static final String CN_GDES = "gdescripcion";
    public static final String CN_GASTO = "gasto";
    public static final String CN_IDES = "idescripcion";
    public static final String CN_INGRESO = "ingreso";
    public static final String CN_FECHA = "fecha";
    public static final String CN_LOC = "localidad";
    public static final String CN_TEL = "telefono";
    public static final String CN_LAT = "latitud";
    public static final String CN_LON = "longitud";


    public static final String CREATE_TABLE = "create table " + TB_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_SALDO + " integer,"
            + CN_GDES + " text,"
            + CN_GASTO + " integer,"
            + CN_IDES + " text,"
            + CN_INGRESO + " integer,"
            + CN_FECHA + " text,"
            + CN_LOC + " text,"
            + CN_TEL + " text,"
            + CN_LAT + " real,"
            + CN_LON + " real);";

    private DBHelper helper;
    private SQLiteDatabase db;




    public DBManager(Context context) {

        helper = new DBHelper(context);
        db = helper.getWritableDatabase();

    }


    //Contenedor de valores para insertarlos en la base de datos
    public ContentValues GCValues(int saldo, String gdes, int gasto, String ides, int ingreso, String fecha, String loc, String tel, double lat, double lng)
    {
        ContentValues valores = new ContentValues();
        valores.put(CN_SALDO, saldo);
        valores.put(CN_GDES, gdes);
        valores.put(CN_GASTO, gasto);
        valores.put(CN_IDES, ides);
        valores.put(CN_INGRESO, ingreso);
        valores.put(CN_FECHA, fecha);
        valores.put(CN_LOC, loc);
        valores.put(CN_TEL, tel);
        valores.put(CN_LAT, lat);
        valores.put(CN_LON, lng);

        return valores;
    }

    public ContentValues SaldoValues(int saldo)
    {
        ContentValues valor = new ContentValues();
        valor.put(CN_SALDO, saldo);

        return valor;
    }

    public void inSaldoI(int saldo, String fecha)
    {

        db.insert(TB_NAME, null, GCValues(saldo, "",0, "", 0, fecha, "", "", 0, 0));
    }


    public void inGasto(int saldo, String gdes, int gasto, String fecha, String loc, String tel, double lat, double lon)
    {

        db.insert(TB_NAME, null, GCValues(saldo, gdes,gasto, "", 0, fecha, loc, tel, lat, lon));
    }



    public void inIngreso(int saldo, String ides, int ingreso, String fecha)
    {

        db.insert(TB_NAME, null, GCValues(saldo, "",0, ides, ingreso, fecha, "", "", 0, 0));
    }



    public Cursor getTable()
    {
        return db.rawQuery("select * from tb_cuenta", null);
    }




    public void delTable()
    {
        db.execSQL("DELETE FROM " + TB_NAME);
    }




    public void eliminarM(String id)
    {
        db.delete(TB_NAME, CN_ID + "=?", new String[]{id});
    }




    public void actualizarG(String id, String dgasto, int gasto, String fecha, String loc, String tel, double lat, double lng)
    {
        db.update(TB_NAME, GCValues(0, dgasto, gasto, "", 0, fecha, loc, tel, lat, lng), CN_ID + "=?", new String[]{id});
    }

    public void actualizarI(String id, String dingreso, int ingreso, String fecha)
    {
        db.update(TB_NAME, GCValues(0, "", 0, dingreso, ingreso, fecha, "", "", 0, 0), CN_ID + "=?", new String[]{id});
    }



    public void actualizarS(String id, int saldo)
    {
        db.update(TB_NAME, SaldoValues(saldo), CN_ID + "=?", new String[]{id});
    }




    public ArrayList<Datos> getCuenta()
    {
        ArrayList<Datos> saldo = new ArrayList<>();
        String[] columnas = new String[] {CN_ID, CN_SALDO, CN_GDES, CN_GASTO, CN_IDES, CN_INGRESO, CN_FECHA, CN_LOC, CN_TEL, CN_LAT, CN_LON};

        Cursor c = db.query(TB_NAME, columnas, null, null, null, null, null);

        if(c.moveToFirst())
        {
            do {
                Datos a = new Datos();

                a.setID(c.getInt(0));
                a.setSALDO(c.getInt(1));
                a.setGDESC(c.getString(2));
                a.setGASTO(c.getInt(3));
                a.setIDESC(c.getString(4));
                a.setINGRESO(c.getInt(5));
                a.setFECHA(c.getString(6));
                a.setLOC(c.getString(7));
                a.setTEL(c.getString(8));
                a.setLAT(c.getDouble(9));
                a.setLNG(c.getDouble(10));

                saldo.add(a);

            }while(c.moveToNext());
        }
        return saldo;
    }





}
