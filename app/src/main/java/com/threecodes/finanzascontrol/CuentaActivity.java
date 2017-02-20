package com.threecodes.finanzascontrol;

import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class CuentaActivity extends AppCompatActivity {


    private Button BTN_GC, BTN_EC;
    private EditText ET_SA;
    private TextView TV_SALDO, TV_FECHA;
    private int SI = 0;
    private String fecha, ff;
    private Cursor c;
    private DBManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);


        manager = new DBManager(this);
        getInfo();

    }

    //Agrega los datos ingresados a la base de datos SQLite
    public void GuardarCuenta(View view)
    {
        try
        {
            ET_SA = (EditText) findViewById(R.id.editTextSaldoInicial);
            BTN_GC = (Button) findViewById(R.id.btnGuardarC);
            BTN_EC = (Button) findViewById(R.id.btnEliminarC);
            getFecha();

            manager.inSaldoI(Integer.parseInt(ET_SA.getText().toString()), fecha);

            Toast toast = Toast.makeText(getApplicationContext(), "Cuenta Creada", Toast.LENGTH_SHORT);
            toast.show();

            getInfo();

        }catch (Exception e) {e.printStackTrace();}
    }


    //Elimina la cuenta y todos los registros de la base de datos
    public void EliminarCuenta(View view)
    {
        try {
            manager.delTable();
            Toast toast = Toast.makeText(getApplicationContext(), "Cuenta Eliminada", Toast.LENGTH_LONG);
            toast.show();
            getInfo();
        }catch (Exception e){e.printStackTrace();}
    }


    //Muestra la información de la cuenta y habilita los botones
    public void getInfo()
    {
        try
        {
            SI = 0;
            TV_SALDO = (TextView) findViewById(R.id.textViewSI);
            TV_FECHA = (TextView) findViewById(R.id.textViewFecha);
            BTN_GC = (Button) findViewById(R.id.btnGuardarC);
            BTN_EC = (Button) findViewById(R.id.btnEliminarC);
            ArrayList<Datos> saldo = manager.getCuenta();


            SI = saldo.get(0).getSALDO();
            ff = saldo.get(0).getFECHA();

        }catch (Exception e) {e.printStackTrace();}

        if(SI != 0)
        {
            TV_SALDO.setText("Saldo Inicial: $." + Integer.toString(SI));
            TV_FECHA.setText("Fecha: " + ff);
            BTN_EC.setEnabled(true);
            BTN_GC.setEnabled(false);
        }
        else
        {
            TV_SALDO.setText("Saldo Inicial: ");
            TV_FECHA.setText("Fecha: ");
            BTN_GC.setEnabled(true);
            BTN_EC.setEnabled(false);
        }

    }


    //Devuelve la fecha del sistema en formato ("dd/MM/yyyy - HH:mm")
    public void getFecha()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        fecha = sdf.format(new Date());
    }


    //Crea un archivo CSV con todos los registros de la base de datos
    public void gCSV(View view)
    {
        try {
            c = manager.getTable();

            int rowcount = 0;
            int colcount = 0;

            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "Mi Cuenta - Backup.csv";

            // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);
            BufferedWriter bw = new BufferedWriter(fw);

            rowcount = c.getCount();
            colcount = c.getColumnCount();

            if (rowcount > 0)
            {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++)
                {
                    if (i != colcount - 1)
                    {
                        bw.write(c.getColumnName(i) + ",");
                    }
                    else
                    {
                        bw.write(c.getColumnName(i));
                    }
                }

                bw.newLine();
                for (int i = 0; i < rowcount; i++)
                {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++)
                    {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
            }

            Toast toast = Toast.makeText(getApplicationContext(), "Datos Guardados\nSDCard/ Mi Cuenta - Backup.csv", Toast.LENGTH_LONG);
            toast.show();

        } catch (Exception e) {};
    }



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/


}
