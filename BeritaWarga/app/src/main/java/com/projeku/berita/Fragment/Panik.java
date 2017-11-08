package com.projeku.berita.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.HashMap;

import at.markushi.ui.CircleButton;

/**
 * Created by Fathurrahman on 25/04/2017.
 */

public class Panik extends Fragment {

    String url = "http://sdbundamulia.com/ws_berita/NotifBerita.php";

    public static Panik newInstance() {
        return new Panik();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.panik, container, false);

        CircleButton panikBtn = (CircleButton) view.findViewById(R.id.panikBtn);
        panikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tambah();
            }
        });

        return view;
    }

    public void Tambah() {

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

                        Toast.makeText(getActivity(), "Pesan telah dikirim !!", Toast.LENGTH_LONG).show();
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

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

}
