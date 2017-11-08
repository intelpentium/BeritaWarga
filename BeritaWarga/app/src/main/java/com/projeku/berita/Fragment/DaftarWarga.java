package com.projeku.berita.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.WargaAdapter;
import com.projeku.berita.Adapter.WargaModel;
import com.projeku.berita.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fathurrahman on 10/08/2017.
 */

public class DaftarWarga extends Fragment {

    private RecyclerView recycler_view;
    private WargaAdapter adapter;
    private List<WargaModel> wargaModelList;

    String url = "http://sdbundamulia.com/ws_berita/ViewUser.php";

    ProgressBar progress;
    SwipeRefreshLayout swLayout;

    public static DaftarWarga newInstance() {
        return new DaftarWarga();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daftar_warga, container, false);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        progress    =(ProgressBar) view.findViewById(R.id.progressbar_loading);

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

        return view;
    }

    private void parsingList(){

        class Parsing extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

                progress.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), ""+s2, Toast.LENGTH_LONG).show();

                wargaModelList = new ArrayList<>();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("user");

                        for (int it = 0; it < hasilArray.length(); it++) {

                            JSONObject coun = hasilArray.getJSONObject(it);

                            WargaModel wargaModelData = new WargaModel();
                            wargaModelData.ID = coun.getInt("id_user");
                            wargaModelData.nama = coun.getString("nama_user");
                            wargaModelData.email = coun.getString("email_user");
                            wargaModelData.photo = coun.getString("photo");
                            wargaModelList.add(wargaModelData);
                        }

                        adapter = new WargaAdapter(getActivity(), wargaModelList);
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
//                par.put("hari", "Senin");

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendGetRequest(url);
                return res;
            }
        }

        Parsing tambah = new Parsing();
        tambah.execute();
    }
}
