package com.projeku.berita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fathurrahman on 21/04/2017.
 */

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginDebit, loginInves;

    LinearLayout LayoutLogin;
    Animation shake;

    String getemail, getpassword;
    String url = "http://sdbundamulia.com/ws_berita/Login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        String level = preferences.getString("id_level","-");

        if(level.equals("1")){
            Intent i = new Intent(Login.this, HomeWarga.class);
            startActivity(i);
            finish();
        }
        if(level.equals("2")){
            Intent i = new Intent(Login.this, HomeSatpam.class);
            startActivity(i);
            finish();
        }
        if(level.equals("4")){
            Intent i = new Intent(Login.this, HomeRt.class);
            startActivity(i);
            finish();
        }

        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        shake = AnimationUtils.loadAnimation(Login.this, R.anim.shake);

        email = (EditText) findViewById(R.id.email);
        email.addTextChangedListener(new text(email));

        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(new text(password));

        TextView forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Lupa.class);
                startActivity(i);
            }
        });

        TextView regis = (TextView) findViewById(R.id.regis);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Daftar.class);
                startActivity(i);
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkEmail() || !checkPass() ) {
                    LayoutLogin.startAnimation(shake);
//                    Snackbar.make(findViewById(android.R.id.con tent), "Data tidak sesuai !!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }
                else{

                    Login();
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

    // cek ke database
    public void Login() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Proses Login...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(Login.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("id_user", jObj.getString("id_user"));
                        editor.putString("email_user", jObj.getString("email_user"));
                        editor.putString("nama_user", jObj.getString("nama_user"));
                        editor.putString("id_level", jObj.getString("id_level"));
                        editor.putString("photo", jObj.getString("photo"));
                        editor.commit();

                        String level = jObj.getString("id_level");
                        if(level.equals("1")){
                            Intent i = new Intent(Login.this, HomeWarga.class);
                            startActivity(i);
                            finish();
                        }
                        if(level.equals("2")){
                            Intent i = new Intent(Login.this, HomeSatpam.class);
                            startActivity(i);
                            finish();
                        }
                        if(level.equals("3")){
                            Intent i = new Intent(Login.this, HomeBendahara.class);
                            startActivity(i);
                            finish();
                        }
                        if(level.equals("4")){
                            Intent i = new Intent(Login.this, HomeRt.class);
                            startActivity(i);
                            finish();
                        }
                        if(level.equals("5")){
                            Intent i = new Intent(Login.this, HomeAdmin.class);
                            startActivity(i);
                            finish();
                        }
                    }else{
                        LayoutLogin.startAnimation(shake);
                        Snackbar.make(findViewById(android.R.id.content), "Email tidak terdaftar !!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        email.setError("Email tidak terdaftar !!");
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("email_user", getemail);
                par2.put("password", getpassword);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
