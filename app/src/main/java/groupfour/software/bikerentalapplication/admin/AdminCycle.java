package groupfour.software.bikerentalapplication.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import groupfour.software.bikerentalapplication.R;

public class AdminCycle extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        onCreateDrawer();
        if(cycle){
            setCycles();
        }
        else{
            setLocations();
        }
    }
}
