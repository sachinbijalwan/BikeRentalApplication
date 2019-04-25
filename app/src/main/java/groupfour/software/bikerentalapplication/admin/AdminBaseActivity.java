package groupfour.software.bikerentalapplication.admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.login.LoginActivity;
import groupfour.software.bikerentalapplication.user.UserBaseActivity;

public class AdminBaseActivity extends UserBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_admin_main_drawer, menu);
        return true;
    }

    protected void onCreateDrawer() {
        onCreateDrawer(R.id.drawer_layout, R.id.nav_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.nav_transfer:
                intent = new Intent(getApplicationContext(), AdminTransfer.class);
                break;
            case R.id.nav_complaint:
                intent = new Intent(getApplicationContext(), AdminComplaint.class);
                break;
            case R.id.nav_add_location:
                intent = new Intent(getApplicationContext(), AdminLocation.class);
                break;
            case R.id.logout:
                logout();
                break;

        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
