package com.projeku.berita;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.projeku.berita.Adapter.A_ParsingRequest;
import com.projeku.berita.Adapter.KasAdapter;
import com.projeku.berita.Adapter.KasModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fathurrahman on 28/04/2017.
 */

public class Kas extends AppCompatActivity {

    private RecyclerView recycler_view;
    private KasAdapter adapter;
    private List<KasModel> KasModelList;

    String url = "http://sdbundamulia.com/ws_berita/ViewUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        parsingList();
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

//                Toast.makeText(Kas.this, ""+s2, Toast.LENGTH_LONG).show();

                KasModelList = new ArrayList<>();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")) {

                        JSONArray hasilArray = jObj.getJSONArray("user");

                        for (int it = 0; it < hasilArray.length(); it++) {

                            JSONObject coun = hasilArray.getJSONObject(it);

                            KasModel KasModelData = new KasModel();
                            KasModelData.ID = coun.getInt("id_user");
                            KasModelData.email = coun.getString("email_user");
                            KasModelData.nama = coun.getString("nama_user");
                            KasModelList.add(KasModelData);
                        }

                        adapter = new KasAdapter(Kas.this, KasModelList);
                        recycler_view.setAdapter(adapter);
                        recycler_view.setLayoutManager(new LinearLayoutManager(Kas.this));
                        adapter.notifyDataSetChanged();

                    }
                }catch (Exception e) {
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

//                SharedPreferences sharedPreferences = Kas.this.getSharedPreferences("session", Context.MODE_PRIVATE);
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
