package com.projeku.berita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Fragment.*;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Fathurrahman on 10/08/2017.
 */

public class HomeAdmin extends AppCompatActivity {

    //Mendefinisikan variabel
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    String getToken;
    String url = "http://sdbundamulia.com/ws_berita/InputToken.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        //=========== Firebase ============
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        getToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("TOKEN", "Kode :"+FirebaseInstanceId.getInstance().getToken());
//        Toast.makeText(HomeSatpam.this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();

        Token();

        //=========== Permition ============
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, RtUtama.newInstance())
                    .commit();

//            Toast.makeText(getApplicationContext(), ""+savedInstanceState,Toast.LENGTH_LONG).show();
        }

        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.clear();
                editor.commit();

                Intent i = new Intent(HomeAdmin.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){

                    case R.id.home:
                        getSupportActionBar().setTitle("Home");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, RtUtama.newInstance())
                                .commit();
                        return true;

                    case R.id.kejahatan:
                        getSupportActionBar().setTitle("Berita Kejahatan");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, BeritaKejahatan.newInstance())
                                .commit();
                        return true;

                    case R.id.posting:
                        Intent i = new Intent(HomeAdmin.this, RtAdd.class);
                        startActivity(i);
                        return true;

                    case R.id.warga:
                        getSupportActionBar().setTitle("Daftar Warga");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, DaftarWarga.newInstance())
                                .commit();
                        return true;

                    case R.id.ronda:
                        getSupportActionBar().setTitle("Ronda");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, Ronda.newInstance())
                                .commit();
                        return true;

                    case R.id.kas:
                        getSupportActionBar().setTitle("Info Kas");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, KasPeriode.newInstance())
                                .commit();
                        return true;

                    case R.id.lokasi:
                        getSupportActionBar().setTitle("Lokasi");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, Map.newInstance())
                                .commit();
                        return true;

                    case R.id.jabatan:
                        getSupportActionBar().setTitle("Jabatan");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, RtJabatan.newInstance())
                                .commit();
                        return true;

                    case R.id.panik:
                        getSupportActionBar().setTitle("Panik Button");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, Panik.newInstance())
                                .commit();
                        return true;

                    case R.id.notifikasi:
                        getSupportActionBar().setTitle("Panik Button");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, Notif.newInstance())
                                .commit();
                        return true;

                    case R.id.ganti:
                        getSupportActionBar().setTitle("Ganti Password");

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, com.projeku.berita.Fragment.GantiPassword.newInstance())
                                .commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        View header = navigationView.getHeaderView(0);

        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        String nama_user = sharedPreferences.getString("nama_user","Not Available");
        String email_user = sharedPreferences.getString("email_user","Not Available");
        String photo = sharedPreferences.getString("photo","Not Available");

        TextView nama = (TextView) header.findViewById(R.id.nama);
        nama.setText(nama_user);

        TextView email = (TextView) header.findViewById(R.id.email);
        email.setText(email_user);

        ImageView profile = (ImageView) header.findViewById(R.id.profile);
        ImageView gambar = (ImageView) header.findViewById(R.id.gambar);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("type_log", "Admin");
                editor.commit();

                Intent i = new Intent(HomeAdmin.this, EditProfile.class);
                startActivity(i);
            }
        });
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("type_log", "Admin");
                editor.commit();

                Intent i = new Intent(HomeAdmin.this, EditProfile.class);
                startActivity(i);
            }
        });

        Log.e("photo",photo);
        Glide.with(HomeAdmin.this).load(photo)
                .error(R.drawable.ic_profile)
                .into(gambar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void Token() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

//            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(RtAdd.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
//                loading.dismiss();

//                Toast.makeText(RtAdd.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

//                        Intent i = new Intent(RtAdd.this, HomeAdmin.class);
//                        startActivity(i);
//                        finish();
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String email_user = preferences.getString("email_user","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("email_user", email_user);
                par2.put("token", getToken);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
