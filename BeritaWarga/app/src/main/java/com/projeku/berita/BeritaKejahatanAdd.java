package com.projeku.berita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Fathurrahman on 10/08/2017.
 */

public class BeritaKejahatanAdd extends AppCompatActivity {

    EditText judul, berita;

    LinearLayout LayoutLogin;
    Animation shake;

    String getjudul, getberita;

    String url = "http://sdbundamulia.com/ws_berita/InputBeritaKejahatan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.berita_kejahatan_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        shake = AnimationUtils.loadAnimation(BeritaKejahatanAdd.this, R.anim.shake);

        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        String nama_pelapor = preferences.getString("nama_pelapor","Not Available");

        TextView nama = (TextView) findViewById(R.id.nama);
        nama.setText(nama_pelapor);

        judul = (EditText) findViewById(R.id.judul);
        judul.addTextChangedListener(new BeritaKejahatanAdd.text(judul));

        berita = (EditText) findViewById(R.id.berita);
        berita.addTextChangedListener(new BeritaKejahatanAdd.text(berita));

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkjudul() || !checkPass() ) {
                    LayoutLogin.startAnimation(shake);
//                    Snackbar.make(findViewById(android.R.id.con tent), "Data tidak sesuai !!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }
                else{

                    Berita();
                }
            }
        });
    }

    private class text implements TextWatcher {

        private View view;

        private text(View view){
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (view.getId()){
                case R.id.judul:
                    checkjudul();
//                    judul();
                    break;

                case R.id.berita:
                    checkPass();
//                    judul();
                    break;
            }

        }
    }

    private boolean checkjudul(){

        getjudul = judul.getText().toString().trim();

        if(getjudul.isEmpty()){

            judul.setError("Judul tidak boleh kosong");
            return false;
        }
        return true;
    }

    private boolean checkPass(){

        getberita = berita.getText().toString().trim();

        if(getberita.isEmpty()){

            berita.setError("Berita tidak boleh kosong");
            return false;
        }
        return true;
    }

    public void Berita() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BeritaKejahatanAdd.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(BeritaKejahatanAdd.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Toast.makeText(BeritaKejahatanAdd.this, "Berita kejahatan berhasil disimpan", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(BeritaKejahatanAdd.this, HomeRt.class);
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
                String email_pelapor = preferences.getString("email_pelapor","Not Available");
                String id_laporan_notif = preferences.getString("id_laporan_notif","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_laporan_notif", id_laporan_notif);
                par2.put("email_user", email_pelapor);
                par2.put("judul", getjudul);
                par2.put("berita", getberita);

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
