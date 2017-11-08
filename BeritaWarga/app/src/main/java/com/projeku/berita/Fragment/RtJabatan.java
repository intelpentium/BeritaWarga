package com.projeku.berita.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Fathurrahman on 24/05/2017.
 */

public class RtJabatan extends Fragment {

    Spinner level, nama;

    LinearLayout LayoutDaftar;
    Animation shake;

    String getnama, getlevel;

    String url = "http://sdbundamulia.com/ws_berita/ViewJabatan.php";
    String url_nama = "http://sdbundamulia.com/ws_berita/ViewUser.php";
    String url_add = "http://sdbundamulia.com/ws_berita/InputJabatan.php";

    public static RtJabatan newInstance() {
        return new RtJabatan();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rt_jabatan, container, false);

        LayoutDaftar = (LinearLayout) view.findViewById(R.id.LayoutDaftar);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        level = (Spinner) view.findViewById(R.id.level);
        Listlevel();

        nama = (Spinner) view.findViewById(R.id.nama);
        Listnama();

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Berita();

//                if (!checknama() || !checkPass() || !checkpassword()) {
//                    LayoutDaftar.startAnimation(shake);
//                    return;
//                }
//                else{

//                    SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//
//                    editor.putString("id_level", getlevel);
//                    editor.putString("nama", getnama);
//                    editor.putString("type", "jabatan");
//                    editor.commit();
//
//                    Intent i = new Intent(getActivity(), ActivityMap.class);
//                    startActivity(i);
//                    Ganti();
//                }
            }
        });

        return view;
    }

    public void Listlevel() {

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

                    JSONArray hasilArray = jObj.getJSONArray("level");

                    final String[] datalevel = new String[hasilArray.length()];
                    final String[] id_level = new String[hasilArray.length()];

                    for(int it = 0; it < hasilArray.length(); it++){

                        JSONObject coun = hasilArray.getJSONObject(it);

                        datalevel[it] = coun.getString("nama_level");
                        id_level[it] = coun.getString("id_level");

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                            android.R.layout.simple_spinner_item, datalevel);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    level.setAdapter(adapter);

                    level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                            Toast.makeText(getApplication(), id_level[position], Toast.LENGTH_LONG ).show();
                            getlevel = id_level[position];
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
                String res2 = parsing.sendGetRequest(url);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

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
                        id_user[it] = coun.getString("id_user");

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),
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
                loading = ProgressDialog.show(getActivity(), "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(getActivity(), ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Toast.makeText(getActivity(), "Data berhasil disimpan", Toast.LENGTH_LONG ).show();
//                        Intent i = new Intent(getActivity(), HomeRt.class);
//                        startActivity(i);
//                        finish();
//
//                        uploadImage();
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_user", getnama);
                par2.put("id_level", getlevel);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_add, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
