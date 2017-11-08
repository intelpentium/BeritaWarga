package com.projeku.berita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fathurrahman on 21/04/2017.
 */

public class Daftar extends AppCompatActivity {

    EditText email, password, nama;
    Button submitBtn, regisInves;

    LinearLayout LayoutDaftar;
    Animation shake;

    String getemail, getpassword, getnama, getrw, getrt, getno;

    String[] bahasa ={"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutDaftar = (LinearLayout) findViewById(R.id.LayoutDaftar);
        shake = AnimationUtils.loadAnimation(Daftar.this, R.anim.shake);

        nama = (EditText) findViewById(R.id.nama);
        nama.addTextChangedListener(new text(nama));

        email = (EditText) findViewById(R.id.email);
        email.addTextChangedListener(new text(email));

        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(new text(password));

        nama = (EditText) findViewById(R.id.nama);
        nama.addTextChangedListener(new text(nama));

        Spinner rw = (Spinner) findViewById(R.id.rw);
        rw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                Toast.makeText(getApplicationContext(), bahasa[position], Toast.LENGTH_LONG).show();
                getrw = bahasa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(Daftar.this, android.R.layout.simple_spinner_item, bahasa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rw.setAdapter(adapter);

        Spinner rt = (Spinner) findViewById(R.id.rt);
        rt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                Toast.makeText(getApplicationContext(), bahasa[position], Toast.LENGTH_LONG).show();
                getrt = bahasa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(Daftar.this, android.R.layout.simple_spinner_item, bahasa);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rt.setAdapter(adapter2);


        Spinner no = (Spinner) findViewById(R.id.no);
        no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                Toast.makeText(getApplicationContext(), bahasa[position], Toast.LENGTH_LONG).show();
                getno = bahasa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter3 = new ArrayAdapter(Daftar.this, android.R.layout.simple_spinner_item, bahasa);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        no.setAdapter(adapter3);

        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checknama() || !checkEmail() || !checkPass() ) {
                    LayoutDaftar.startAnimation(shake);
//                    Snackbar.make(findViewById(android.R.id.con tent), "Data tidak sesuai !!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }
                else{

                    SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("id_level", "1");
                    editor.putString("nama", getnama);
                    editor.putString("email", getemail);
                    editor.putString("password", getpassword);
                    editor.putString("rw", getrw);
                    editor.putString("rt", getrt);
                    editor.putString("no", getno);
                    editor.putString("type", "daftar");
                    editor.commit();

                    Intent i = new Intent(Daftar.this, ActivityMap.class);
                    startActivity(i);
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
                case R.id.email:
                    checkEmail();
//                    Email();
                    break;

                case R.id.password:
                    checkPass();
//                    Email();
                    break;

                case R.id.nama:
                    checknama();
//                    Email();
                    break;
            }

        }
    }

    private boolean checkEmail(){

        getemail = email.getText().toString().trim();

        String check = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

        Pattern pa = Pattern.compile(check);
        Matcher ma = pa.matcher(getemail);

        if(getemail.isEmpty()){

            email.setError("Email tidak boleh kosong");
            return false;
        }
        else if(!ma.find()){
            email.setError("Email tidak sesuai !!");
            return false;
        }
        return true;
    }

    private boolean checkPass(){

        getpassword = password.getText().toString().trim();

        if(getpassword.isEmpty()){

            password.setError("Password tidak boleh kosong");
            return false;
        }
        return true;
    }

    private boolean checknama(){

        getnama = nama.getText().toString().trim();

        if(getnama.isEmpty()){

            nama.setError("nama tidak boleh kosong");
            return false;
        }
        return true;
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
