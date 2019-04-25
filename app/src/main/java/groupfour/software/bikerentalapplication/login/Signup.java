package groupfour.software.bikerentalapplication.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;
import groupfour.software.bikerentalapplication.models.PersonModel;
import groupfour.software.bikerentalapplication.models.UserModel;

public class Signup extends AppCompatActivity {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText confirmPassword;

    private PersonModel personModel;
    private UserModel   userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.sign_username);
        name = findViewById(R.id.sign_name);
        email = findViewById(R.id.sign_email);
        phone = findViewById(R.id.sign_phone);
        password = findViewById(R.id.otp_password);
        confirmPassword = findViewById(R.id.otp_confirm_password);

        Button signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String errorMessage = validationResult();
                if (errorMessage.equals("ok")) {
                    startOTP();
                } else {
                    Builder builder = new Builder(Signup.this);
                    builder.setTitle("Invalid Details");
                    builder.setMessage(errorMessage);
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    public String validationResult() {
        String emailText = email.getText().toString();
        String phoneNo = phone.getText().toString();

        String errorMessage = "ok";

        if (!validateEmail(emailText)) {
            errorMessage = "Invalid Email";
        } else if (!validatePhone(phoneNo)) {
            errorMessage = "Invalid Phone number";
        }
        errorMessage = validateSignup(errorMessage);
        return errorMessage;

    }

    public boolean validateEmail(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            String domain = "iitrpr.ac.in";
            return email.endsWith(domain);
        }
        return false;
    }


    public boolean validatePhone(String mobile) {
        return mobile.matches("\\d{10}");
    }

    public void startOTP() {
        String jsonStr;
        PersonModel personModel = new PersonModel();
        personModel.setEmail(email.getText().toString());
        personModel.setContactNumber(Long.parseLong(phone.getText().toString()));
        personModel.setName(name.getText().toString());
        ObjectMapper objectMapper = new ObjectMapper();

        userModel = new UserModel();
        userModel.setUsername(username.getText().toString());
        userModel.setRole(UserModel.UserRole.NORMAL_USER);
        userModel.setPassword(password.getText().toString());
        try {
            jsonStr = objectMapper.writeValueAsString(personModel);
            sendPersonDetails(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), OTP.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);

    }

    public void sendPersonDetails(final String requestBody) {
        String url = Constants.IPSERVER + Constants.PERSON;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    personModel = objectMapper.readValue(response, PersonModel.class);
                    String jsonStr;
                    userModel.setPersonId(personModel.getId());
                    getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                            .edit()
                            .putInt(Constants.STORED_ID, personModel.getId())
                            .apply();
                    jsonStr = objectMapper.writeValueAsString(userModel);

                    sendUserDetails(jsonStr);
                    Toast
                            .makeText(getApplicationContext(), "Personal Details Created", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast
                        .makeText(getApplicationContext(), "Unable to create Personal Details", Toast.LENGTH_SHORT)
                        .show();
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

        };

        queue.add(stringRequest);

    }

    public void sendUserDetails(final String requestBody) {
        String url = Constants.IPSERVER + Constants.USER;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast
                        .makeText(getApplicationContext(), "Login Credentials Sent Successfully.", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast
                        .makeText(getApplicationContext(), "Unable to Send Login Credentials", Toast.LENGTH_SHORT)
                        .show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        queue.add(stringRequest);
    }

    public String validateSignup(String errorMessage) {
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        if (!(passwordText.equals(confirmPasswordText))) {
            errorMessage = "Password doesn't match";
        }
        return errorMessage;
    }

}
