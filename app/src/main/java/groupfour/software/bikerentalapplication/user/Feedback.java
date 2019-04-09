package groupfour.software.bikerentalapplication.user;

import android.os.Bundle;

import groupfour.software.bikerentalapplication.R;

public class Feedback extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        onCreateDrawer();
    }
}
