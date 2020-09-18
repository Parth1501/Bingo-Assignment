package com.example.parth.bingo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.Random;

public class Splash_Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progressStatus=0;
    private Handler handler = new Handler();
    private static Random rand=new Random();
    private int r,g,b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        progressBar=findViewById(R.id.progressBar);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(Splash_Activity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, 2000);*/
        r=253;
        g=170;
        b=171;
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 8;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            if(g>=0&&b>=0)
                            progressBar.getProgressDrawable().setColorFilter(
                                    Color.rgb(r,g,b), android.graphics.PorterDuff.Mode.SRC_IN);
                            g-=13;
                            b-=13;
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent i=new Intent(Splash_Activity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }).start();
    }
}
