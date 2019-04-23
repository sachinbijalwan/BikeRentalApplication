package groupfour.software.bikerentalapplication.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ZoomButtonsController;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.Models.PersonModel;
import groupfour.software.bikerentalapplication.Models.Session;
import groupfour.software.bikerentalapplication.Models.UserModel;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;
import groupfour.software.bikerentalapplication.admin.AdminCycle;
import groupfour.software.bikerentalapplication.admin.AdminLocation;
import groupfour.software.bikerentalapplication.user.MapUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button forgotPassword;
    private Button createAccount;
    private String PREFS_NAME = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).getString(Constants.STORED_ACCESS_TOKEN,"").isEmpty()){
            Log.d("Loginjkl",getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).getString(Constants.STORED_ACCESS_TOKEN,""));
            if(getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).getString(Constants.STORED_ROLE,"USER_NAME").equals("ADMIN")){
                Intent intent = new Intent(getApplicationContext(), AdminCycle.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(getApplicationContext(), MapUser.class);
                startActivity(intent);
                finish();
            }
        }

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_login);
        forgotPassword = findViewById(R.id.login_forgot);
        createAccount = findViewById(R.id.login_create);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (isDetailsCorrect(username.getText().toString(), password.getText().toString())) {


                    sendRequest();

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                    builder1.setTitle("Failed to Log In");
                    builder1.setMessage("Invalid Username or Password");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
        });

    }

    public void sendRequest(){
        String url=  Constants.IPSERVER + "/" +Constants.SESSION;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Session session=new Session();
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Log.e("Volley",response+" 1");

                            // get Organisation object as a json string
                            session = objectMapper.readValue(response, Session.class);
                            Log.e("Volley","running");
                            //userModel.setPersonId(personModel_1.getId());
                            getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).edit().putString(Constants.STORED_ACCESS_TOKEN,session.getAccessToken()).apply();

                            //TODO: change it
                            //getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).edit().putInt(Constants.STORED_ID,1).apply();
                            Log.d("Login",session.getIdentity());
                            getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).edit().putString(Constants.STORED_ROLE,UserModel.UserRole.NORMAL_USER.toString()).apply();
                            //   PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Constants.ACCESS_TOKEN,).apply();
                            //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("ID",userModel.getPersonId()).apply();
                            Log.e("Volley","running 2");
                            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("USERID", username.getText().toString());
                            editor.apply();
                            if (username.getText().toString().equals("adminadmin")) {
                                getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).edit().putString(Constants.STORED_ROLE,UserModel.UserRole.ADMIN.toString()).apply();

                                Intent intent = new Intent(getApplicationContext(), AdminCycle.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MapUser.class);
                                startActivity(intent);
                            }

                        }

                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        // System.out.print(response);
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                return params;
            }



        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Log.e("Volley","queue "+stringRequest.toString());

    }

    //TODO
    public boolean isDetailsCorrect(String username, String password) {

        if (username.length() > 4 && !Character.isDigit(username.charAt(username.length() - 1))) {

            return true;
        } else {
            return false;
        }

    }
}
