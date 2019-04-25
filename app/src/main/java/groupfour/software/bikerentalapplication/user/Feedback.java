package groupfour.software.bikerentalapplication.user;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;
import groupfour.software.bikerentalapplication.models.ComplaintModel;

public class Feedback extends UserBaseActivity {

    private static final String PROTOCOL_CHARSET = "utf-8";

    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private String intentData;
    private AutoCompleteTextView textView;

    private CameraSource cameraSource;
    private String personID;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        onCreateDrawer();
        textView = findViewById(R.id.autoCompleteTextView);
        accessToken = Objects.requireNonNull(getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.STORED_ACCESS_TOKEN, ""));
        personID = Objects.requireNonNull(getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.STORED_PERSON_ID, ""));
        String[] problems = getResources().getStringArray(R.array.cycle_problem);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, problems);
        textView.setAdapter(adapter);
        initViews();
        Button button = findViewById(R.id.btnsubmitcomplaint);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = textView.getText().toString();
                String qrCode = txtBarcodeValue.getText().toString();
                if (qrCode.equals("SCAN QR CODE")) {
                    Toast.makeText(getBaseContext(), "SCAN CYCLE FIRST", Toast.LENGTH_LONG).show();
                    return;
                }
                ComplaintModel complaint = new ComplaintModel();
                complaint.setDetails(text);
                try {
                    complaint.setCycleId(Integer.parseInt(intentData));
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "QR Code Value invalid: " + intentData, Toast.LENGTH_SHORT).show();
                }


                complaint.setPersonId(Integer.parseInt(personID));
                complaint.setDetails(textView.getText().toString());
                String url = Constants.COMPLAINTS;
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonStr = null;
                try {
                    jsonStr = objectMapper.writeValueAsString(complaint);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                sendRequest(jsonStr, url);
            }
        });

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void sendRequest(final String requestBody, String url2) {
        String url = Constants.IPSERVER + url2;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(), "Complaint Recorded", Toast.LENGTH_LONG).show();
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
            public byte[] getBody() {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Access_Token", accessToken);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    return Response.success(jsonResponse.toString(),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        queue.add(stringRequest);
    }

    private void initialiseDetectorsAndSources() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Feedback.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                        Toast.makeText(getApplicationContext(), "Camera started", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(Feedback.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;

                                txtBarcodeValue.setText(intentData);
                            } else {
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText("Cycle id: " + intentData);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
