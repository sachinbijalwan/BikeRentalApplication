package groupfour.software.bikerentalapplication.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import groupfour.software.bikerentalapplication.R;

public class OTP extends AppCompatActivity {

    private EditText otp, password, confirmPassword ;
    private Button login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp = findViewById(R.id.otp_otp);
        login = findViewById(R.id.otp_login);
        password = findViewById(R.id.otp_password);
        confirmPassword = findViewById(R.id.otp_confirm_password);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String errorMessage = validation() ;
                if (errorMessage.equals("ok")){
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
    public String validation(){
        String errorMessage = "ok" ;

        String passwordText = password.getText().toString();

        String confirmPasswordText = confirmPassword.getText().toString();
        String otpText = otp.getText().toString() ;

        if (!validateOTP(otpText)){
            errorMessage = "Invalid OTP" ;
        }
        else if (!validatePassword(passwordText)){
            errorMessage = "Invalid Password" ;
        }
        else if (!(passwordText.equals(confirmPasswordText))){
            errorMessage = "Password doesn't match" ;
        }
        return errorMessage ;

    }
    public boolean validateOTP(String otpText){

        return  checkOTPValid(otpText);
    }
    //TODO : validate OTP
    private boolean checkOTPValid(String otpText) {
        if (otpText.equals("1234")){
            return  true ;
        }
        else {
            return  false ;
        }

    }

    public boolean validatePassword(String password){
        return (password.length() > 7) ;

    }
}
