package groupfour.software.bikerentalapplication.admin;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;

public class AdminTransfer extends AdminBaseActivity {

    private SurfaceView surfaceView;
    private TextView    txtBarcodeValue;
    private EditText    destinationID;
    private Button      submit;
    private String      intentData = "";
    private boolean     isEmail;

    private CameraSource cameraSource;
    private String       accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transfer);
        accessToken = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.STORED_ACCESS_TOKEN, "");
        destinationID = findViewById(R.id.destinationId);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (intentData.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminTransfer.this);
                    builder.setTitle("Invalid CycleId");
                    builder.setMessage("Please scan QR code once more");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    getStartTimeRequest(intentData.substring(0, intentData.length() - 1), destinationID
                            .getText()
                            .toString());

                }
            }
        });
        onCreateDrawer();
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
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
                    if (ActivityCompat.checkSelfPermission(AdminTransfer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                        Toast
                                .makeText(getApplicationContext(), "Camera started", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(AdminTransfer.this, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA_PERMISSION);
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
                Toast
                        .makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT)
                        .show();
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
                                isEmail = true;
                            } else {
                                isEmail = false;
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


    private void getStartTimeRequest(final String cycleId, final String newLocation) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = Constants.IPSERVER + Constants.CYCLE + Constants.CHANGE_LOCATION;
        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.d("Volley", "Response: " + response);
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminTransfer.this);
                builder.setTitle("Location Changed");

                builder.setCancelable(true);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Access_Token", accessToken);
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", cycleId);
                params.put("newLocation", "2");
                return params;
            }

        };

        queue.add(jsonObjRequest);
    }
}
