package groupfour.software.bikerentalapplication.admin;

import android.os.Bundle;

import java.util.ArrayList;

import groupfour.software.bikerentalapplication.R;

public class AdminCycle extends AdminBaseActivity {

    private void addcycles(ArrayList<Cycle> cycles) {
        cycles.clear();
        for (int i = 0; i < 10; i++) {
            cycles.add(new Cycle("Cycle" + i, "LocationModel" + i));
        }
    }

    private void addLocations(ArrayList<Cycle> cycles) {
        cycles.clear();
        for (int i = 0; i < 10; i++) {
            cycles.add(new Cycle("LocationModel " + i, "Cycle " + i));
        }
    }
//    private void setCycles(){
//        //code for binding array list with cycle adapter
//        //for more info see https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
//        ArrayList<Cycle> cycles=new ArrayList<Cycle>();
//        addcycles(cycles);
//        System.out.println("Cycle size" + cycles.size());
//        CycleAdapter adapter=new CycleAdapter(this,cycles);
//        ListView lview=findViewById(R.id.admin_lv);
//        lview.setAdapter(adapter);
//    }
//    private void setLocations(){
//        //to be changed
//        //make separate LocationModel class or find some workaround
//        ArrayList<Cycle> cycles=new ArrayList<Cycle>();
//        addLocations(cycles);
//        CycleAdapter adapter=new CycleAdapter(this,cycles);
//        ListView lview=findViewById(R.id.admin_lv);
//        lview.setAdapter(adapter);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        onCreateDrawer();
        //setCycles();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String c = extras.getString("cycle");
            boolean cycle = c.equals("1");
            //The key argument here must match that used in the other activity
            //   Log.d("AFD","This is cycle "+(c=="1")+" dk "+c);
        }

//        if(cycle){
//            setCycles();
//        }
//        else{
//            setLocations();
//        }
    }
}
