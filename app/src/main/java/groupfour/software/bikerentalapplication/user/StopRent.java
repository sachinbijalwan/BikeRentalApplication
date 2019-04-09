package groupfour.software.bikerentalapplication.user;

import android.os.Bundle;
import groupfour.software.bikerentalapplication.R;

public class StopRent extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_rent);
        onCreateDrawer();
    }
}
