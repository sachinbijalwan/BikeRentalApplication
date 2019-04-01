package groupfour.software.bikerentalapplication.admin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Random;

import groupfour.software.bikerentalapplication.R;

public class admin_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private boolean cycle=true;
    private void addcycles(ArrayList<Cycle> cycles){
        cycles.clear();
        for(int i=0;i<10;i++){
            cycles.add(new Cycle("Cycle"+i,"Location"+i));
        }
    }
    private void addLocations(ArrayList<Cycle> cycles){
        cycles.clear();
        for(int i=0;i<10;i++){
            cycles.add(new Cycle("Location "+i,"Cycle "+i));
        }
    }
    public void setCycles(){
        //code for binding array list with cycle adapter
        //for more info see https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayList<Cycle> cycles=new ArrayList<Cycle>();
        addcycles(cycles);
        CycleAdapter adapter=new CycleAdapter(this,cycles);
        ListView lview=findViewById(R.id.admin_lv);
        lview.setAdapter(adapter);
    }
    public void setLocations(){
        //to be changed
        //make separate Location class or find some workaround
        ArrayList<Cycle> cycles=new ArrayList<Cycle>();
        addLocations(cycles);
        CycleAdapter adapter=new CycleAdapter(this,cycles);
        ListView lview=findViewById(R.id.admin_lv);
        lview.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(cycle){
            setCycles();
        }
        else{
            setLocations();
        }

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
        getMenuInflater().inflate(R.menu.admin_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cycle) {
            if(!cycle){
                cycle=true;
                setCycles();
            }
            // Handle the camera action
        } else if (id == R.id.nav_location) {
                if(cycle){
                    cycle=false;
                    setLocations();
                }
        } else if (id == R.id.nav_transfer) {

        } else if (id == R.id.nav_complaint) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
