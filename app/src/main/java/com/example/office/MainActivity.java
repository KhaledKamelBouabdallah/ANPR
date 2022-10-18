package com.example.office;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    RelativeLayout city, parking, road;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getSupportActionBar().hide();
        city = (RelativeLayout) findViewById(R.id.layt1);
        parking =  (RelativeLayout) findViewById(R.id.layt3);
        road = (RelativeLayout) findViewById(R.id.layt2);

        MaterialAlertDialogBuilder load = new MaterialAlertDialogBuilder(this);

        System.out.println("interneeeeeeeeeeeeeeeeeeeeeeet");
        System.out.println(isNetworkConnected());
        load.setMessage("Please turn on your intenet first");
        System.out.println("hereeeeeeeeeeeeeeeeeeeeee");
        load.setPositiveButton("Go to settings", null);
        // the negative button
        load.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAndRemoveTask();
            }
        });
        dialog = load.create();

        if(!isNetworkConnected()){


            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));


                    dialog.dismiss();

                }
            });
        }

        System.out.println("nztwoooek ="+isNetworkConnected());

            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    //so that in the map we can know which button called the activity
                    intent.putExtra("map_type", "city");
                    intent.setClass(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                }
            });

            parking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    //so that in the map we can know which button called the activity
                    intent.putExtra("map_type", "parking");
                    intent.setClass(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                }
            });

            road.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    //so that in the map we can know which button called the activity
                    intent.putExtra("map_type", "road");
                    intent.setClass(getApplicationContext(),road.class);
                    startActivity(intent);
                }
            });



    }

    @Override
    public void onBackPressed() {

        finishAndRemoveTask();

    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}