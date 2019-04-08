package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import groupfour.software.bikerentalapplication.R;

public class ComplaintAdapter extends ArrayAdapter<Complaint> {
    public ComplaintAdapter(Context context, ArrayList<Complaint> resource) {
        super(context, 0,resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Complaint complaint=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_admin_complaint, parent, false);
        }
        TextView usern=convertView.findViewById(R.id.admin_complaint_username);
        TextView comp=convertView.findViewById(R.id.admin_complaint);
        usern.setText(complaint.getUser());
        comp.setText(complaint.getComplaint());
        //TODO: if last message is of admin then make text bold
        if(position%2==0){
            usern.setTypeface(null,Typeface.BOLD);
            comp.setTypeface(null,Typeface.BOLD);

        }
        return convertView;
    }
}
