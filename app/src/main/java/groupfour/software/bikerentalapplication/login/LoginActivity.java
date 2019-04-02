package groupfour.software.bikerentalapplication.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ZoomButtonsController;

import groupfour.software.bikerentalapplication.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username ;
    private EditText password ;
    private Button login ;
    private Button forgotPassword ;
    private Button createAccount ;
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

    }
}
