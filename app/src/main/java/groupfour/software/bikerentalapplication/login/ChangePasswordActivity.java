package groupfour.software.bikerentalapplication.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.user.UserBaseActivity;
import groupfour.software.bikerentalapplication.utility.Constants;


public class ChangePasswordActivity extends UserBaseActivity {

    private EditText username;
    private EditText password;
    private EditText newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        username = findViewById(R.id.change_pass_user);
        password = findViewById(R.id.change_pass_old_pass);
        newPassword = findViewById(R.id.change_pass_new_pass);
        Button changePassword = findViewById(R.id.btn_change_pass);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IPSERVER + Constants.USER + Constants.CHANGE_PASS;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast
                                .makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_LONG)
                                .show();
                        logoutPreferences();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                            Toast
                                    .makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast
                                    .makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Access_Token", Objects.requireNonNull(getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                                .getString(Constants.STORED_ACCESS_TOKEN, "")));
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("newPassword", newPassword.getText().toString());
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });
    }
}
