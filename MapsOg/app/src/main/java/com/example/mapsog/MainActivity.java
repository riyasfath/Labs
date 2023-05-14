package com.example.mapsog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button showmap, getcrntln, lcnlsnr;
    TextView tvlocation;
    double lat,lng;
    LocationManager locationManager;
    ArrayList<String> loclist=new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lat = lng = 0.00;

        showmap = findViewById(R.id.show);
        getcrntln = findViewById(R.id.crntlcn);
        lcnlsnr = findViewById(R.id.lcnlsnr);
        tvlocation = findViewById(R.id.txt);
        listView = findViewById(R.id.lst);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        List<String> providers = locationManager.getProviders(new Criteria(), true);


        String provider = "";
        for (String pro : providers) {
            provider += pro + "\n";
        }

        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();

        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("lati",lat);
                intent.putExtra("long",lng);
                startActivity(intent);
            }
        });


        getcrntln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!providerEnabled) {
                    EnableGPS();
                } else
                    GetLocation();

            }
        });

    }

    private void GetLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] perm = {

                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this,perm,1);

        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if(location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        Toast.makeText(getApplicationContext(), "Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude(), Toast.LENGTH_SHORT).show();

                        String loc="Latitude :" + lat+ " Longitude:"+lng;
                        loclist.add(loc);

                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, loclist);
                        listView.setAdapter(arrayAdapter);
                    }
                }
            });
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            String[] perm = {

                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this,perm,1);

        }
        else {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location!=null){
                lat = Double.parseDouble(location.getLatitude()+"");
                lng = Double.parseDouble(location.getLongitude()+"");
                tvlocation.setText("Lattitude : "+lat +"\n Longitude :"+ lng);

            }
        }




    }

    private void EnableGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
}