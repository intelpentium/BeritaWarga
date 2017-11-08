package com.projeku.berita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fathurrahman on 24/05/2017.
 */

public class SatpamAdd extends AppCompatActivity {

    Spinner nama;

    LinearLayout LayoutLogin;
    Animation shake;

    String getnama;

    String url = "http://sdbundamulia.com/ws_berita/SatpamAdd.php";
    String url_nama = "http://sdbundamulia.com/ws_berita/ViewUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satpam_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
//        shake = AnimationUtils.loadAnimation(SatpamAdd.this, R.anim.shake);
//
//        email = (EditText) findViewById(R.id.email);
//        email.addTextChangedListener(new text(email));

        nama = (Spinner) findViewById(R.id.nama);
        Listnama();

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!checkEmail()) {
//                    LayoutLogin.startAnimation(shake);
////                    Snackbar.make(findViewById(android.R.id.con tent), "Data tidak sesuai !!", Snackbar.LENGTH_LONG)
////                            .setAction("Action", null).show();
//                    return;
//                }
//                else{

                    Berita();
//                }
            }
        });

    }

//    private class text implements TextWatcher {
//
//        private View view;
//
//        private text(View view){
//            this.view = view;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//            switch (view.getId()){
//                case R.id.email:
//                    checkEmail();
////                    Email();
//                    break;
//            }
//
//        }
//    }
//
//    private boolean checkEmail(){
//
//        getemail = email.getText().toString().trim();
//
//        String check = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
//
//        Pattern pa = Pattern.compile(check);
//        Matcher ma = pa.matcher(getemail);
//
//        if(getemail.isEmpty()){
//
//            email.setError("Email tidak boleh kosong");
//            return false;
//        }
//        else if(!ma.find()){
//            email.setError("Email tidak sesuai !!");
//            return false;
//        }
//        return true;
//    }

    public void Listnama() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah

                    JSONArray hasilArray = jObj.getJSONArray("user");

                    final String[] datauser = new String[hasilArray.length()];
                    final String[] id_user = new String[hasilArray.length()];

                    for(int it = 0; it < hasilArray.length(); it++){

                        JSONObject coun = hasilArray.getJSONObject(it);

                        datauser[it] = coun.getString("nama_user");
                        id_user[it] = coun.getString("email_user");

                    }

                    ArrayAdapter adapter = new ArrayAdapter(SatpamAdd.this,
                            android.R.layout.simple_spinner_item, datauser);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    nama.setAdapter(adapter);

                    nama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                            Toast.makeText(getApplication(), id_level[position], Toast.LENGTH_LONG ).show();
                            getnama = id_user[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

//                HashMap<String, String> par2 = new HashMap<String, String>();
//                par2.put("id_provinsi", getprovinsi);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendGetRequest(url_nama);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

    public void Berita() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SatpamAdd.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(SatpamAdd.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Toast.makeText(SatpamAdd.this, "Jadwal ronda berhasil ditambahkan", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(SatpamAdd.this, HomeSatpam.class);
                        startActivity(i);
                        finish();
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String hari = preferences.getString("hari","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("email_user", getnama);
                par2.put("hari", hari);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // difungsikan untuk mengembalikan ke dashboard atau menu sebelumnya
            ActivityCompat.finishAfterTransition(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
