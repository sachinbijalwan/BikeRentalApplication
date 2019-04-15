package groupfour.software.bikerentalapplication.user;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import groupfour.software.bikerentalapplication.R;

public class StartRide extends AppCompatActivity {

    private TextView rideTime, rideDistance, rideAmount ;
    private Button rideEnd ;
    private String cycleId ;

    private long startTime = 0L;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);
        rideTime = findViewById(R.id.rideTime);
        //rideDistance = findViewById(R.id.rideDistance);
        rideAmount = findViewById(R.id.rideAmount);
        rideEnd = findViewById(R.id.btnRideEnd);

        Intent intent = getIntent();
        cycleId = intent.getStringExtra("cycleId");

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        rideEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);


            }
        });


    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            rideTime.setText("Time duration: " + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            int amount = (mins/60)*10 + 10 ;
            rideAmount.setText("Total Amount : " + amount + " Rs");
            customHandler.postDelayed(this, 0);
        }

    };
}
