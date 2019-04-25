package groupfour.software.bikerentalapplication.user;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.lang.reflect.Field;
import java.util.Objects;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;
import groupfour.software.bikerentalapplication.login.LoginActivity;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected void onCreateDrawer() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        drawer = findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                ImageButton navButton = Objects.requireNonNull(getNavButtonView(toolbar));
                navButton.setZ(20);
                navButton.setTranslationX(slideX);
            }

        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(this);
        Drawable icon = getDrawable(R.drawable.ic_motorcycle_black_24dp);
        ImageButton navButton = Objects.requireNonNull(getNavButtonView(toolbar));
        navButton.setImageDrawable(icon);
        navButton.setColorFilter(getResources().getColor(R.color.white, null));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.ride_cycle:
                intent = new Intent(getApplicationContext(), RideCycle.class);
                break;
            case R.id.past_trips:
                intent = new Intent(getApplicationContext(), PastTrips.class);
                break;
            case R.id.feedback:
                intent = new Intent(getApplicationContext(), Feedback.class);
                break;
            case R.id.rent_cycle:
                intent = new Intent(getApplicationContext(), RentCycle.class);
                break;
            case R.id.stop_rent:
                intent = new Intent(getApplicationContext(), StopRent.class);
                break;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(Constants.STORED_ACCESS_TOKEN);
                editor.remove(Constants.STORED_USERNAME);
                editor.remove(Constants.STORED_EMAIL);
                editor.remove(Constants.STORED_ID);
                editor.remove(Constants.STORED_ROLE);
                editor.apply();
                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            case R.id.user_map:
                intent = new Intent(getApplicationContext(), MapUser.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
