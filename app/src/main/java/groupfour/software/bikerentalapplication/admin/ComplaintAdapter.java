package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import groupfour.software.bikerentalapplication.Models.ComplaintModel;
import groupfour.software.bikerentalapplication.R;

public class ComplaintAdapter extends ArrayAdapter<ComplaintModel> {
    public ComplaintAdapter(Context context, List<ComplaintModel> resource) {
        super(context, 0,resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComplaintModel complaint=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_admin_complaint, parent, false);
        }
        TextView usern=convertView.findViewById(R.id.admin_complaint_username);
        TextView comp=convertView.findViewById(R.id.admin_complaint);
        String us=String.valueOf(complaint.getPersonId());
        usern.setText("USER "+us);
        comp.setText("COMPLAINT "+complaint.getDetails());
        if(complaint.getStatus()==ComplaintModel.ComplaintStatus.UNRESOLVED){
            usern.setTypeface(null,Typeface.BOLD);
            comp.setTypeface(null,Typeface.BOLD);

        }
        usern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminResolveComplaint.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminResolveComplaint.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}
