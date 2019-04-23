package groupfour.software.bikerentalapplication.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import groupfour.software.bikerentalapplication.Models.PersonModel;
import groupfour.software.bikerentalapplication.Models.UserModel;
import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.Utility.Constants;

public class OTP extends AppCompatActivity {

    private EditText otp ;
    private static final String PROTOCOL_CHARSET = "utf-8";
    private Button login ;
    PersonModel personModel_1=null;
    PersonModel personModel;
    UserModel userModel;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp = findViewById(R.id.otp_otp);
        login = findViewById(R.id.otp_login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String errorMessage = validation() ;
                if (errorMessage.equals("ok")){
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        String name = extras.getString("name");
                        username=extras.getString("username");
                        String email=extras.getString("email");
                        String phone=extras.getString("phone");


                        try {
                            sendRequest3();
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        //The key argument here must match that used in the other activity
                    }
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OTP.this);
                    builder1.setTitle("Something went wrong");
                    builder1.setMessage(errorMessage);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
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
    public void sendRequest3() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        String jsonStr;
        final String requestBody;
        String url=Constants.IPSERVER+"/user/validateRegistrationOTP/"+username;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getBaseContext(),"Successfully registered",Toast.LENGTH_LONG).show();
                        // System.out.print(response);
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        Log.e("Volley",response+"3");
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
                params.put("otp",otp.getText().toString());
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
    }



    public String validation(){
        String errorMessage = "ok" ;


        String otpText = otp.getText().toString() ;

        if (!validateOTP(otpText)){
            errorMessage = "Invalid OTP" ;
        }

        return errorMessage ;

    }
    public boolean validateOTP(String otpText){

        return  checkOTPValid(otpText);
    }
    //TODO : validate OTP
    private boolean checkOTPValid(String otpText) {
        return true;

    }

    public boolean validatePassword(String password){
        return (password.length() > 7) ;

    }
}
