package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import groupfour.software.bikerentalapplication.R;

public class CycleAdapter extends ArrayAdapter<Cycle> {
    public CycleAdapter(Context context, ArrayList<Cycle> resource) {
        super(context, 0, resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cycle cycle = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_admin_cycle, parent, false);
        }
        TextView cid = convertView.findViewById(R.id.admin_tv_cycle);
        TextView lid = convertView.findViewById(R.id.admin_tv_location);
        cid.setText(cycle.getCycleid());
        lid.setText(cycle.getLocationid());
        return convertView;
    }
}
