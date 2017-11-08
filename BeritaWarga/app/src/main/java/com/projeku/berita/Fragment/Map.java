package com.projeku.berita.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.KasAdapter;
import com.projeku.berita.Adapter.KasModel;
import com.projeku.berita.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fathurrahman on 28/04/2017.
 */

public class Map extends Fragment implements OnMapReadyCallback, LocationListener {

    private MapView mapView;
    private GoogleMap googleMap;

    double lat, lon;
    String lokasi;

    LocationManager locationmanager;

    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    double lok_lat=0, lok_lon=0;

    String url = "http://sdbundamulia.com/ws_berita/ViewUser.php";
    int it;

    public static Map newInstance() {
        return new Map();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            //request permission from user if the app hasn't got the required permission
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},   //request specific permission from user
//                    10);
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},   //request specific permission from user
//                    10);
////            return;
//        }
//        else {
//
//            locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
//                    Toast.makeText(getActivity(), "location not found", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(getActivity(), "Provider is null", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        // stop search gps
//        this.locationmanager.removeUpdates(this);

        //    ================= Location ==============
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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

        return view;
    }

    // ---------------- lokasi yg ditampilkan sekarang ------------------
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Lokasi anda"));

        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16));

        lokasi = Double.valueOf(lat)+","+String.valueOf(lon);

//        mMap.setMyLocationEnabled(true);
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

    private void parsingList(){

        class Parsing extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(getActivity(), ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("user");

                        for(it = 0; it < hasilArray.length(); it++){

                            JSONObject coun = hasilArray.getJSONObject(it);

                            googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(coun.getDouble("lat"),coun.getDouble("lon")))
                                .title(coun.getString("nama_user"))
                                .snippet(coun.getString("email_user"))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        }

                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
//                String email_user = sharedPreferences.getString("email_user","Not Available");
//
//                HashMap<String, String> par = new HashMap<String, String>();
//                par.put("hari", "Senin");

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendGetRequest(url);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

    //    --------------- check permission--------------------
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
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
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //    --------------- check On GPS--------------------
    private void showGPSDisabledAlertToUser(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
