package com.projeku.berita.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.projeku.berita.ActivityMap;
import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.RondaAdapter;
import com.projeku.berita.Adapter.RondaModel;
import com.projeku.berita.Adapter.UtamaAdapter;
import com.projeku.berita.Adapter.UtamaModel;
import com.projeku.berita.R;
import com.projeku.berita.SatpamAdd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fathurrahman on 29/04/2017.
 */

public class RondaSen extends Fragment {

    private RecyclerView recycler_view;
    private RondaAdapter adapter;
    private List<RondaModel> RondaModelList;

    String url = "http://sdbundamulia.com/ws_berita/ViewRonda.php";

    public static RondaSen newInstance() {
        return new RondaSen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ronda_sen, container, false);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

        parsingList();

        SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        String level = preferences.getString("id_level","-");

        Button tambahBtn = (Button) view.findViewById(R.id.tambahBtn);

        if(level.equals("2") || level.equals("5")){
            tambahBtn.setVisibility(View.VISIBLE);

            tambahBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("hari", "Senin");
                    editor.commit();

                    Intent i = new Intent(getActivity(), SatpamAdd.class);
                    startActivity(i);
                }
            });
        }
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

                RondaModelList = new ArrayList<>();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("ronda");

                        for (int it = 0; it < hasilArray.length(); it++) {

                            JSONObject coun = hasilArray.getJSONObject(it);

                            RondaModel RondaModelData = new RondaModel();
                            RondaModelData.ID = coun.getInt("id_ronda");
                            RondaModelData.nama = coun.getString("email_user");
                            RondaModelList.add(RondaModelData);
                        }

                        adapter = new RondaAdapter(getActivity(), RondaModelList);
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

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
//                String email_user = sharedPreferences.getString("email_user","Not Available");
//
                HashMap<String, String> par = new HashMap<String, String>();
                par.put("hari", "Senin");

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url, par);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }

}
