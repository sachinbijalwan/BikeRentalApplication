package groupfour.software.bikerentalapplication.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.VolleyLog;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import groupfour.software.bikerentalapplication.Models.ComplaintModel;
import groupfour.software.bikerentalapplication.Models.PersonModel;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;
import groupfour.software.bikerentalapplication.admin.AdminTransfer;
import groupfour.software.bikerentalapplication.admin.Complaint;
import groupfour.software.bikerentalapplication.login.ForgotPass;

public class Feedback extends BaseActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final String PROTOCOL_CHARSET = "utf-8";

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    AutoCompleteTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        onCreateDrawer();
        // Get a reference to the AutoCompleteTextView in the layout

        textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
// Get the string array
        String[] problems = getResources().getStringArray(R.array.cycle_problem);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, problems);
        textView.setAdapter(adapter);
        initViews();
        //textView.setInputType(InputType.TYPE_NULL);
        Button button = findViewById(R.id.btnsubmitcomplaint);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String text=textView.getText().toString();
                Log.d("Loginjkl","text "+text);
                String qrcode=txtBarcodeValue.getText().toString();
                if(qrcode.equals("SCAN QR CODE")){
                    Toast.makeText(getBaseContext(),"SCAN CYCLE FIRST",Toast.LENGTH_LONG).show();
                    return;
                }
                ComplaintModel complaint=new ComplaintModel();
                complaint.setDetails(text);
                try {
                    complaint.setCycleId(Integer.parseInt(qrcode));
                }
                catch (NumberFormatException e){
                    ;//complaint.setCycleId(1);
                }
                int id;
                id=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(Constants.STORED_ID,-1);
                //TODO: change it
                complaint.setPersonId(id);
                complaint.setDetails(textView.getText().toString());
                String url=Constants.COMPLAINTS;
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonStr = null;
                try {
                    jsonStr = objectMapper.writeValueAsString(complaint);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                sendRequest(jsonStr,url);
                Log.d("JSONSTR",jsonStr);

            }
        });

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        // btnAction = findViewById(R.id.btnAction);


        /*btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {
                    if (isEmail)
                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    }
                }


            }
        });*/
    }
    public void sendRequest(final String requestBody,String url2){
        String url= Constants.IPSERVER + "/" +url2;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Loginjkl",response);
                        Toast.makeText(getBaseContext(),"Complaint Recorded",Toast.LENGTH_LONG).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                SharedPreferences preferences =getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE);
                params.put("Access_Token",preferences.getString(Constants.STORED_ACCESS_TOKEN,""));
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
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
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Log.e("Volley","queue "+stringRequest.toString());

    }

    private void initialiseDetectorsAndSources() {

        // Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Feedback.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                        Toast.makeText(getApplicationContext(),"Camera started",Toast.LENGTH_LONG).show();
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

                                //      btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                               //   btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText("Cycle id: "+intentData);

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
