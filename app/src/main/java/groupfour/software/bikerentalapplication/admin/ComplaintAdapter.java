package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.models.ComplaintModel;
import groupfour.software.bikerentalapplication.models.PersonModel;
import groupfour.software.bikerentalapplication.models.Session;
import groupfour.software.bikerentalapplication.models.UserInformationModel;
import groupfour.software.bikerentalapplication.models.UserModel;
import groupfour.software.bikerentalapplication.user.MapUser;
import groupfour.software.bikerentalapplication.utility.Constants;

public class ComplaintAdapter extends ArrayAdapter<ComplaintModel> {
    Context        context;
    ComplaintModel complaint;
    TextView       usern, comp;
    Button button;

    public ComplaintAdapter(Context context1, List<ComplaintModel> resource) {
        super(context1, 0, resource);
        context = context1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        complaint = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.item_admin_complaint, parent, false);
        }
        usern = convertView.findViewById(R.id.admin_complaint_username);
        comp = convertView.findViewById(R.id.admin_complaint);
        LinearLayout linearLayout = convertView.findViewById(R.id.layout_admin_resolve);
        button = convertView.findViewById(R.id.admin_complaint_resolve_button);

        String us = String.valueOf(complaint.getPersonId());
        usern.setText("USER " + us);
        comp.setText("COMPLAINT " + complaint.getDetails());
        if (complaint.getStatus() == ComplaintModel.ComplaintStatus.UNRESOLVED) {
            usern.setTypeface(null, Typeface.BOLD);
            comp.setTypeface(null, Typeface.BOLD);

        } else {
            button.setVisibility(View.INVISIBLE);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminResolveComplaint.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResolveComplaintRequest();
            }
        });

        return convertView;
    }

    public void createResolveComplaintRequest() {
        String url = Constants.IPSERVER + Constants.COMPLAINTS;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ComplaintModel complaint = objectMapper.readValue(response, ComplaintModel.class);
                    if (complaint.getStatus() == ComplaintModel.ComplaintStatus.RESOLVED) {
                        Toast.makeText(context, "Complaint Resolved", Toast.LENGTH_LONG).show();
                        usern.setTypeface(null, Typeface.NORMAL);
                        comp.setTypeface(null, Typeface.NORMAL);
                        button.setVisibility(View.INVISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast
                        .makeText(context, "Complaint not resolved " + error.toString(), Toast.LENGTH_SHORT)
                        .show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                String accessToken = context
                        .getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                        .getString(Constants.STORED_ACCESS_TOKEN, "");
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("complaintId", String.valueOf(complaint.getId()));
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
