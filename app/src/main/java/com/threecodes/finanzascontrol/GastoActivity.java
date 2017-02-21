package com.threecodes.finanzascontrol;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GastoActivity extends AppCompatActivity {


    private String desc, cant, fecha, loc, tel;
    private int gasto = 0, saldoA = 0;
    private double lat = 0, lng = 0;
    private EditText ET_DESC, ET_CANT, ET_FECHA, ET_TEL, ET_LOC;
    private TextView TV_UB;
    private GPS_Service servicio;

    private DBManager manager;
    private int not = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ET_DESC = (EditText) findViewById(R.id.editText_gDesc);
        ET_CANT = (EditText) findViewById(R.id.editText_gCant);
        ET_FECHA = (EditText) findViewById(R.id.editText_gFecha);
        ET_LOC = (EditText) findViewById(R.id.editText_gLoc);
        ET_TEL = (EditText) findViewById(R.id.editText_gTel);
        TV_UB = (TextView) findViewById(R.id.textView_gUB);


        manager = new DBManager(this);
        getFecha();

    }

    //Si se cumplen las condiciones se registra el gasto en la base de datos
    public void inGasto(View view) {
        cant = ET_CANT.getText().toString();
        desc = ET_DESC.getText().toString();
        fecha = ET_FECHA.getText().toString();
        loc = ET_LOC.getText().toString();
        tel = ET_TEL.getText().toString();


        if (desc.equals("") || cant.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Descripción y Cantidad\nNo pueden quedar vacios", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            gasto = Integer.parseInt(ET_CANT.getText().toString());
            getsaldoA();
            saldoA = saldoA - gasto;
            manager.inGasto(saldoA, desc, gasto, fecha, loc, tel, lat, lng);
            Toast toast = Toast.makeText(getApplicationContext(), "Gasto Guardado", Toast.LENGTH_SHORT);
            toast.show();
            clearET();

            if (saldoA < 50) {
                showNot();
            }
        }
    }


    //Devuelve el saldo actual
    public void getsaldoA() {
        try {
            ArrayList<Datos> saldo = manager.getCuenta();
            int x;
            x = saldo.size();
            saldoA = saldo.get(x - 1).getSALDO();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Agrega la fecha del sistema automáticamente al editText
    public void getFecha() {
        ET_FECHA = (EditText) findViewById(R.id.editText_gFecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        fecha = sdf.format(new Date());
        ET_FECHA.setText(fecha);
    }


    //Limpia los editText para realizar otro gasto
    public void clearET() {
        ET_DESC.setText("");
        ET_CANT.setText("");
        getFecha();
        ET_LOC.setText("");
        ET_TEL.setText("");
    }


    //Obtiene las coordenadas de GPS_Servicio
    public void setUB(View view) {

        Permisos();

        servicio = new GPS_Service(getApplicationContext());

        try {
            lat = servicio.location.getLatitude();
            lng = servicio.location.getLongitude();
        } catch (Exception e) {
        }

        TV_UB.setText("Coordenadas: " + lat + " " + lng);

        if (lat == 0.0 && lng == 0.0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ubicación no disponible", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    //Muestra una notificación si el saldo resultante es menor a 50
    public void showNot() {
        try {
            long[] a = {0, 200, 400, 200, 50, 200, 50, 200, 50, 200, 400, 200, 50, 200};
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Información del Saldo");
            mBuilder.setContentText("¡Precaución! Su saldo es menor a $.50");
            //mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            mBuilder.setVibrate(a);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            //mBuilder.setVibrate(long[] );

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(not, mBuilder.build());
        } catch (Exception e) {
        }
        ;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //static final String pp = "ACCESS_FINE_LOCATION";

    @AfterPermissionGranted(1)
    private void Permisos() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.app_name),
                    1, perms);
        }
    }


    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
    }

}
