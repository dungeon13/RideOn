package com.example.avin.demo;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity  {

    private long timeCountInMilliSeconds = 1 * 60000;
    String user;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    Button button;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        button = findViewById(R.id.end);
        // method call to initialize the views
        initViews();
        // method call to initialize the listeners

        startStop();
        Bundle b2 = getIntent().getExtras();
        user = b2.getString("user");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showbook();
            }
        });

    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        progressBarCircle =  findViewById(R.id.progressbar);
        textViewTime =  findViewById(R.id.textViewTime);

    }
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }
    private void setTimerValues() {
        int time = 1; // here we decide the time alloted
        timeCountInMilliSeconds = time * 60 * 1000;
    }
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                timerStatus = TimerStatus.STOPPED;
                Toast toast = Toast.makeText(getApplicationContext(),"Your Time is Finished",Toast.LENGTH_SHORT);
                toast.show();
                showbook();
            }

        }.start();
        countDownTimer.start();
    }
    public void showbook(){
        // display book activity
        Bundle b4 = new Bundle();
        b4.putString("user",user);
        Intent intent = new Intent(this,cancel.class);
        intent.putExtras(b4);
        startActivity(intent);
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }
}