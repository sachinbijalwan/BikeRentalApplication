package groupfour.software.bikerentalapplication.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groupfour.software.bikerentalapplication.Models.PersonModel;
import groupfour.software.bikerentalapplication.Models.UserModel;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;

public class Signup extends AppCompatActivity {

    private EditText username , email , phone,password,confirm_password;
    private Button signIn ;
    private PersonModel personModel_1;
    private UserModel userModel;
    private static final String PROTOCOL_CHARSET = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.sign_username);
        email = findViewById(R.id.sign_email);

        signIn = findViewById(R.id.sign_sign);
        phone = findViewById(R.id.sign_phone);
        password=findViewById(R.id.otp_password);
        confirm_password=findViewById(R.id.otp_confirm_password);
        confirm_password=findViewById(R.id.otp_confirm_password);
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String errorMessage = validation() ;
                if (errorMessage.equals("ok")){
                    startotp();

                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Signup.this);
                    builder1.setTitle("Invalid Details");
                    builder1.setMessage(errorMessage);
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

    public String validation(){
        String usernameText = username.getText().toString();
        String emailText = email.getText().toString();

        String phoneNo = phone.getText().toString() ;
        String errorMessage = "ok" ;

        if (!validateUsername(usernameText)){
            errorMessage = "Username length at least 8 " ;
        }
        else if (!validateEmail(emailText)){
            errorMessage = "Invalid Email" ;
        }


        else if (!validatePhone(phoneNo)){
            errorMessage = "Invalid Phone number";
        }
        errorMessage=validationlogin(errorMessage);
        return errorMessage ;


    }

    public boolean validateEmail(String email){
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()){
            String domain = "iitrpr.ac.in" ;
            return email.endsWith(domain) ;
        }
        return false ;

    }

    public boolean validateUsername(String username){
         return (username.length() > 7) ;

    }


    public boolean validatePhone(String mobile){
        return mobile.matches("\\d{10}") ;
    }

    public void startotp(){
        String jsonStr;
        PersonModel personModel=new PersonModel();
        personModel.setEmail(email.getText().toString());
        personModel.setContactNumber(Long.parseLong(phone.getText().toString()));
        personModel.setName(username.getText().toString());
        ObjectMapper objectMapper = new ObjectMapper();


        userModel=new UserModel();
        userModel.setUsername(username.getText().toString());
        userModel.setRole(UserModel.UserRole.NORMAL_USER);
        userModel.setPassword(password.getText().toString());
        try {

            String url = Constants.PERSON;
            // get Oraganisation object as a json string
            jsonStr = objectMapper.writeValueAsString(personModel);
            sendRequest(jsonStr,url);


        }

        catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), OTP.class);
        intent.putExtra("name",username.getText().toString());
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("username",username.getText().toString());
        intent.putExtra("phone",phone.getText().toString());
        startActivity(intent);

    }
    public void sendRequest(final String requestBody,String url2){
        String url=  Constants.IPSERVER + "/" +url2;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Log.e("Volley",response+" 1");

                            // get Organisation object as a json string
                            personModel_1 = objectMapper.readValue(response, PersonModel.class);
                            Log.e("Volley","running");
                            String jsonStr;
                            String url=Constants.USER;
                            jsonStr=null;
                            userModel.setPersonId(personModel_1.getId());
                            getSharedPreferences(Constants.PREFERENCES,Context.MODE_PRIVATE).edit().putInt(Constants.STORED_ID,personModel_1.getId()).apply();
                            //   PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(Constants.ACCESS_TOKEN,).apply();
                            //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("ID",userModel.getPersonId()).apply();
                            Log.e("Volley","running 2");

                            jsonStr=objectMapper.writeValueAsString(userModel);
                            Log.e("Volley","running 3");

                            sendRequest2(jsonStr,url);
                            Log.e("Volley","running 4");

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


        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Log.e("Volley","queue "+stringRequest.toString());

    }

    public void sendRequest2(final String requestBody,String url2){
        String url= Constants.IPSERVER + "/" +url2;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("Volley",response+" 2");
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
                Map<String, String>  params = new HashMap<String, String>();

                return super.getHeaders();
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Log.e("Volley","queue "+stringRequest.toString());

    }
    public String validationlogin(String errorMessage){


        String passwordText = password.getText().toString();

        String confirmPasswordText = confirm_password.getText().toString();

        if (!validatePassword(passwordText)){
            errorMessage = "Invalid Password" ;
        }
        else if (!(passwordText.equals(confirmPasswordText))){
            errorMessage = "Password doesn't match" ;
        }
        return errorMessage ;

    }


    public boolean validatePassword(String password){
        return (password.length() > 7) ;

    }
}
