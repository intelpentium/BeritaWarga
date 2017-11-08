package com.projeku.berita.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Fathurrahman on 24/05/2017.
 */

public class KasDetailOkt extends Fragment {

    Button tambahBtn, deleteBtn;
    ImageView image;

    String level, getid_kas, getbln="Okt";

    String url = "http://sdbundamulia.com/ws_berita/ViewKas.php";
    String url_kas = "http://sdbundamulia.com/ws_berita/KasAdd.php";
    String url_delete = "http://sdbundamulia.com/ws_berita/DeleteKas.php";

    public static KasDetailOkt newInstance() {
        return new KasDetailOkt();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kas_detail_frag, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        level = preferences.getString("id_level","-");

        tambahBtn = (Button) view.findViewById(R.id.tambahBtn);
        deleteBtn = (Button) view.findViewById(R.id.deleteBtn);
        image = (ImageView) view.findViewById(R.id.image);

        parsingList();

        return view;
    }

    private void parsingList(){

        class Parsing extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Menampilkan Data...", "Silahkan Tunggu..." ,false, false);
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

                    if(result.trim().equals("1")) {

                        tambahBtn.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);

                        getid_kas = jObj.getString("id_kas");

                        if(level.equals("3") || level.equals("5")){

                            deleteBtn.setVisibility(View.VISIBLE);
                            deleteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                    alertDialog.setTitle("Confirm Hapus");
                                    alertDialog.setMessage("Apakah anda yakin ?");

                                    alertDialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {

                                            Delete();
                                        }
                                    });
                                    alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }else{
                            deleteBtn.setVisibility(View.GONE);
                        }


                    }else {

                        deleteBtn.setVisibility(View.GONE);
                        image.setVisibility(View.GONE);

                        if(level.equals("3") || level.equals("5")){
                            tambahBtn.setVisibility(View.VISIBLE);

                            tambahBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Tambah();
                                }
                            });
                        }else{
                            tambahBtn.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email","Not Available");
                String tahun = sharedPreferences.getString("tahun","Not Available");
//
                HashMap<String, String> par = new HashMap<String, String>();
                par.put("email", email);
                par.put("bulan", getbln);
                par.put("tahun", tahun);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

    private void Tambah(){

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

                        Toast.makeText(getActivity(), "Selamat !! Pembayaran berhasil", Toast.LENGTH_LONG).show();

                        tambahBtn.setVisibility(View.GONE);
                        deleteBtn.setVisibility(View.VISIBLE);
                        image.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email","Not Available");
                String tahun = sharedPreferences.getString("tahun","Not Available");
//
                HashMap<String, String> par = new HashMap<String, String>();
                par.put("email_user", email);
                par.put("bulan", getbln);
                par.put("tahun", tahun);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url_kas, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

    private void Delete(){

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

                        Toast.makeText(getActivity(), "Data berhasil diubah", Toast.LENGTH_LONG).show();

                        deleteBtn.setVisibility(View.GONE);
                        image.setVisibility(View.GONE);

                        if(level.equals("3") || level.equals("5")){
                            tambahBtn.setVisibility(View.VISIBLE);

                            tambahBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Tambah();
                                }
                            });
                        }else{
                            tambahBtn.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
//                String email = sharedPreferences.getString("email","Not Available");
//                String tahun = sharedPreferences.getString("tahun","Not Available");
//
                HashMap<String, String> par = new HashMap<String, String>();
                par.put("id_kas", getid_kas);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url_delete, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }
}
