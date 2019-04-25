package groupfour.software.bikerentalapplication.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;
import groupfour.software.bikerentalapplication.models.ComplaintModel;

public class AdminComplaint extends BaseActivity {
    private String accessToken;

    private void addcomplaint(ArrayList<Complaint> complaints) {
        complaints.clear();
        for (int i = 0; i < 10; i++) {
            complaints.add(new Complaint(i, "Complaint" + i, "User" + i));
        }
    }

    private void setComplaint() {
        //code for binding array list with cycle adapter
        //for more info see https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayList<Complaint> complaints = new ArrayList<Complaint>();
        addcomplaint(complaints);
        //  ComplaintAdapter adapter=new ComplaintAdapter(getBaseContext(),complaints);
        ListView lview = findViewById(R.id.admin_lv);
        // lview.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaint);
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

        accessToken = preferences.getString(Constants.STORED_ACCESS_TOKEN, "null");
        sendRequest();
        //resolveComplaint("2");
        onCreateDrawer();
        //setComplaint();
    }

    public void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.IPSERVER + Constants.COMPLAINTS;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                //System.out.print("Response : " + response);
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
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    //JSONObject jsonResponse = new JSONObject(jsonString);
                    //jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } //catch (JSONException je) {
                //                    return Response.error(new ParseError(je));
                //                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);
                Log.e("VOLLEY", accessToken);


                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public void resolveComplaint(final String complaintId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = Constants.IPSERVER + Constants.COMPLAINTS;
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminComplaint.this);
                builder1.setTitle("Complaint has been resolved");

                builder1.setCancelable(true);

                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                params.put("complaintId", complaintId);

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
