package groupfour.software.bikerentalapplication.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.models.UserModel;
import groupfour.software.bikerentalapplication.user.UserBaseActivity;
import groupfour.software.bikerentalapplication.utility.Constants;

public class ForgotPasswordActivity extends UserBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        final TextView username = findViewById(R.id.forgot_pass_user);
        final TextView got_username = findViewById(R.id.forgot_pass_got_username);
        final TextView got_password = findViewById(R.id.forgot_pass_got_password);
        final EditText otp = findViewById(R.id.forgot_pass_otp);
        Button forgotPassword = findViewById(R.id.btn_forgot_pass_request_otp);
        Button sendOTP = findViewById(R.id.btn_forgot_pass_send_otp);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IPSERVER + Constants.USER + Constants.FORGOT_PASS + "/" + username
                        .getText()
                        .toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast
                                .makeText(getApplicationContext(), "Successfully sent Forgot Password Request", Toast.LENGTH_LONG)
                                .show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast
                                .makeText(getApplicationContext(), "Username not Found", Toast.LENGTH_LONG)
                                .show();
                    }
                });

                queue.add(stringRequest);
            }
        });

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IPSERVER + Constants.USER + Constants.VALIDATE_FORGOT_PASS_OTP + "/" + username
                        .getText()
                        .toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast
                                .makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG)
                                .show();
                        ObjectMapper objectMapper = new ObjectMapper();
                        UserModel userModel = null;
                        try {
                            userModel = objectMapper.readValue(response, UserModel.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        got_username.setText("Username: " + Objects.requireNonNull(userModel).getUsername());
                        got_password.setText("Password: " + userModel.getPassword());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
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
                        if (response.statusCode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                            Toast
                                    .makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG)
                                    .show();
                            return null;
                        } else return super.parseNetworkResponse(response);
                    }
                };

                queue.add(stringRequest);
            }
        });
    }
}
