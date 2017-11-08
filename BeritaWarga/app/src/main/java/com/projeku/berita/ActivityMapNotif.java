package com.projeku.berita;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fathurrahman on 25/05/2017.
 */

public class ActivityMapNotif extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    LocationManager locationmanager;
//    double lat, lon;
    double lok_lat, lok_lon;

    String nama;

    SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    String url = "http://sdbundamulia.com/ws_berita/ViewLocation.php";

    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    double lat=0, lon=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // =========================== Location ===============================
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //request permission from user if the app hasn't got the required permission
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},   //request specific permission from user
//                    1);
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},   //request specific permission from user
//                    1);
//            return ;
//        }
//        else {
//
//            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Criteria cri = new Criteria();
//            String provider = locationmanager.getBestProvider(cri, false);
//
//            if (provider != null & !provider.equals("")) {
//                Location location = locationmanager.getLastKnownLocation(provider);
//                locationmanager.requestLocationUpdates(provider, 2000, 1, this);
//
//                if (location != null) {
//                    onLocationChanged(location);
//                } else {
//                    Toast.makeText(this, "location not found", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(this, "Provider is null", Toast.LENGTH_LONG).show();
//            }
//
//            // stop search gps
//            this.locationmanager.removeUpdates(this);
//        }

        //    ================= Location ==============
        locationManager = (LocationManager) ActivityMapNotif.this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            checkLocationPermission();
        }
        else{
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                showGPSDisabledAlertToUser();
            }
        }

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        final Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
//            locationManager.requestLocationUpdates(provider, 2000, 1, (LocationListener) this);

            locationManager.requestLocationUpdates(provider, 2000, 1, this);

            lat = location.getLatitude();
            lon = location.getLongitude();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    onLocationChanged(location);
                    handler.postDelayed(this, 5000);

                }
            }, 5000);

//            url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lok_lat+","+lok_lon+"&sensor=true";

//            ViewAddress();

        } else {
            final Location lastlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(provider, 2000, 1, this);
            if (lastlocation != null) {

                lat = lastlocation.getLatitude();
                lon = lastlocation.getLongitude();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        onLocationChanged(lastlocation);
                        handler.postDelayed(this, 5000);

                    }
                }, 5000);

//                url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lok_lat+","+lok_lon+"&sensor=true";

//                ViewAddress();

            }else{
                showGPSDisabledAlertToUser();
            }
        }

        parsingList();
    }


    // ---------------- lokasi yg ditampilkan sekarang ------------------
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .draggable(true)
                .title("Lokasi Anda"));

        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10));
    }

    // ---------------- location ------------------
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
//            ActivityCompat.finishAfterTransition(this);

            SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("pos", "notif");
            editor.commit();

            Intent i = new Intent(ActivityMapNotif.this, HomeSatpam.class);
            startActivity(i);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("pos", "notif");
        editor.commit();

        Intent i = new Intent(ActivityMapNotif.this, HomeSatpam.class);
        startActivity(i);
        finish();
    }


    private void parsingList(){

        class Parsing extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(ActivityMapNotif.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        lok_lat = jObj.getDouble("lat");
                        lok_lon = jObj.getDouble("lon");
                        nama = jObj.getString("nama_user");

                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lok_lat,lok_lon))
                                .title(nama)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email","Not Available");

                HashMap<String, String> par = new HashMap<String, String>();
                par.put("email", email);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

    //    --------------- check permission--------------------
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(ActivityMapNotif.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMapNotif.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(ActivityMapNotif.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ActivityMapNotif.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else{

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                showGPSDisabledAlertToUser();
            }

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(ActivityMapNotif.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED){

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                            showGPSDisabledAlertToUser();
                        }

                        if (mGoogleApiClient == null){
//                            buildGoogleApiClient();
                        }
                    }
                }
                else{
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ActivityMapNotif.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //    --------------- check On GPS--------------------
    private void showGPSDisabledAlertToUser(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityMapNotif.this);
        alertDialogBuilder.setMessage("GPS anda tidak aktif. Apakah anda akan mengaktifkan ?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

//                        mapFragment.getMapAsync(MapsActivity.this);

//                        Intent i = new Intent(RealisasiKegiatanAdd.this, RealisasiKegiatanAdd.class);
//                        startActivity(i);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
