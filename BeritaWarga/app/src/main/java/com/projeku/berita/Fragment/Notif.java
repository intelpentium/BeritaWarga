package com.projeku.berita.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projeku.berita.ActivityMapNotif;
import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.NotifAdapter;
import com.projeku.berita.Adapter.NotifModel;
import com.projeku.berita.HomeSatpam;
import com.projeku.berita.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fathurrahman on 25/05/2017.
 */

public class Notif extends Fragment {

    private RecyclerView recycler_view;
    private NotifAdapter adapter;
    private List<NotifModel> NotifModelList;

    String url = "http://sdbundamulia.com/ws_berita/ViewNotif.php";
    String url_del = "http://sdbundamulia.com/ws_berita/DeleteNotif.php";

    SwipeRefreshLayout swLayout;

    public static Notif newInstance() {
        return new Notif();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notif, container, false);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

        swLayout = (SwipeRefreshLayout) view.findViewById(R.id.swlayout);
        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                parsingList();

                swLayout.setRefreshing(false);
            }
        });

        parsingList();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Konfirmasi Hapus");
                alertDialog.setMessage("Apakah anda yakin ?");
//                    alertDialog.setIcon(R.drawable.delete);

                alertDialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

//                            deleteData();
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

        return view;
    }

    private void parsingList(){

        class Parsing extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(getActivity(), ""+s2, Toast.LENGTH_LONG).show();

                NotifModelList = new ArrayList<>();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("notif");

                        for (int it = 0; it < hasilArray.length(); it++) {

                            JSONObject coun = hasilArray.getJSONObject(it);

                            NotifModel NotifModelData = new NotifModel();
                            NotifModelData.ID = coun.getInt("id_notif");
                            NotifModelData.nama = coun.getString("nama_user");
                            NotifModelData.email = coun.getString("email_user");
                            NotifModelData.photo = coun.getString("photo");
                            NotifModelData.tgl = coun.getString("tgl");
                            NotifModelData.status = coun.getString("status");
                            NotifModelList.add(NotifModelData);
                        }

                        adapter = new NotifAdapter(getActivity(), NotifModelList);
                        recycler_view.setAdapter(adapter);
                        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter.notifyDataSetChanged();

                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                String id_user = sharedPreferences.getString("id_user","Not Available");

                HashMap<String, String> par = new HashMap<String, String>();
                par.put("id_user", id_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

    public void Delete() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(RencanaKegiatanEdit.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("pos", "notif");
                        editor.commit();

                        Intent i = new Intent(getActivity(), HomeSatpam.class);
                        startActivity(i);

//                        SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        editor.putString("type", "Rencana");
//                        editor.commit();

//                        Intent i = new Intent(RencanaKegiatanEdit.this, Home.class);
//                        startActivity(i);
//                        finish();
//
//                        Toast.makeText(RencanaKegiatanEdit.this, "Selamat anda telah mengubah rencana kegiatan. Tunggu konfirmasi selanjutnya ", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                String id_user = sharedPreferences.getString("id_user","Not Available");

                HashMap<String, String> par = new HashMap<String, String>();
                par.put("id_user", id_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_del, par);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
