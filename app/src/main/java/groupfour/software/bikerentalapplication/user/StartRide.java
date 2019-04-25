package groupfour.software.bikerentalapplication.user;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;
import groupfour.software.bikerentalapplication.models.RideCycle;

public class StartRide extends AppCompatActivity {

    RideCycle rideCycle;
    private TextView rideTime, rideAmount, rideEndTime;
    private Button rideEnd;
    private String cycleId;
    private String personID = "2";
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);
        rideTime = findViewById(R.id.rideTime);
        rideAmount = findViewById(R.id.rideAmount);
        rideEnd = findViewById(R.id.btnRideEnd);
        rideEndTime = findViewById(R.id.rideEndtime);
        Intent intent = getIntent();
        cycleId = intent.getStringExtra("cycleId");
        //personID = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Constants.STORED_ID,"null");
        accessToken = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getString(Constants.STORED_ACCESS_TOKEN, "null");

        getStartTimeRequest(accessToken, cycleId, personID);
        rideEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getEndTimeRequest(Integer.toString(rideCycle.getId()), "1");

            }
        });


    }


    private void getStartTimeRequest(final String accessToken, final String cycleId, final String personId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = Constants.IPSERVER + Constants.RIDE + Constants.RIDE_START;
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.d("Volley", "Response: " + response);
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    // get Oraganisation object as a json string
                    rideCycle = objectMapper.readValue(response, RideCycle.class);

                    DateFormat simple = new SimpleDateFormat("HH:mm:ss");
                    Date current = new Date(rideCycle.getStartTime());
                    rideTime.setText("Start time : " + simple.format(current));


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                error.printStackTrace();

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("startLocationId", "1");
                params.put("cycleId", cycleId);
                params.put("personId", personId);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    //jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse.toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }


        };

        queue.add(jsonObjRequest);
    }

    private void getEndTimeRequest(final String rideId, final String endLocationId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = Constants.IPSERVER + Constants.RIDE + Constants.RIDE_END;
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.d("Volley", "Response: " + response);
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    // get Oraganisation object as a json string
                    RideCycle rideCycle1 = objectMapper.readValue(response, RideCycle.class);
                    rideCycle.setEndLocationId(rideCycle1.getEndLocationId());
                    rideCycle.setEndTime(rideCycle1.getEndTime());
                    rideCycle.setCost(rideCycle1.getCost());

                    DateFormat simple = new SimpleDateFormat("HH:mm:ss");
                    Date current = new Date(rideCycle.getEndTime());

                    NumberFormat formatter = new DecimalFormat("#0.00");
                    String cost = formatter.format(rideCycle.getCost());

                    rideEndTime.setText("Ride End time : " + simple.format(current));
                    rideAmount.setText("Total Amount : " + cost + " Rs");


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                error.printStackTrace();

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("endLocationId", endLocationId);
                params.put("rideId", rideId);

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    //jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse.toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        queue.add(jsonObjRequest);
    }


}
