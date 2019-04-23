package groupfour.software.bikerentalapplication.admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.Models.LocationModel;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;


public class AdminLocation extends BaseActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private EditText locationName, latitude, longitude;
    private Button submit;
    private String name, lat, longi;
    private FusedLocationProviderClient fusedLocationClient;

    private String accessToken  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        onCreateDrawer();
        accessToken =PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Constants.STORED_ACCESS_TOKEN,"null");

        locationName = findViewById(R.id.locationName);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                postAdminLocation();


            }
        });

    }

    public void postAdminLocation() {
        String jsonStr = null;
        final LocationModel locationModel = new LocationModel();

        locationModel.setName(locationName.getText().toString());
        if (!longitude.getText().toString().isEmpty() && !latitude.getText().toString().isEmpty()) {
            locationModel.setLongitude(Double.parseDouble(longitude.getText().toString()));
            locationModel.setLatitude(Double.parseDouble(latitude.getText().toString()));

            ObjectMapper objectMapper = new ObjectMapper();
            try {

                // get Oraganisation object as a json string
                jsonStr = objectMapper.writeValueAsString(locationModel);
                sendRequest(jsonStr);
                // Displaying JSON String
                System.out.println(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdminLocation.this, new
                        String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                locationModel.setLongitude(location.getLongitude());
                                locationModel.setLatitude(location.getLatitude());

                                ObjectMapper objectMapper = new ObjectMapper();
                                try {

                                    // get Oraganisation object as a json string
                                    String jsonStr = objectMapper.writeValueAsString(locationModel);

                                    // Displaying JSON String
                                    System.out.println(jsonStr);
                                    sendRequest(jsonStr);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                System.out.println("Location is Null ");
                            }
                        }
                    });

        }


    }

    public void sendRequest(final String requestBody) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url =     Constants.IPSERVER + "/" + Constants.LOCATION;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminLocation.this);
                        builder1.setTitle("Location has been added");

                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
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
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    //jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse.toString(),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/json");

                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

}
