package groupfour.software.bikerentalapplication.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;
import groupfour.software.bikerentalapplication.models.CycleInfo;

public class RentCycle extends UserBaseActivity {
    String text = ""; // Whatever you need to encode in the QR code
    private String PREFS_NAME  = "USER";
    private String cycleBrand  = "Atlas";
    private int    locationId  = 1;
    private int    ownerId     = 1;
    private String accessToken = "47420131-3f37-4bd0-b811";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_cycle);
        onCreateDrawer();
        //ownerId = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Constants.STORED_ID,"-1"));
        accessToken = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext())
                .getString(Constants.STORED_ACCESS_TOKEN, "null");
        if (text.isEmpty()) {
            sendJsonString();
        }


    }

    public void writeqrcode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        ImageView imageView = findViewById(R.id.imgqrcode);
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void sendJsonString() {
        CycleInfo cycleInfo = new CycleInfo();
        cycleInfo.setBrand(cycleBrand);
        cycleInfo.setLocationId(locationId);
        cycleInfo.setOwnerId(ownerId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            // get Oraganisation object as a json string
            String jsonStr = objectMapper.writeValueAsString(cycleInfo);

            // Displaying JSON String
            System.out.println(jsonStr);
            sendRequest(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View view) {
        sendJsonString();
        Toast
                .makeText(getApplicationContext(), "QR CODE CHANGED SUCCESSFULLY", Toast.LENGTH_LONG)
                .show();
    }

    public void sendRequest(final String requestBody) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.IPSERVER + Constants.CYCLE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    // get Oraganisation object as a json string
                    CycleInfo cycleInfo = objectMapper.readValue(response, CycleInfo.class);
                    text = Integer.toString(cycleInfo.getId());
                    writeqrcode();

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
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/json");

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
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}
