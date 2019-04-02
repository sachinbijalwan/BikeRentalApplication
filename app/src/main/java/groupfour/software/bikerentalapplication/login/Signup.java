package groupfour.software.bikerentalapplication.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import groupfour.software.bikerentalapplication.R;

public class Signup extends AppCompatActivity {

    private EditText username , email , phone;
    private Button signIn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.sign_username);
        email = findViewById(R.id.sign_email);

        signIn = findViewById(R.id.sign_sign);
        phone = findViewById(R.id.sign_phone);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String errorMessage = validation() ;
                if (errorMessage.equals("ok")){
                    registerUserInDatabase();
                    Intent intent = new Intent(getApplicationContext(), OTP.class);
                    startActivity(intent);
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

    //TODO: Connect with backend
    public void registerUserInDatabase(){

    }
}
