package groupfour.software.bikerentalapplication.admin;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import groupfour.software.bikerentalapplication.Models.Location;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;


public class AdminLocation extends BaseActivity {

    private EditText locationName, latitude, longitude;
    private Button submit;
    private String name, lat, longi;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location);
        onCreateDrawer();
        locationName = findViewById(R.id.locationName);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                final String requestBody = getJsonStrLocation();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://" + Constants.IPSERVER + "/" + Constants.LOCATION;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                System.out.print(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

// Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
        });




    }

    public String getJsonStrLocation() {
        String jsonStr = null ;
        Location adminLocation = new Location();
        adminLocation.setName(locationName.getText().toString());
        adminLocation.setLongitude(Double.parseDouble(longitude.getText().toString()));
        adminLocation.setLatitude(Double.parseDouble(latitude.getText().toString()));

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            // get Oraganisation object as a json string
            jsonStr = objectMapper.writeValueAsString(adminLocation);

            // Displaying JSON String
            System.out.println(jsonStr);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr ;
    }
}
