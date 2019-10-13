package com.example.dig4634.dig4634_module4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


//Your activity must implement a LocationListener so that it listens to onLocationChanged events.
public class MainActivity extends AppCompatActivity implements LocationListener {


    //This integer serves as a "memory address" for storing and retrieving the user's permission status
    final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION=0;
    boolean I_have_the_key=false;
    boolean I_have_the_skull=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //This retrieves the user's permission status.
        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;


        TextView myLabel=findViewById(R.id.myLabel);
        if(permissionAccessFineLocationApproved) {

            //This block of code will run if the user has previously granted permissions.

            myLabel.setText("User has previously provided permission.");

            //Start location services
            startGPS();

        }
        else {

            //This block of code will run if the user has not granted permissions yet.

            myLabel.setText("User has not provided permission yet.");

            //This requests permission by opening a standardized dialog box.
            ActivityCompat.requestPermissions( this,
                    new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION );


        }


    }

    //This piece of code will run when the user responds to a permission dialog box.
    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_ACCESS_FINE_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                //This block of code will run if the user clicks on the "DENY" button.

                finish();//terminate the application if the user does not provide permission
            }
            else {

                //This block of code will run if the user clicks on the "ALLOW" button.

                //the user just provided permission
                startGPS();


            }
        }
    }

    //Here I put the necessary code to start the Location Service.
    private void startGPS() {
        //First we make sure that the user has indeed granted permissions.
        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
        if (permissionAccessFineLocationApproved) {

            //And then we start the location service.
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
        }
    }

    //These are some additional events related to location services, but we will not use them in this app.
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) { }
    @Override
    public void onProviderEnabled(String s) { }
    @Override
    public void onProviderDisabled(String s) { }


    //This is the method that runs every time a new location is available.
    //The information about this new location is provided in the given "location" object.
    @Override
    public void onLocationChanged(Location location) {

        DecimalFormat df = new DecimalFormat("###.#####");

        TextView myLabel=findViewById(R.id.myLabel);
         myLabel.setText("LONGITUDE: "+ df.format(location.getLongitude())+"\nLATITUDE: "+ df.format(location.getLatitude()));

        //If I am around Norman Gym
        if( Math.abs(-82.3404267 - location.getLongitude())<0.001 &&
                Math.abs(29.646736 - location.getLatitude())<0.001)
        {

            //Open the Norman Gym Activity
            //myLabel.setText("You are at the Norman Gym");

            if(!I_have_the_key) {

                I_have_the_key = true;

                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "You found the key! Now you can open the door and acquire the scroll.");
                my_intent.putExtra("button", "Go to the Century Tower!");
                my_intent.putExtra("image", R.drawable.key);
                startActivity(my_intent);
            }
            else
            {
                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "This is where you found the key to the door. There is nothing else here.");
                my_intent.putExtra("button", "Go to the Century Tower!");
                my_intent.putExtra("image", 0);
                startActivity(my_intent);
            }
        }
        //if I am around Century Tower
        else if( Math.abs(-82.3454497 - location.getLongitude())<0.001 &&
                Math.abs(29.6488011 - location.getLatitude())<0.001){

            //Open the Century Tower Activity
            //myLabel.setText("You are at the Century Tower");

            if(I_have_the_key){

                I_have_the_skull=true;

                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "You opened the door and acquired the scroll.");
                my_intent.putExtra("button", "Go to the O'Connell Center and return the scroll to its rightful place.");
                my_intent.putExtra("image", R.drawable.scroll);
                startActivity(my_intent);

            }else {

                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "The scroll is locked behind this door. You need to find the key!");
                my_intent.putExtra("button", "Go to the Norman Gym!");
                my_intent.putExtra("image", R.drawable.door);
                startActivity(my_intent);
            }
        }
        //if I am around O'Connell Center
        else if( Math.abs(-82.3532618 - location.getLongitude())<0.001 &&
                Math.abs(29.6493856 - location.getLatitude())<0.001){

            //Open the O'Connell Center Activity
            //myLabel.setText("You are at the O'Connell Center");

            if(I_have_the_skull){
                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "Well done, brave one.");
                my_intent.putExtra("button", "QUEST COMPLETE.");
                my_intent.putExtra("image", R.drawable.scroll_returned);
                startActivity(my_intent);
            }
            else {
                Intent my_intent = new Intent(getBaseContext(), InfoActivity.class);
                my_intent.putExtra("text", "You have not yet acquired the scroll. Return here once you have found it.");
                my_intent.putExtra("button", "Go to the century tower!");
//                my_intent.putExtra("image", R.drawable.empty_cage);
                startActivity(my_intent);
            }

        }
    }



    //Calculates the approximate geodesic distance in meters between two geographical locations.
    private double distance(double lon1, double lat1, Location loc) {
        double theta = lon1 - loc.getLongitude();double dist = Math.sin(deg2rad(lat1))*  Math.sin(deg2rad(loc.getLatitude()))+ Math.cos(deg2rad(lat1))*  Math.cos(deg2rad(loc.getLatitude()))*  Math.cos(deg2rad(theta));dist = Math.acos(dist);dist = rad2deg(dist);dist = dist * 60 * 1.1515* 1000.0;return (dist);
    }
    private double deg2rad(double deg) {return (deg * Math.PI / 180.0);}private double rad2deg(double rad) {return (rad * 180.0 / Math.PI);}

}
