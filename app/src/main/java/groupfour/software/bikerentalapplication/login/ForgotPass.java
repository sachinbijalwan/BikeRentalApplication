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

public class ForgotPass extends AppCompatActivity {

    private EditText email ;
    private Button getOTP ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        email = findViewById(R.id.forgot_user);
        getOTP = findViewById(R.id.forgot_otp);

        getOTP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (isEmailValid(email.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), OTP.class);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgotPass.this);
                    builder1.setTitle("Invalid Email");
                    builder1.setMessage("Email doesn't exist");
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
    //TODO
    public boolean isEmailValid(String email){
        return true ;
    }
}
