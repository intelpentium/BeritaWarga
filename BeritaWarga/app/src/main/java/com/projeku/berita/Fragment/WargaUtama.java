package com.projeku.berita.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.UtamaAdapter;
import com.projeku.berita.Adapter.UtamaModel;
import com.projeku.berita.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fathurrahman on 23/04/2017.
 */

public class WargaUtama extends Fragment {

    private RecyclerView recycler_view;
    private UtamaAdapter adapter;
    private List<UtamaModel> UtamaModelList;

    String url = "http://sdbundamulia.com/ws_berita/ViewBerita.php";

    public static WargaUtama newInstance() {
        return new WargaUtama();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.warga_utama, container, false);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

        parsingList();

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

                UtamaModelList = new ArrayList<>();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("berita");

                        for (int it = 0; it < hasilArray.length(); it++) {

                            JSONObject coun = hasilArray.getJSONObject(it);

                            UtamaModel UtamaModelData = new UtamaModel();
                            UtamaModelData.ID = coun.getInt("id_berita");
                            UtamaModelData.judul = coun.getString("judul");
                            UtamaModelData.berita = coun.getString("berita");
                            UtamaModelData.tgl = coun.getString("tgl");
                            UtamaModelData.imageView = coun.getString("photo");
                            UtamaModelList.add(UtamaModelData);
                        }

                        adapter = new UtamaAdapter(getActivity(), UtamaModelList);
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
//                HashMap<String, String> par = new HashMap<String, String>();
//                par.put("email_user", email_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendGetRequest(url);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }
}
