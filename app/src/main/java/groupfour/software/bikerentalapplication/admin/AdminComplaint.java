package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.models.ComplaintModel;
import groupfour.software.bikerentalapplication.utility.Constants;

public class AdminComplaint extends AdminBaseActivity {
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaint);
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

        accessToken = preferences.getString(Constants.STORED_ACCESS_TOKEN, "null");
        sendGetAllComplaintsRequest();
        onCreateDrawer();
    }

    private void sendGetAllComplaintsRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.IPSERVER + Constants.COMPLAINTS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("VOLLEY", "RESPONSE " + response);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ComplaintModel[] complaintModels = objectMapper.readValue(response, ComplaintModel[].class);
                    List<ComplaintModel> complaintList = Arrays.asList(complaintModels);
                    ComplaintAdapter adapter = new ComplaintAdapter(getBaseContext(), complaintList);
                    ListView lview = findViewById(R.id.admin_lv);
                    lview.setAdapter(adapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);

                return params;
            }
        };

        queue.add(stringRequest);


    }

}
