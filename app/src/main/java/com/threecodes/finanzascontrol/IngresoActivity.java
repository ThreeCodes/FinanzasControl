package com.threecodes.finanzascontrol;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IngresoActivity extends AppCompatActivity {

    private EditText ET_DESC, ET_FECHA, ET_CANT;
    private String desc, cant, fecha;
    private DBManager manager;
    private int saldoA = 0, ingreso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new DBManager(this);

        ET_DESC = (EditText) findViewById(R.id.editText_iDesc);
        ET_CANT = (EditText) findViewById(R.id.editText_iCant);
        ET_FECHA = (EditText) findViewById(R.id.editText_iFecha);

        getFecha();

    }

    public void inIngreso(View view)
    {
        desc = ET_DESC.getText().toString();
        cant = ET_CANT.getText().toString();
        fecha = ET_FECHA.getText().toString();

        if(desc.equals("") || cant.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Descripción y Cantidad\nNo pueden quedar vacios", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            getsaldoA();
            ingreso = Integer.parseInt(ET_CANT.getText().toString());
            saldoA = saldoA + ingreso;

            manager.inIngreso(saldoA, desc, ingreso, fecha);

            Toast toast = Toast.makeText(getApplicationContext(), "Ingreso Guardado", Toast.LENGTH_SHORT);
            toast.show();

            clearET();
        }
    }



    public void getsaldoA()
    {
        try
        {
            ArrayList<Datos> saldo = manager.getCuenta();
            int x;
            x = saldo.size();
            saldoA = saldo.get(x-1).getSALDO();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getFecha()
    {
        ET_FECHA = (EditText) findViewById(R.id.editText_iFecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        fecha = sdf.format(new Date());
        ET_FECHA.setText(fecha);
    }



    public void clearET()
    {
        ET_DESC.setText("");
        ET_CANT.setText("");
        getFecha();
    }


    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

}
