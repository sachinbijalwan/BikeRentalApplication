package groupfour.software.bikerentalapplication.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import groupfour.software.bikerentalapplication.R;

public class AdminCycle extends BaseActivity {

    protected boolean cycle=true;
    protected void addcycles(ArrayList<Cycle> cycles){
        cycles.clear();
        for(int i=0;i<10;i++){
            cycles.add(new Cycle("Cycle"+i,"Location"+i));
        }
    }
    protected void addLocations(ArrayList<Cycle> cycles){
        cycles.clear();
        for(int i=0;i<10;i++){
            cycles.add(new Cycle("Location "+i,"Cycle "+i));
        }
    }
    protected void setCycles(){
        //code for binding array list with cycle adapter
        //for more info see https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayList<Cycle> cycles=new ArrayList<Cycle>();
        addcycles(cycles);
        CycleAdapter adapter=new CycleAdapter(this,cycles);
        ListView lview=findViewById(R.id.admin_lv);
        lview.setAdapter(adapter);
    }
    protected void setLocations(){
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
        onCreateDrawer();
        setCycles();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String c=extras.getString("cycle");
            if(c.equals("1"))
                cycle = true;
            else
                cycle=false;
            //The key argument here must match that used in the other activity
         //   Log.d("AFD","This is cycle "+(c=="1")+" dk "+c);
        }

        if(cycle){
            setCycles();
        }
        else{
            setLocations();
        }
    }
}
