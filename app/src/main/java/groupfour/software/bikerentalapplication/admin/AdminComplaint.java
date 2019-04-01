package groupfour.software.bikerentalapplication.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import groupfour.software.bikerentalapplication.R;

public class AdminComplaint extends BaseActivity {
    private void addcomplaint(ArrayList<Complaint> complaints){
        complaints.clear();
        for(int i=0;i<10;i++){
            complaints.add(new Complaint(i,"Complaint"+i,"User"+i));
        }
    }

    private void setComplaint(){
        //code for binding array list with cycle adapter
        //for more info see https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayList<Complaint> complaints=new ArrayList<Complaint>();
        addcomplaint(complaints);
        ComplaintAdapter adapter=new ComplaintAdapter(this,complaints);
        ListView lview=findViewById(R.id.admin_lv);
        lview.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaint);
        onCreateDrawer();
        setComplaint();
    }
}
