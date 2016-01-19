package com.tomych.progress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (ProgressView) findViewById(R.id.progress);
        progressView.setFirstTitle("TIME TO FINISH:");
        progressView.setSecondTitle("TIME FROM START:");
        progressView.setMax(300);//Total timer duration
        progressView.setProgress(0);//Starting progress

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setProgress(progressView.getProgress()+1f);
                    }
                });
            }
        }, 0, 1000);



    }

}
