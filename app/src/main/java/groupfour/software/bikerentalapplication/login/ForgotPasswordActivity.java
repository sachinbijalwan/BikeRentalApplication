package groupfour.software.bikerentalapplication.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });

                queue.add(stringRequest);
            }
        });

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IPSERVER + Constants.USER + Constants.VALIDATE_FORGOT_PASS_OTP;
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
                        got_username.setText("Username: " + userModel.getUsername());
                        got_password.setText("Password: " + userModel.getPassword());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });

                queue.add(stringRequest);
            }
        });
    }
}
