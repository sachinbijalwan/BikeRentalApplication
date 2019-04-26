package groupfour.software.bikerentalapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;

public class OTP extends AppCompatActivity {

    String username;
    private EditText otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp = findViewById(R.id.otp_otp);
        Button login = findViewById(R.id.otp_login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    username = extras.getString("username");
                    sendOTP();
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    public void sendOTP() {
        String url = Constants.IPSERVER + Constants.USER + Constants.VALIDATE_REGISTRATION_OTP + "/" + username;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast
                        .makeText(getBaseContext(), "Successfully registered", Toast.LENGTH_LONG)
                        .show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast
                        .makeText(getBaseContext(), "Invalid OTP/Unable to Register", Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otp.getText().toString());
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == HttpURLConnection.HTTP_OK) {
                    return super.parseNetworkResponse(response);
                } else {
                    Toast.makeText(getBaseContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };

        queue.add(stringRequest);
    }

}
