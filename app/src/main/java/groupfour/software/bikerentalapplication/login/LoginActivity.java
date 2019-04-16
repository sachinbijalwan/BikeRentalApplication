package groupfour.software.bikerentalapplication.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ZoomButtonsController;

import groupfour.software.bikerentalapplication.R;
import groupfour.software.bikerentalapplication.admin.AdminCycle;
import groupfour.software.bikerentalapplication.user.MapUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username ;
    private EditText password ;
    private Button login ;
    private Button forgotPassword ;
    private Button createAccount ;
    private String PREFS_NAME="USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_login);
        forgotPassword = findViewById(R.id.login_forgot);
        createAccount = findViewById(R.id.login_create);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(getApplicationContext(), ForgotPass.class);
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
                if (isDetailsCorrect(username.getText().toString(), password.getText().toString())){
                    SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("USERID", username.getText().toString());
                    editor.apply();

                    if (username.getText().toString().equals("admin")){
                        Intent intent = new Intent(getApplicationContext(), AdminCycle.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent (getApplicationContext(), MapUser.class);
                        startActivity(intent);
                    }
                }
                else {
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
    //TODO
    public boolean isDetailsCorrect(String username, String password){
        if (username.length() > 4 && !Character.isDigit(username.charAt(username.length()-1))){
            return true ;
        }
        else {
            return  false ;
        }

    }
}
