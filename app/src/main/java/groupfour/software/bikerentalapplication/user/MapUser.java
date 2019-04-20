package groupfour.software.bikerentalapplication.user;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Field;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.login.LoginActivity;

public class MapUser extends FragmentActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean addMarker = false ;

    private int activity=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        onCreateDrawer();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    LatLng latlng = new LatLng(latitude, longitude);
                    if (!addMarker){
                        mMap.addMarker(new MarkerOptions().position(latlng));
                        addMarker = true ;
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.2f));

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    LatLng latlng = new LatLng(latitude, longitude);
                    if (!addMarker){
                        mMap.addMarker(new MarkerOptions().position(latlng));
                        addMarker = true ;
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.2f));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }


        //b.onCreateDrawer();


  //      BaseActivity b = new BaseActivity();
       // b.onCreate(savedInstanceState);
//        b.onCreateDrawer();
    }

    protected void onCreateDrawer(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //ImageButton img=getNavButtonView(toolbar);

                float slideX = drawerView.getWidth() * slideOffset;
                getNavButtonView(toolbar).setZ(20);
                getNavButtonView(toolbar).setTranslationX(slideX);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(this);
        Drawable icon= getDrawable(R.drawable.ic_motorcycle_black_24dp);
        getNavButtonView(toolbar).setImageDrawable(icon);
        getNavButtonView(toolbar).setColorFilter(getResources().getColor(android.R.color.white));

    }
    private ImageButton getNavButtonView(Toolbar toolbar) {
        try {
            Class<?> toolbarClass = Toolbar.class;
            Field navButtonField = toolbarClass.getDeclaredField("mNavButtonView");
            navButtonField.setAccessible(true);
            ImageButton navButtonView = (ImageButton) navButtonField.get(toolbar);

            return navButtonView;
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<LocationModel>() {
//                    @Override
//                    public void onSuccess(LocationModel location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            double latitude = location.getLatitude();
//                            double longitude = location.getLongitude() ;
//                            LatLng latLng = new LatLng(latitude, longitude);
//                            mMap.addMarker(new MarkerOptions().position(latLng));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
//                        }
//                    }
//                });


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ride_cycle) {
            if(true){
                Intent i=new Intent(getApplicationContext(), RideCycle.class);
                i.putExtra("cycle","1");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            activity=0;

            // Handle the camera action
        } else if (id == R.id.past_trips) {
            if(activity!=1){
                Intent i=new Intent(getApplicationContext(),PastTrips.class);
                i.putExtra("cycle","0");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            activity=1;

        } else if (id == R.id.feedback) {
            if(activity!=2){
                activity=2;
                Intent i=new Intent(getApplicationContext(), Feedback.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

        } else if (id == R.id.rent_cycle) {
            if(activity!=3){
                activity=3;
                Intent i=new Intent(getApplicationContext(), RentCycle.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }else if (id == R.id.stop_rent) {
            if(activity!=4){
                activity=4;
                Intent i=new Intent(getApplicationContext(), StopRent.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }else if (id == R.id.logout) {
            if(activity!=5){
                activity=5;
                Intent i=new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }else if (id == R.id.user_map) {
            if(activity!=6){
                activity=6;
                Intent i=new Intent(getApplicationContext(), MapUser.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
