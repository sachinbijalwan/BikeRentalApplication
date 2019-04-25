package groupfour.software.bikerentalapplication.admin;

import android.os.Bundle;

import groupfour.software.bikerentalapplication.R;

public class AdminCycle extends AdminBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        onCreateDrawer();
        Bundle extras = getIntent().getExtras();
    }
}
