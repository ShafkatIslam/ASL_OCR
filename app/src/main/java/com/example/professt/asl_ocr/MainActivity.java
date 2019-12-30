package com.example.professt.asl_ocr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button profileListButton;
    private Button cameraReaderButton;

    private AlertDialog.Builder alertdialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileListButton = (Button) findViewById(R.id.profileListButton);
        cameraReaderButton = (Button) findViewById(R.id.cameraReaderButton);

        profileListButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cameraReaderButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraReaderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        /*moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        */
        alertdialogBuilder = new AlertDialog.Builder(MainActivity.this);    //creating object of alertDialogBuilder

        //setting the properties of alertDialogBuilder:

        //for setting title
        alertdialogBuilder.setTitle("ASL OCR");

        //for setting message
        alertdialogBuilder.setMessage("Do you want to exit?");

        //for setting icon
        alertdialogBuilder.setIcon(R.drawable.exit);

        //for setting cancelable
        alertdialogBuilder.setCancelable(false);

        //for setting Button
        alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //exit
//                finish();;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity(); // Close all activites
                    System.exit(0);  // Releasing resources
                }

//                System.exit(0);
                onDestroy();
            }
        });
        alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                // Toast.makeText(MainActivity.this,"You have clicked on no button",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertdialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                //Toast.makeText(MainActivity.this,"You have clicked on cancel button",Toast.LENGTH_SHORT).show();
            }
        });

        //showing alertDialog by creating alertDialog in object and creating alertDialogBuilder in this object
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }
}
