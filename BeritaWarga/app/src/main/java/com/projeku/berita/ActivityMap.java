package com.projeku.berita;

import android.content.Context;
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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fathurrahman on 23/05/2017.
 */

public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        LocationListener {

    LocationManager locationmanager;
    double lat, lon;
    double lok_lat, lok_lon;

    SupportMapFragment mapFragment;

    private static GoogleMap map;

    String address, negara;
    String url = "http://sdbundamulia.com/ws_berita/Signup.php";

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},   //request specific permission from user
                    1);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},   //request specific permission from user
                    1);
            return ;
        }
        else {

            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria cri = new Criteria();
            String provider = locationmanager.getBestProvider(cri, false);

            if (provider != null & !provider.equals("")) {
                Location location = locationmanager.getLastKnownLocation(provider);
                locationmanager.requestLocationUpdates(provider, 2000, 1, this);

                if (location != null) {
                    onLocationChanged(location);
                } else {
                    Toast.makeText(this, "location not found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Provider is null", Toast.LENGTH_LONG).show();
            }

            // stop search gps
            this.locationmanager.removeUpdates(this);
        }
    }


    // ---------------- lokasi yg ditampilkan sekarang ------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // ambil marker ke googleMap
        map = googleMap;
        map.setOnMapClickListener(this);

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .draggable(true)
                .title("Klik ke tempat mana saja yang anda inginkan"));

        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16));

        lok_lat = lat;
        lok_lon = lon;
//        lokasi = Double.valueOf(lat)+","+String.valueOf(lon);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            if (addresses != null && addresses.size() > 0) {
                String alamat = addresses.get(0).getAddressLine(0);
                String lokasi = addresses.get(0).getSubLocality();
                String kota = addresses.get(0).getSubAdminArea();
                String codePos = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();

                address = alamat+", "+lokasi+", "+kota+", "+codePos;
                negara = country;

                Toast.makeText(this, address+" - "+negara, Toast.LENGTH_LONG).show();
            }


        } catch (IOException e) {

        }
    }

    // ---------------- klik lokasi ------------------
    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

        String lokasi1 = latLng.toString().substring(10).replace(")", "");
        String[] pecah = lokasi1.split(",");

        lok_lat = Double.valueOf(pecah[0]);
        lok_lon = Double.valueOf(pecah[1]);

        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(pecah[0]), Double.parseDouble(pecah[1]), 1);

            if (addresses != null && addresses.size() > 0) {
                String alamat = addresses.get(0).getAddressLine(0);
                String lokasi = addresses.get(0).getSubLocality();
                String kota = addresses.get(0).getSubAdminArea();
                String codePos = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();

                address = alamat+", "+lokasi+", "+kota+", "+codePos;
                negara = country;

                Toast.makeText(this, address+" - "+negara, Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {

        }
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this);
            return true;
        }
        if (id == R.id.simpan) {
//            Toast.makeText(getApplicationContext(), lokasi , Toast.LENGTH_LONG).show();

            Daftar();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Daftar() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(ActivityMap.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Toast.makeText(ActivityMap.this, "Selamat !! Data telah terdaftar", Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                        String type = preferences.getString("type","Not Available");

                        if(type.equals("jabatan")){
                            Intent i = new Intent(ActivityMap.this, HomeRt.class);
                            startActivity(i);
                            finish();
                        }
                        if(type.equals("daftar")){

                            SharedPreferences.Editor editor = preferences.edit();

                            editor.clear();
                            editor.commit();

                            Intent i = new Intent(ActivityMap.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String id_level = preferences.getString("id_level","Not Available");
                String nama = preferences.getString("nama","Not Available");
                String email = preferences.getString("email","Not Available");
                String password = preferences.getString("password","Not Available");
                String rw = preferences.getString("rw","Not Available");
                String rt = preferences.getString("rt","Not Available");
                String no = preferences.getString("no","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_level", id_level);
                par2.put("nama_user", nama);
                par2.put("email_user", email);
                par2.put("password", password);
                par2.put("rw", rw);
                par2.put("rt", rt);
                par2.put("no", no);
                par2.put("lat", String.valueOf(lok_lat));
                par2.put("lon", String.valueOf(lok_lon));

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);

                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
