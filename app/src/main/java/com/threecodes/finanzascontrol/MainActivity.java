package com.threecodes.finanzascontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TextView TV_SI, TV_SA, TV_TG, TV_TI;
    private int saldoI=0, saldoA=0, totalG=0, totalI=0;
    private ImageButton BTN_G, BTN_I;
    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        TV_SI = (TextView) findViewById(R.id.textView_mSI);
        TV_SA = (TextView) findViewById(R.id.textView_mSA);
        TV_TG = (TextView) findViewById(R.id.textView_mTG);
        TV_TI = (TextView) findViewById(R.id.textView_mTI);
        //BTN_G = (ImageButton) findViewById(R.id.imgBtn_Gasto);
        //BTN_I = (ImageButton) findViewById(R.id.imgBtn_Ingreso);

        manager = new DBManager(this);

        infoCuenta();


    }

    public void lCuenta(View view)
    {
        Intent intent = new Intent(MainActivity.this, CuentaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.gasto) {
            Intent intent = new Intent(this, GastoActivity.class);
            startActivity(intent);

        } else if (id == R.id.ingreso) {
            Intent intent = new Intent(this, IngresoActivity.class);
            startActivity(intent);

        } else if (id == R.id.hgastos) {

        } else if (id == R.id.hingresos) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Muestra información sobre nuestra cuenta
    public void infoCuenta()
    {
        try
        {
            ArrayList<Datos> saldo = manager.getCuenta();

            int x;
            x = saldo.size();

            saldoI = saldo.get(0).getSALDO();
            saldoA = saldo.get(x-1).getSALDO();

            for(int i = 0; i < x; i++)
            {
                totalG = totalG + saldo.get(i).getGASTO();
                totalI = totalI + saldo.get(i).getINGRESO();
            }

            TV_SI.setText("$." + saldoI);
            TV_SA.setText("$." + saldoA);
            TV_TG.setText("$." + totalG);
            TV_TI.setText("$." + totalI);

        }catch (Exception e) {e.printStackTrace();}
    }



}


