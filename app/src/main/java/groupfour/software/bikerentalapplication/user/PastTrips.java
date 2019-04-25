package groupfour.software.bikerentalapplication.user;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;

public class PastTrips extends UserBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);
        onCreateDrawer();
    }

    public void sendRequest(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String uri = String.format(Constants.IPSERVER + Constants.RIDE + "?personId=%1$s", id);

        StringRequest myReq = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                attachresponsetolist(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(myReq);
    }

}
