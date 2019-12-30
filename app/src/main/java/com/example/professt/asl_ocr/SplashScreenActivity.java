package com.example.professt.asl_ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    private  int progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setContentView(R.layout.activity_splash_screen);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Thread thread = new Thread(new Runnable() {  // to change the value of progress during a time intervel we use this "Thread class" and we use "Runnable interface in the Thread Class.
            @Override
            public void run() {

                doWork();  //we call a method
                startAct();  //we call a method to go to the next Activiity

            }
        });
        thread.start();    //start the Thread Class
    }

    public void doWork(){    //create a method to get the progress from start to end during a time intervel

        for (progress=25;progress<=100;progress=progress+25)  //to get the progress the loop is used
        {
            try {
                Thread.sleep(1000);           //to get the progress from start to end during a time intervel
                progressBar.setProgress(progress);  //to set the progress value in the progressBar
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public void startAct(){   //create a method to go to the next Activiity

        Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);   //By using Intent class we can go one Activity to another Activity
        startActivity(intent);
        finish();

    }
}
