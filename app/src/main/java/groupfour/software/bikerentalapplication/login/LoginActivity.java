package groupfour.software.bikerentalapplication.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.utility.Constants;
import groupfour.software.bikerentalapplication.admin.AdminCycle;
import groupfour.software.bikerentalapplication.models.PersonModel;
import groupfour.software.bikerentalapplication.models.Session;
import groupfour.software.bikerentalapplication.models.UserInformationModel;
import groupfour.software.bikerentalapplication.models.UserModel;
import groupfour.software.bikerentalapplication.models.UserModel.UserRole;
import groupfour.software.bikerentalapplication.user.MapUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if (!Objects
                .requireNonNull(sharedPreferences.getString(Constants.STORED_ACCESS_TOKEN, ""))
                .isEmpty()) {
            Intent intent;
            if (Objects.equals(sharedPreferences.getString(Constants.STORED_ROLE, ""), "ADMIN")) {
                intent = new Intent(getApplicationContext(), AdminCycle.class);
            } else {
                intent = new Intent(getApplicationContext(), MapUser.class);
            }
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        Button login = findViewById(R.id.login_login);
        Button forgotPassword = findViewById(R.id.login_forgot);
        Button createAccount = findViewById(R.id.login_create);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createSessionRequest();
            }
        });

    }

    public void createSessionRequest() {
        String url = Constants.IPSERVER + Constants.SESSION;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {

                    UserInformationModel userInformation = objectMapper.readValue(response, UserInformationModel.class);
                    Session session = userInformation.getSession();
                    UserModel user = userInformation.getUser();
                    PersonModel person = userInformation.getPerson();

                    Editor editor = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                            .edit();

                    editor.putString(Constants.STORED_USERNAME, session.getIdentity());
                    editor.putString(Constants.STORED_ACCESS_TOKEN, session.getAccessToken());
                    editor.putString(Constants.STORED_ROLE, UserRole.NORMAL_USER.toString());

                    editor.apply();

                    if (user.getRole().equals(UserRole.ADMIN)) {
                        getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                                .edit()
                                .putString(Constants.STORED_ROLE, UserRole.ADMIN.toString())
                                .apply();

                        Intent intent = new Intent(getApplicationContext(), AdminCycle.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MapUser.class);
                        startActivity(intent);
                    }
                    Toast
                            .makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }

}
