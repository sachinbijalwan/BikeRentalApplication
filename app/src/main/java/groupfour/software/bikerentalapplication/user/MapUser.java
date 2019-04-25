package groupfour.software.bikerentalapplication.user;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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

import java.lang.reflect.Field;
import java.util.Objects;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;

public class MapUser extends UserBaseActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap                   mMap;
    private boolean                     addMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user);
        onCreateDrawer();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_MAP_PERMISSIONS);
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    LatLng latlng = new LatLng(latitude, longitude);
                    if (!addMarker) {
                        mMap.addMarker(new MarkerOptions().position(latlng));
                        addMarker = true;
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
                    if (!addMarker) {
                        mMap.addMarker(new MarkerOptions().position(latlng));
                        addMarker = true;
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

    }

    protected void onCreateDrawer() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                Objects.requireNonNull(getNavButtonView(toolbar)).setZ(20);
                Objects.requireNonNull(getNavButtonView(toolbar)).setTranslationX(slideX);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(this);
        Drawable icon = getDrawable(R.drawable.ic_motorcycle_black_24dp);
        Objects.requireNonNull(getNavButtonView(toolbar)).setImageDrawable(icon);
        Objects.requireNonNull(getNavButtonView(toolbar)).setColorFilter(getResources().getColor(android.R.color.white));

    }

    private ImageButton getNavButtonView(Toolbar toolbar) {
        try {
            Class<?> toolbarClass = Toolbar.class;
            Field navButtonField = toolbarClass.getDeclaredField("mNavButtonView");
            navButtonField.setAccessible(true);

            return (ImageButton) navButtonField.get(toolbar);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


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

}
