package com.projeku.berita.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Login;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Fathurrahman on 29/04/2017.
 */

public class GantiPassword extends Fragment {

    EditText current_pass, new_pass, confirm;

    LinearLayout LayoutDaftar;
    Animation shake;

    String getcurrent_pass, getnew_pass, getconfirm;

    String url = "http://sdbundamulia.com/ws_berita/GantiPassword.php";

    public static GantiPassword newInstance() {
        return new GantiPassword();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ganti_password, container, false);

        LayoutDaftar = (LinearLayout) view.findViewById(R.id.LayoutDaftar);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        current_pass = (EditText) view.findViewById(R.id.current_pass);
        current_pass.addTextChangedListener(new text(current_pass));

        new_pass = (EditText) view.findViewById(R.id.new_pass);
        new_pass.addTextChangedListener(new text(new_pass));

        confirm = (EditText) view.findViewById(R.id.confirm);
        confirm.addTextChangedListener(new text(confirm));

        Button regisDebit = (Button) view.findViewById(R.id.regisDebit);
        regisDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (!checkcurrent_pass() || !checkPass() || !checkConfirm()) {
                LayoutDaftar.startAnimation(shake);
                return;
            }
            else{

                Ganti();
            }
            }
        });

        return view;
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
                case R.id.current_pass:
                    checkcurrent_pass();
                    break;

                case R.id.new_pass:
                    checkPass();
                    break;

                case R.id.confirm:
                    checkConfirm();
                    break;
            }

        }
    }

    private boolean checkcurrent_pass(){

        getcurrent_pass = current_pass.getText().toString().trim();

        if(getcurrent_pass.isEmpty()){

            current_pass.setError("Password lama tidak boleh kosong");
            return false;
        }
        return true;
    }

    private boolean checkPass(){

        getnew_pass = new_pass.getText().toString().trim();

        if(getnew_pass.isEmpty()){

            new_pass.setError("Password baru tidak boleh kosong");
            return false;
        }
        return true;
    }

    private boolean checkConfirm(){

        getconfirm = confirm.getText().toString().trim();

        if(getconfirm.isEmpty()){

            confirm.setError("Confirm password tidak boleh kosong");
            return false;
        }
        else if(!getconfirm.equals(getnew_pass)){

            confirm.setError("Password tidak sesuai");
            return false;
        }
        return true;
    }

    public void Ganti() {

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
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.clear();
                        editor.commit();

                        Intent i = new Intent(getActivity(), Login.class);
                        startActivity(i);
                        getActivity().finish();

                    }else{
                        LayoutDaftar.startAnimation(shake);
//                        Snackbar.make(findViewById(android.R.id.content), "email_user telah terdaftar !!", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();

                        current_pass.setError("Password tidak terdaftar!!");
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                String email_user = preferences.getString("email_user","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("email_user", email_user);
                par2.put("pass_lama", getcurrent_pass);
                par2.put("pass_baru", getconfirm);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
